package ru.spbstu.frauddetection.sparkmanager;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import ru.spbstu.frauddetection.FraudConfig.ConfigurationParser.ConfigurationParser;
import ru.spbstu.frauddetection.FraudConfig.ObjectModel.Configuration;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import ru.spbstu.frauddetection.InputDataCalculator.InputCalculator;
import ru.spbstu.frauddetection.InputDataCalculator.InputGroup;
import ru.spbstu.frauddetection.InputDataCalculator.InputType;
import ru.spbstu.frauddetection.datastorage.AbstractData;
import ru.spbstu.frauddetection.datastorage.HDFSData;
import ru.spbstu.frauddetection.datastorage.MockData;
import scala.Tuple2;

import java.io.FileInputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class SparkManager implements Serializable {
    private final static Logger logger = Logger.getLogger(SparkManager.class);

    private static Configuration configuration;

    public static final Properties properties = new Properties();
    private static final String PROPERTIESFILE = "./etc/SparkManager.properties";
    private static final String XML_CONGIG_PATH_KEY = "spark_manager.xml_config_path";
    private static final String TOPIC_NAME_KEY = "spark_manager.kafka.input_topic";
    private static final String ZK_HOST_KEY = "spark_manager.kafka.zk_host";

    static {
        try {
            properties.load(new FileInputStream(PROPERTIESFILE));
            String xmlConfig = new String(Files.readAllBytes(Paths.get((String)properties.get(XML_CONGIG_PATH_KEY))));
            configuration = ConfigurationParser.parse(xmlConfig);
        } catch (Exception e) {
            new Exception(e);
        }
    }

    private static JavaSparkContext sc;
    private static JavaStreamingContext ssc;
    static  {
        SparkConf conf = new SparkConf()
                .setAppName("FraudProject")
                .setMaster("local[*]");
        sc = new JavaSparkContext(conf);
        sc.setLocalProperty("spark.ui.enabled", "true");
        sc.setLocalProperty("spark.ui.port", "4040");
        ssc = new JavaStreamingContext(sc, Durations.seconds(10));
    }

    public void run() throws Exception {
        logger.info("Start\n");
        Map<String, Integer> topicMap = new HashMap<>();
        //topic and number threads
        topicMap.put((String)properties.get(TOPIC_NAME_KEY), 1);
        JavaPairReceiverInputDStream<String, String> kafkaStream =
                KafkaUtils.createStream(ssc, (String)properties.get(ZK_HOST_KEY), UUID.randomUUID().toString(), topicMap);
        //get massage
        JavaDStream<String> xmls = kafkaStream.map((Tuple2<String, String> tuple2) -> tuple2._2());

        xmls.foreachRDD(rdd -> rdd.foreach(string ->
                logger.info("\nInput xml:\n" + string + "\n")));

        JavaPairDStream<String, List<InputGroup>> groupList1 = xmls.mapToPair(xml ->
                new Tuple2<>(xml, InputCalculator.calculate(configuration, xml)));
        JavaPairDStream<Integer, List<InputGroup>> groupList = groupList1.mapToPair(tuple2 ->
                new Tuple2<>(tuple2._1.hashCode(), tuple2._2));
        JavaPairDStream<Integer, String> hashTable = groupList1.mapToPair(tuple2 ->
                new Tuple2<>(tuple2._1.hashCode(), tuple2._1));

        //AbstractData database = new MockData();
        AbstractData database = new HDFSData("Medicine");
        //get all fields for all groups
        List<InputGroup> data = database.getValues(configuration.getUniqueFields());

        logger.info("\nData:\n");
        for(InputGroup inputGroup : data)
            logger.info(inputGroup.toString() + "\n");

        //added data fo group
        JavaPairDStream<Integer, Tuple2<InputGroup, List<List<InputType>>>> setStream =
                groupList.flatMapValues(grList -> {
                    List<Tuple2<InputGroup, List<List<InputType>>>> list = new ArrayList<>();
                    for(InputGroup inputGroup : grList) {
                        /*
                        //get data from database
                        List<Field> fieldsName = inputGroup.getValues().stream()
                                .map(inputType -> {
                                    Field field =  new Field();
                                    field.setXpathName(inputType.getFieldName());
                                    field.setType(null);
                                    return field;
                                })
                                .collect(Collectors.toList());
                        List<List<InputType>> values = database.getValues(fieldsName).stream()
                                .map(inputGroup1 -> inputGroup.getValues())
                                .collect(Collectors.toList());
                        list.add(new Tuple2<>(tuple2._1, new Tuple2<>(inputGroup, values)));
                        */
                        //filtred data from field names group
                        List<String> fieldsName = inputGroup.getValues().stream()
                                .map(inputType -> inputType.getFieldName()).collect(Collectors.toList());
                        List<List<InputType>> values = data.stream()
                                .map(inputGroup1 -> inputGroup.getValues())
                                .map(list1 -> list1.stream()
                                        .filter(inputType -> fieldsName.contains(inputType.getFieldName()))
                                        .collect(Collectors.toList()))
                                .collect(Collectors.toList());
                        list.add(new Tuple2<>(inputGroup, values));
                    }
                    return list;
                });

        setStream.foreachRDD(rdd -> rdd.foreach(tuple -> {
                logger.info("\ngroup and value" +
                        "\nHash: " + tuple._1 +
                        "\ngroup: " + tuple._2._1 +
                        "\nvalues:\n");
                        for(List list : tuple._2._2)
                            for(Object obj : list)
                                logger.info(obj + "\n");
                }));

        DetectorsFactory detectorsFactory = new DetectorsFactory();

        List<Tuple2<Integer, Tuple2<InputGroup, List<List<InputType>>>>> values = new ArrayList<>();
        Map<Integer, Tuple2<InputGroup, Boolean>> results = new HashMap<>();
        setStream.foreachRDD(rdd -> values.addAll(rdd.collect()));
        for(Tuple2<Integer, Tuple2<InputGroup, List<List<InputType>>>> tuple2 : values) {
            Integer hash = tuple2._1;
            InputGroup value = tuple2._2._1;
            List<List<InputType>> valueData = tuple2._2._2;
            results.put(hash, new Tuple2<>(value, detectorsFactory.get(sc, value.getMethod()).detect(value.getValues(), valueData)));
        }

        for(Integer key : results.keySet()) {
            Tuple2 tuple = results.get(key);
            logger.info("\ngroup and value" +
                    "\nHash: " + key +
                    "\ngroup: " + tuple._1 +
                    "\nresult: " + tuple._2 + "\n");
        }



        /*
        //processing group using detector
        JavaPairDStream<Integer, Tuple2<InputGroup, Boolean>> results = setStream.mapValues(tuple2 ->
                new Tuple2<>(tuple2._1, detectorsFactory.get(sc, tuple2._1.getMethod())
                        .detect(tuple2._1.getValues(), tuple2._2)));

        results.foreachRDD(rdd -> rdd.foreach(tuple ->
                logger.info("\ngroup and result" +
                            "\nHash: " + tuple._1 +
                            "\ngroup: " + tuple._2._1 +
                            "\nresult: " + tuple._2._2)));

        //group result fo each input xml
        JavaPairDStream<Integer, List<Tuple2<InputGroup, Boolean>>> resultByKey = results
                .groupByKey()
                .mapValues(tuplList -> {
                    List<Tuple2<InputGroup, Boolean>> list = new ArrayList<>();
                    tuplList.forEach(list::add);
                    return list;
                });

        //result fo each xml
        JavaPairDStream<Integer, Boolean> verdictsHash = resultByKey.mapValues(tupleList -> {
            Boolean result = true;
            for(Tuple2 tupl : tupleList) {
                result = result.equals(tupl._2);
            }
            return result;
        });

        verdictsHash.foreachRDD(rdd -> rdd.foreach(tuple ->
                logger.info("\nVerdict:\n" +
                        "\nhash: " + tuple._1 +
                        "\nresult: " + tuple._2)));

        JavaPairDStream<String, Boolean> verdictsXml =
                verdictsHash.cogroup(hashTable).flatMapToPair(tuple2 -> {
                    List<Tuple2<String, Boolean>> list = new ArrayList<>();
                    for(String str : tuple2._2._2)
                        for(Boolean bln : tuple2._2._1)
                            list.add(new Tuple2<>(str, bln));
                    return list.listIterator();
                });

        verdictsXml.foreachRDD(rdd -> rdd.foreach(tuple ->
                logger.info("\nVerdict:\n" +
                            "\nxml: " + tuple._1 +
                            "\nresult: " + tuple._2)));

        */

        ssc.start();
        try {
            ssc.awaitTermination();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.toString());
        } finally {
            logger.info("Finish");
        }
    }
}

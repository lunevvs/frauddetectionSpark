package ru.spbstu.frauddetection.core.generator;

import com.google.common.io.Resources;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KafkaSender implements Sender {
    private final String topic = "xml_data";

    private Producer<String, String> producer;

    public KafkaSender() throws IOException {
        try (InputStream props = Resources.getResource("sender.properties").openStream()) {
            Properties kafkaProperties = new Properties();
            kafkaProperties.load(props);
            producer = new KafkaProducer<>(kafkaProperties);
        }
    }

    @Override
    public void send(String data) {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, data);
        producer.send(record);

        System.out.println("KafkaProducer sent:");
        System.out.println(data);
    }

}


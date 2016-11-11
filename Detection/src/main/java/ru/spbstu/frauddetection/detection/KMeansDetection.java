package ru.spbstu.frauddetection.detection;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import ru.spbstu.frauddetection.InputDataCalculator.InputType;

import java.util.Collections;
import java.util.List;

public class KMeansDetection extends DetectionBaseSpark<Double> {

    @Override
    public Boolean detect(List<InputType<Double>> data, List<List<InputType<Double>>> value) {
        List<List<InputType<Double>>> dat = value;
        dat.add(data);
        JavaRDD<List<InputType<Double>>> typeRDD = sc.parallelize(dat);
        JavaRDD<Vector> VectorRDD = typeRDD.map(list -> {
            double[] values = new double[list.size()];
            for (int i = 0; i < values.length; ++i)
                values[i] = list.get(i).getT().doubleValue();
            return Vectors.dense(values);
        });
        VectorRDD.cache();

        // Cluster the data into two classes using KMeans
        int numClusters = 2;
        int numIterations = 20;
        KMeansModel clusters = KMeans.train(VectorRDD.rdd(), numClusters, numIterations);

        // Evaluate clustering by computing Within Set Sum of Squared Errors
        double WSSSE = clusters.computeCost(VectorRDD.rdd());

        for(Vector vec : VectorRDD.collect()){
            System.out.println(vec.toJson() + " Cluster #" + clusters.predict(vec));
        }

        List<Integer> clusterNums = clusters.predict(VectorRDD).collect();


        return Collections.frequency(clusterNums, clusterNums.get(clusterNums.size()-1)) > 1;
    }
}


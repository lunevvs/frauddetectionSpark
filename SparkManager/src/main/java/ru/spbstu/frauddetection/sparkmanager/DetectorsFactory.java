package ru.spbstu.frauddetection.sparkmanager;

import org.apache.spark.api.java.JavaSparkContext;
import ru.spbstu.frauddetection.FraudConfig.ObjectModel.Method;
import ru.spbstu.frauddetection.detection.DetectionBaseSpark;
import ru.spbstu.frauddetection.detection.KMeansDetection;
import ru.spbstu.frauddetection.detection.SentenceDetection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DetectorsFactory implements Serializable {
    private final Map<Method, DetectionBaseSpark> detectors = new HashMap<>();

    public DetectorsFactory() {
        detectors.put(Method.KMeans, new KMeansDetection());
        detectors.put(Method.Sentence, new SentenceDetection());
    }

    public DetectionBaseSpark get(JavaSparkContext sc, Method method) {
        DetectionBaseSpark detector = detectors.get(method);
        detector.setSc(sc);
        return detector;
    }
}

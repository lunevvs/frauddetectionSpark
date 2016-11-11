package ru.spbstu.frauddetection.detection;

import org.apache.spark.api.java.JavaSparkContext;

public abstract class DetectionBaseSpark<T> extends DetectionBase<T>{
    protected JavaSparkContext sc = null;
    public void setSc(JavaSparkContext sc) {
        this.sc = sc;
    }
}

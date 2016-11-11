package ru.spbstu.frauddetection.detection;

import ru.spbstu.frauddetection.InputDataCalculator.InputType;

import java.io.Serializable;
import java.util.List;

public abstract class DetectionBase<T> implements Serializable{
    public abstract Boolean detect(List<InputType<T>> value, List<List<InputType<T>>> data);
}

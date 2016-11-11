package ru.spbstu.frauddetection.detection;

import ru.spbstu.frauddetection.InputDataCalculator.InputType;

import java.util.List;

public class SentenceDetection extends DetectionBaseSpark<String> {
    @Override
    public Boolean detect(List<InputType<String>> value, List<List<InputType<String>>> data) {
        return true;
    }
}

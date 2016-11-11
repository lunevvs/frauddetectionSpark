package ru.spbstu.frauddetection.FraudConfig.ObjectModel;

import java.io.Serializable;

public enum Type implements Serializable {
    Integer,
    String,
    Double,
    Boolean,
    Text,
    Enum,
    Date
}

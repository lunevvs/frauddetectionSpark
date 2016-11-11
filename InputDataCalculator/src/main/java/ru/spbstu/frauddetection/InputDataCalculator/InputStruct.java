package ru.spbstu.frauddetection.InputDataCalculator;

import java.io.Serializable;
import java.util.LinkedList;

public class InputStruct implements Serializable {
    private LinkedList<InputGroup> struct;

    public InputStruct() {
        struct = new LinkedList<InputGroup>();
    }

    @Override
    public String toString() {
        return "InputStruct{" +
                "struct=" + struct +
                '}';
    }

    public LinkedList<InputGroup> getStruct() {
        return struct;
    }

    public void setStruct(LinkedList<InputGroup> struct) {
        this.struct = struct;
    }
}

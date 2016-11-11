package ru.spbstu.frauddetection.datastorage;

import ru.spbstu.frauddetection.FraudConfig.ObjectModel.Field;
import ru.spbstu.frauddetection.InputDataCalculator.InputGroup;

import java.util.List;

public class HDFSData extends AbstractData {
    private HDFSManager hdfsManager = new HDFSManager();
    private String nameTable;

    public HDFSData(String nameTable) {
        super();
        this.nameTable = nameTable;
    }

    public List<InputGroup> getValues(List<Field> list) {
        try {
            return hdfsManager.getData(nameTable, list);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void addValue(String strXml) {
        try {
            hdfsManager.putData(nameTable, strXml);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String toString() {
        return "HDFSData. table:" + nameTable;
    }
}

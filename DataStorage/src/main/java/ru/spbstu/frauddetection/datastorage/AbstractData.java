package ru.spbstu.frauddetection.datastorage;

import ru.spbstu.frauddetection.FraudConfig.ObjectModel.Field;
import ru.spbstu.frauddetection.InputDataCalculator.InputGroup;
import ru.spbstu.frauddetection.InputDataCalculator.InputType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractData implements Serializable {
    //abstract public List<InputGroup> getData();
    abstract public List<InputGroup> getValues(List<Field> list);
    abstract public void addValue(String xmlInput);

    public static List<InputGroup> getNormalList(InputGroup groupInput, List<InputGroup> db) {
        List<InputGroup> list = new ArrayList<InputGroup>();

        for (InputGroup valueGroup : db) {
            List<InputType> tmp = new ArrayList<InputType>();
            for (InputType val : groupInput.getValues()) {
                for (InputType valDB : valueGroup.getValues())
                    if (valDB.getFieldName().equals(val.getFieldName()))
                        tmp.add(valDB);
            }
            if (!tmp.isEmpty()) {
                InputGroup tmpGroup = new InputGroup();
                tmpGroup.setValues(tmp);
                list.add(tmpGroup);
            }
        }
        return list;
    }

    abstract public String toString();
}

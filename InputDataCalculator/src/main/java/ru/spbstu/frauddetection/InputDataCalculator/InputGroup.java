package ru.spbstu.frauddetection.InputDataCalculator;

import ru.spbstu.frauddetection.FraudConfig.ObjectModel.Method;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InputGroup implements Serializable {

    private Method method;
    private List<InputType> values;
    private InputGroup group;

    public InputGroup()
    {
        values = new ArrayList<InputType>();
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public List<InputType> getValues() {
        return values;
    }

    public void setValues(List<InputType> values) {
        this.values = values;
    }

    public InputGroup getGroup() {
        return group;
    }

    public void setGroup(InputGroup group) {
        this.group = group;
    }

    public void setValues(InputGroup values){
        values.setValues(values.getValues());
    }

    @Override
    public String toString() {
        return "InputGroup{Method:" + method + ", values=" + values + ", group=" + group + "}";
    }
}

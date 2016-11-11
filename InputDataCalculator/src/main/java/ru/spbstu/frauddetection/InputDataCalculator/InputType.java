package ru.spbstu.frauddetection.InputDataCalculator;
import ru.spbstu.frauddetection.FraudConfig.ObjectModel.Type;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class InputType<T> implements Serializable {
    private T t;
    private String fieldName;

    // whats is it?
    enum tmp_enum {
        a1, b1, a2
    }

    private static Map<Type, BiFunction<String, String, InputType>> InputTypeFactory = new HashMap<>();
    static {
        InputTypeFactory.put(Type.Integer, (val, name) -> new InputType<>(Integer.parseInt(val), name));
        InputTypeFactory.put(Type.String,  (val, name) -> new InputType<>(val, name));
        InputTypeFactory.put(Type.Boolean, (val, name) -> new InputType<>(Boolean.parseBoolean(val), name));
        InputTypeFactory.put(Type.Double,  (val, name) -> new InputType<>(Double.parseDouble(val), name));
        InputTypeFactory.put(Type.Text,    (val, name) -> new InputType<>(val, name));
        InputTypeFactory.put(Type.Enum,    (val, name) -> new InputType<>(tmp_enum.valueOf(val), val));
        InputTypeFactory.put(Type.Date,    (val, name) -> {
            InputType res = null;
            try {
                res = new InputType<>(new SimpleDateFormat("dd.MM.yy HH:mm").parse(val), name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        });
    }

    public InputType(){};
    public InputType(T t, String fieldName) {
        this.t = t;
        this.fieldName = fieldName;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public static InputType adapter(Type t, String val, String fieldName) {
        return InputTypeFactory.get(t).apply(val, fieldName);
    }

    @Override
    public String toString() {
        return "InputType{t=" + t + ", fieldName='" + fieldName + '\'' + '}';
    }
}

package ru.spbstu.frauddetection.FraudConfig.ObjectModel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.List;


public class Group implements Serializable{
    private List <Field> fields;
    private Method method;
    private Group group;

    public Group getGroup() {
        return group;
    }
    @XmlElement(name = "group")
    public void setGroup(Group group) {
        this.group = group;
    }

    public Method getMethod() {
        return method;
    }

    @XmlAttribute(name = "method")
    public void setMethod(Method method) {
        this.method = method;
    }
    
    public List<Field> getFields() {
        return fields;
    }
    @XmlElement(name = "field")
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "Group{" +
                "fields=" + fields +
                ", method=" + method +
                ", group=" + group +
                '}';
    }
}

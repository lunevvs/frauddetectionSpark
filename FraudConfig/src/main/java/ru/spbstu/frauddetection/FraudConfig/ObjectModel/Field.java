package ru.spbstu.frauddetection.FraudConfig.ObjectModel;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;


public class Field implements Serializable {

    private String xpathName;
    private Type type;

    @XmlElement(name = "xpath_name")
    public void setXpathName(String xpathName) {
        this.xpathName = xpathName;
    }
    public String getXpathName() {
        return this.xpathName;
    }

    @XmlElement(name = "type")
    public void setType(Type type) {
        this.type = type;
    }
    public Type getType() {
        return this.type;
    }

    public String toString() {
        return type + " " + xpathName;
    }

}

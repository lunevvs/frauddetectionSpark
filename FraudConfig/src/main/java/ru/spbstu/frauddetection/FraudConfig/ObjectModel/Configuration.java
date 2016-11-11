package ru.spbstu.frauddetection.FraudConfig.ObjectModel;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "fraudconfig")
public class Configuration implements Serializable {
    private List<Group> groups;

    public List<Group> getGroups() {
        return groups;
    }
    @XmlElement(name = "group")
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "Configuration{groups=" + groups + "}";
    }

    public List<Field> getUniqueFields() {
        List<Field> fields = new ArrayList<Field>();
        for (Group group : getGroups()) {
            for (Field field : group.getFields()) {
                if (!fields.contains(field)) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }
}

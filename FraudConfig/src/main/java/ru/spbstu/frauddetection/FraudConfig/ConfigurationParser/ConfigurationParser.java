package ru.spbstu.frauddetection.FraudConfig.ConfigurationParser;

import ru.spbstu.frauddetection.FraudConfig.ObjectModel.Configuration;
import org.xml.sax.SAXException;
import javax.xml.bind.*;
import java.io.Serializable;
import java.io.StringReader;


public class ConfigurationParser implements Serializable{

    public static Configuration parse(String configFile) throws JAXBException, SAXException {
        JAXBContext context = JAXBContext.newInstance(Configuration.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(configFile);
        Configuration config = (Configuration) unmarshaller.unmarshal(reader);
        return config;
    }
}

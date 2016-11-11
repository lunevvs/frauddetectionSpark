package ru.spbstu.frauddetection.core.generator;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Map;

public class DataAdapter {
    Parser parser;
    XMLBuilder xmlBuilder;
    Sender sender;

    public DataAdapter(Parser parser, XMLBuilder xmlBuilder,
                       Sender sender) {

        this.parser = parser;
        this.xmlBuilder = xmlBuilder;
        this.sender = sender;
    }

    public void process() throws IOException, ParserConfigurationException {
        Map<String, String> nextItem;
        while ((nextItem = parser.getNext()) != null) {
            String xmlItem = xmlBuilder.build(nextItem);
            sender.send(xmlItem);
        }
    }
}

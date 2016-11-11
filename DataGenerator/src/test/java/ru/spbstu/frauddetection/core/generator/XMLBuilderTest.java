package ru.spbstu.frauddetection.core.generator;

import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by kokoster on 13.07.16.
 */
public class XMLBuilderTest {
    @Test
    public void testBuild() throws ParserConfigurationException {
        XMLBuilder xmlBuilder = new XMLBuilder();

        Map<String, String> inputItem = new LinkedHashMap<>();
        inputItem.put("ID", "1");
        inputItem.put("Abbreviation", "aaa");

        String builtXML = xmlBuilder.build(inputItem);

        String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                             "<Item>\n\t<ID>1</ID>\n\t<Abbreviation>aaa</Abbreviation>\n</Item>\n";

        assertEquals(builtXML, expectedXML.replaceAll("\t", "    "));
    }

    @Test
    public void testEmptyAttributes() throws ParserConfigurationException {
        XMLBuilder xmlBuilder = new XMLBuilder();

        Map<String, String> inputItem = new LinkedHashMap<>();
        inputItem.put("ID", "1");
        inputItem.put("Abbreviation", "");

        String builtXML = xmlBuilder.build(inputItem);

        String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<Item>\n\t<ID>1</ID>\n\t<Abbreviation/>\n</Item>\n";

        assertEquals(builtXML, expectedXML.replaceAll("\t", "    "));
    }

    @Test(expected=NullPointerException.class)
    public void testNullMap() throws ParserConfigurationException {
        XMLBuilder xmlBuilder = new XMLBuilder();

        String builtXML = xmlBuilder.build(null);
    }
}
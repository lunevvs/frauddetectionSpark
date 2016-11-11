package ru.spbstu.frauddetection.core.generator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Map;

public class XMLBuilder {
    private DocumentBuilder domBuilder;

    public XMLBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domBuilder = domFactory.newDocumentBuilder();
    }

    public String build(Map<String, String> item) throws ParserConfigurationException {
        Document xmlDocument = convertToXML(item);
        String xmlItem = convertToString(xmlDocument);

        return xmlItem;
    }

    private Document convertToXML(Map<String, String> item) throws ParserConfigurationException {
        Document xmlDocument = domBuilder.newDocument();
        Element rootElement = xmlDocument.createElement("Item");
        xmlDocument.appendChild(rootElement);

        for (String header : item.keySet()) {
            Element itemDescriptionElement = xmlDocument.createElement(header);
            itemDescriptionElement.appendChild(xmlDocument.createTextNode(item.get(header)));
            rootElement.appendChild(itemDescriptionElement);
        }

        return xmlDocument;
    }


    private String convertToString(org.w3c.dom.Document doc) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }
}

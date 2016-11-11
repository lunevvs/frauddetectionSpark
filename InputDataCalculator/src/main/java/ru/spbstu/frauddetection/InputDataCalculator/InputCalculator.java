package ru.spbstu.frauddetection.InputDataCalculator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.spbstu.frauddetection.FraudConfig.ObjectModel.*;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputCalculator implements Serializable {
    private static final Logger logger = Logger.getLogger(String.valueOf(InputCalculator.class));

    public static List<InputGroup> calculate(Configuration configuration, String inputXml) {
        List<InputGroup> inputStruct = new ArrayList<>();

        for (Group groupConfig : configuration.getGroups()) {
            InputGroup resultGroup = new InputGroup();
            try {
                roundGroup(groupConfig, inputXml, resultGroup);
                resultGroup.setMethod(groupConfig.getMethod());
            } catch (XPathExpressionException ex) {
                logger.log(Level.ALL, "Excpetion of parsing: " + ex);
            }
            inputStruct.add(resultGroup);
        }
        return inputStruct;
    }

    public static void roundGroup(Group val, String inputXml, InputGroup result) throws XPathExpressionException {
        result.setValues(calculateValues(val, inputXml).getValues());

        if (val.getGroup() != null) {
            result.setGroup(new InputGroup());
            val = val.getGroup();
            roundGroup(val, inputXml, result.getGroup());
        }
    }

    public static InputGroup calculateValues(Group val, String inputXml) throws XPathExpressionException {
        String tempStr;
        InputGroup inputGroup = new InputGroup();
        for (Field f : val.getFields()) {
            InputStream is = new ByteArrayInputStream(inputXml.getBytes());
            XPath xPath = XPathFactory.newInstance().newXPath();
            org.xml.sax.InputSource inputSource = new org.xml.sax.InputSource(is);
            String path = "/Item/" + f.getXpathName();
            NodeList nodes = (NodeList) xPath.evaluate(path, inputSource, XPathConstants.NODESET);
            if (nodes.getLength() != 0) {
                Node node = nodes.item(0);
                tempStr = node.getFirstChild().getNodeValue();
                if (!tempStr.equals("")) {
                    Type t = f.getType();
                    inputGroup.getValues().add(InputType.adapter(t, tempStr, f.getXpathName()));
                }
            }
        }
        return inputGroup;
    }
}


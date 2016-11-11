package ru.spbstu.frauddetection.datastorage;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.spbstu.frauddetection.FraudConfig.ObjectModel.Field;
import ru.spbstu.frauddetection.InputDataCalculator.InputGroup;
import ru.spbstu.frauddetection.InputDataCalculator.InputType;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HDFSManager implements Serializable {
    private String xPathToRecord = "/records/record";
    private String dataBaseFileName = "base.xml";
    private String dataBaseFileNameOld = "base.xml.old";
    private String SLASH = "/";
    private Configuration configuration = (new HadoopConfiguration()).getInstance().getConfiguration();
    private FileSystem hdfsFileSystem;
    private Path hdfsPath = new Path(configuration.get("fs.defaultFS") + "/");
    private Path dataDasesPath = new Path(hdfsPath + "/DataBase/");

    public HDFSManager() {
        try {
            hdfsFileSystem = FileSystem.get(hdfsPath.toUri(), configuration);
        } catch (IOException e) {
            System.out.println("hadoop filesystem not found");
            e.printStackTrace();
        }
    }

    public List<InputGroup> getData(String nameBase, List<Field> listField) throws Exception {
        List<InputGroup> gettedData = new ArrayList<>();
        NodeList records = null;

        XPath xPath = XPathFactory.newInstance().newXPath();
        Path dataBasePath = new Path(dataDasesPath + SLASH + nameBase + SLASH + dataBaseFileName);
        Document document = DocumentBuilderFactory.newInstance().
                newDocumentBuilder().parse(hdfsFileSystem.open(dataBasePath));
        records = (NodeList)xPath.evaluate(xPathToRecord,
                document.getDocumentElement(), XPathConstants.NODESET);

        for (int j = 0; j < records.getLength(); ++j) {
            List<InputType> valueGroup = new ArrayList<>();
            try {
                for (Field field : listField) {
                    NodeList nodeList = (NodeList)xPath.evaluate(
                            field.getXpathName(), records.item(j),
                            XPathConstants.NODESET);
                    for (int i = 0; i < nodeList.getLength(); ++i) {
                        String value = nodeList.item(i).getTextContent();
                        valueGroup.add(InputType.adapter(field.getType(),
                                value, field.getXpathName()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            InputGroup inputGroup = new InputGroup();
            inputGroup.setValues(valueGroup);
            gettedData.add(inputGroup);
        }
        return gettedData;
    }

    public void putData(String nameBase, String xmlData) throws Exception {
        Path dataBasePath = new Path(dataDasesPath + SLASH + nameBase + SLASH + dataBaseFileName);
        Path dataBasePathOld = new Path(dataDasesPath + SLASH + nameBase + SLASH + dataBaseFileNameOld);
        Document record = DocumentBuilderFactory.newInstance().
                newDocumentBuilder().parse(new ByteArrayInputStream(xmlData.getBytes()));
        Node recNode = record.getFirstChild().cloneNode(true);
        Document document = DocumentBuilderFactory.newInstance().
                newDocumentBuilder().parse(hdfsFileSystem.open(dataBasePath));
        document.adoptNode(recNode);
        document.getDocumentElement().appendChild(recNode);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        hdfsFileSystem.rename(dataBasePath, dataBasePathOld);
        OutputStream os = hdfsFileSystem.create(dataBasePath);
        transformer.transform(new DOMSource(document), new StreamResult(os));
        hdfsFileSystem.delete(dataBasePathOld, false);
    }
}
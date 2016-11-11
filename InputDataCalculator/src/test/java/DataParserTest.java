import junit.framework.TestCase;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.spbstu.frauddetection.FraudConfig.ConfigurationParser.ConfigurationParser;
import ru.spbstu.frauddetection.FraudConfig.ObjectModel.*;
import ru.spbstu.frauddetection.InputDataCalculator.*;
public class DataParserTest extends TestCase {
    private Configuration config;
    private String file;
    private String xml;
    private ConfigurationParser parser;


    @Override
    public void setUp() throws Exception {

        parser = new ConfigurationParser();
        initFile();
        config = parser.parse(file);


        xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<Item>\n" +
                "    <medicine>teraflu</medicine>\n" +
                "    <desease>flu</desease>\n" +
                "    <dosage>160</dosage>\n" +
                "    <times>3</times>\n" +
                "</Item>";
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testDataParser() throws Exception {

        List <InputGroup> inputStruct = new ArrayList<InputGroup>();
        inputStruct = InputCalculator.calculate(config, xml);
        assertEquals(inputStruct.get(0).getValues().get(0).getT(), "flu");
        assertEquals(inputStruct.get(0).getGroup().getGroup().getValues().get(0).getT(), 160);
        //System.out.println(inputStruct);
    }

    public void initFile() {
        file = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<fraudconfig>\n" +
                "    <group method=\"Sentence\">\n" +
                "        <field>\n" +
                "            <xpath_name>desease</xpath_name>\n" +
                "            <type>String</type>\n" +
                "        </field>\n" +
                "        <group method=\"Sentence\">\n" +
                "            <field>\n" +
                "                <xpath_name>medicine</xpath_name>\n" +
                "                <type>String</type>\n" +
                "            </field>\n" +
                "            <group method=\"KMeans\">\n" +
                "                <field>\n" +
                "                    <xpath_name>dosage</xpath_name>\n" +
                "                    <type>Integer</type>\n" +
                "                </field>\n" +
                "                <field>\n" +
                "                    <xpath_name>times</xpath_name>\n" +
                "                    <type>Integer</type>\n" +
                "                </field>\n" +
                "            </group>\n" +
                "        </group>\n" +
                "    </group>\n" +
                "</fraudconfig>";
    }
}

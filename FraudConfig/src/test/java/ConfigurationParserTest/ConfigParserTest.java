package ConfigurationParserTest;

import junit.framework.TestCase;
import org.junit.Test;
import ru.spbstu.frauddetection.FraudConfig.ObjectModel.Configuration;
import ru.spbstu.frauddetection.FraudConfig.ObjectModel.Method;
import ru.spbstu.frauddetection.FraudConfig.ConfigurationParser.ConfigurationParser;

public class ConfigParserTest extends TestCase {
    private Configuration config;
    private String file;
    private ConfigurationParser parser;

    @Override
    public void setUp() throws Exception {
        parser = new ConfigurationParser();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testParse() throws Exception {
        initFile();
        config = parser.parse(file);
        System.out.println(config);
    }

    public void initFile() {
        file = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<fraudconfig>\n" +
                "    <group method=\"KMeans\">\n" +
                "        <field>\n" +
                "            <xpath_name>Transaction</xpath_name>\n" +
                "            <type>Float</type>\n" +
                "        </field>\n" +
                "        <field>\n" +
                "            <xpath_name>Date</xpath_name>\n" +
                "            <type>Date</type>\n" +
                "        </field>\n" +
                "        <group method=\"KMeans\">\n" +
                "            <field>\n" +
                "                <xpath_name>Transaction</xpath_name>\n" +
                "                <type>Float</type>\n" +
                "            </field>\n" +
                "            <group method=\"KMeans\">\n" +
                "                <field>\n" +
                "                    <xpath_name>T</xpath_name>\n" +
                "                    <type>Float</type>\n" +
                "                </field>\n" +
                "            </group>\n" +
                "        </group>\n" +
                "    </group>\n" +
                "</fraudconfig>\n";
    }
}

package ru.spbstu.frauddetection.core.generator;

import com.opencsv.CSVReader;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestHabr {
     @Test
    public void testHabrCSVParsing() throws IOException, ParserConfigurationException {
        InputStream is = getClass().getResourceAsStream("/BigHabrData.csv");

        CSVReader csvReader = new CSVReader(new InputStreamReader(is), ',', '"', '\0');
        CSVParser csvParser = new CSVParser(csvReader);
        XMLBuilder xmlBuilder = new XMLBuilder();

        DataAdapter adapter = new DataAdapter(csvParser, xmlBuilder, new MockSender());
        adapter.process();
    }

    private class MockSender implements Sender {
        @Override
        public void send(String data) throws IOException {
            System.out.println(data);
        }
    }
}

package ru.spbstu.frauddetection.core.generator;

import com.opencsv.CSVReader;
import org.junit.Test;
import static org.mockito.Mockito.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataAdapterIntegrationTest {
    @Test
    public void testProcessOneItem() throws IOException, ParserConfigurationException {
        CSVReader readerMock = mock(CSVReader.class);

        when(readerMock.readNext())
                .thenReturn(new String[]{"ID", "Abbreviation"})
                .thenReturn(new String[]{"1", "aaa"})
                .thenReturn(null);

        HTTPSender senderMock = mock(HTTPSender.class);

        DataAdapter dataAdapter = new DataAdapter(new CSVParser(readerMock),
                                                     new XMLBuilder(), senderMock);

        dataAdapter.process();

        String xmlString =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<Item>\n" +
                        "\t<ID>1</ID>\n" +
                        "\t<Abbreviation>aaa</Abbreviation>\n" +
                "</Item>\n";
        verify(senderMock).send(xmlString.replaceAll("\t", "    "));
    }

    @Test
    public void testProcess() throws IOException, ParserConfigurationException {
        CSVReader readerMock = mock(CSVReader.class);

        when(readerMock.readNext())
                .thenReturn(new String[]{"ID", "Abbreviation"})
                .thenReturn(new String[]{"1", "aaa"})
                .thenReturn(new String[]{"4", ""})
                .thenReturn(null);

        HTTPSender senderMock = mock(HTTPSender.class);

        DataAdapter dataAdapter = new DataAdapter(new CSVParser(readerMock),
                new XMLBuilder(), senderMock);

        dataAdapter.process();

        String xmlString =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<Item>\n" +
                        "\t<ID>1</ID>\n" +
                        "\t<Abbreviation>aaa</Abbreviation>\n" +
                "</Item>\n";

        String xmlString2 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<Item>\n" +
                        "\t<ID>4</ID>\n" +
                        "\t<Abbreviation/>\n" +
                "</Item>\n";

        verify(senderMock).send(xmlString.replaceAll("\t", "    "));
        verify(senderMock).send(xmlString2.replaceAll("\t", "    "));
    }

    @Test
    public void testProcessFromFile() throws IOException, ParserConfigurationException {
        InputStream is = getClass().getResourceAsStream("/TestFile.csv");
        CSVReader csvReader = new CSVReader(new InputStreamReader(is));

        HTTPSender senderMock = mock(HTTPSender.class);

        DataAdapter dataAdapter = new DataAdapter(new CSVParser(csvReader),
                new XMLBuilder(), senderMock);

        dataAdapter.process();

        String xmlString =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<Item>\n" +
                        "\t<ID>1</ID>\n" +
                        "\t<Abbreviation>aaa</Abbreviation>\n" +
                "</Item>\n";

        String xmlString2 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<Item>\n" +
                        "\t<ID>4</ID>\n" +
                        "\t<Abbreviation/>\n" +
                "</Item>\n";

        verify(senderMock).send(xmlString.replaceAll("\t", "    "));
        verify(senderMock).send(xmlString2.replaceAll("\t", "    "));
    }

    @Test
    public void testSend() throws ParserConfigurationException, IOException {
//        InputStream is = getClass().getResourceAsStream("/MedicineTest.csv");
        InputStream is = getClass().getResourceAsStream("/BigHabrData.csv");
        CSVReader csvReader = new CSVReader(new InputStreamReader(is));

        DataAdapter dataAdapter = new DataAdapter(new CSVParser(csvReader),
                new XMLBuilder(), new HTTPSender());

        dataAdapter.process();

    }

    @Test
    public void testSendWithKafka() throws ParserConfigurationException, IOException {
        InputStream is = getClass().getResourceAsStream("/TestFile.csv");
        CSVReader csvReader = new CSVReader(new InputStreamReader(is));

        DataAdapter dataAdapter = new DataAdapter(new CSVParser(csvReader),
                new XMLBuilder(), new KafkaSender());

        dataAdapter.process();

    }
}
package ru.spbstu.frauddetection.core.generator;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import static org.mockito.Mockito.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;

public class DataAdapterTest {
    @Test
    public void testProcessOneItem() throws IOException, ParserConfigurationException {
        Map<String, String> testDataMap = ImmutableMap.of("ID", "1", "Abbreviation", "aaa");

        CSVParser csvParserMock = mock(CSVParser.class);
        when(csvParserMock.getNext())
                .thenReturn(testDataMap)
                .thenReturn(null);

        String xmlString =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<Item>\n" +
                        "\t<ID>1</ID>\n" +
                        "\t<Abbreviation>aaa</Abbreviation>\n" +
                "</Item>\n";

        XMLBuilder xmlBuilderMock = mock(XMLBuilder.class);
        when(xmlBuilderMock.build(testDataMap))
                .thenReturn(xmlString);

        HTTPSender senderMock = mock(HTTPSender.class);

        DataAdapter dataAdapter = new DataAdapter(csvParserMock,
                xmlBuilderMock, senderMock);

        dataAdapter.process();

        verify(csvParserMock, times(2)).getNext();
        verify(xmlBuilderMock).build(testDataMap);
        verify(senderMock).send(xmlString);
    }

    @Test
    public void testNullDataMap() throws IOException, ParserConfigurationException {
        CSVParser csvParserMock = mock(CSVParser.class);
        when(csvParserMock.getNext())
                .thenReturn(null);

        XMLBuilder xmlBuilderMock = mock(XMLBuilder.class);
        HTTPSender senderMock = mock(HTTPSender.class);

        DataAdapter dataAdapter = new DataAdapter(csvParserMock,
                xmlBuilderMock, senderMock);

        dataAdapter.process();

        verify(senderMock, never()).send(any());
    }

    @Test
    public void testProcess() throws IOException, ParserConfigurationException {
        Map<String, String> testDataMap = ImmutableMap.of("ID", "1", "Abbreviation", "aaa");
        Map<String, String> testDataMap2 = ImmutableMap.of("ID", "4", "Abbreviation", "");

        CSVParser csvParserMock = mock(CSVParser.class);
        when(csvParserMock.getNext())
                .thenReturn(testDataMap)
                .thenReturn(testDataMap2)
                .thenReturn(null);

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

        XMLBuilder xmlBuilderMock = mock(XMLBuilder.class);
        when(xmlBuilderMock.build(testDataMap)).thenReturn(xmlString);
        when(xmlBuilderMock.build(testDataMap2)).thenReturn(xmlString2);

        HTTPSender senderMock = mock(HTTPSender.class);

        DataAdapter dataAdapter = new DataAdapter(csvParserMock,
                xmlBuilderMock, senderMock);

        dataAdapter.process();

        verify(senderMock).send(xmlString);
        verify(senderMock).send(xmlString2);
    }
}

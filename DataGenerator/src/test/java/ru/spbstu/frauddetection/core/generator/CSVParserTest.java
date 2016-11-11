package ru.spbstu.frauddetection.core.generator;

import com.opencsv.CSVReader;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by kokoster on 13.07.16.
 */
public class CSVParserTest {
    @Test
    public void testGetNext() throws Exception {
        CSVReader readerMock = Mockito.mock(CSVReader.class);

        Mockito.when(readerMock.readNext())
                .thenReturn(new String[]{"ID", "Abbreviation"})
                .thenReturn(new String[]{"1", "aaa"});

        CSVParser csvParser = new CSVParser(readerMock);
        Map<String, String> parsedMap = csvParser.getNext();

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("ID", "1");
        expectedMap.put("Abbreviation", "aaa");

        assertTrue(expectedMap.equals(parsedMap));
    }

    @Test
    public void testHeadersOnlyFile() throws Exception {
        CSVReader readerMock = Mockito.mock(CSVReader.class);

        Mockito.when(readerMock.readNext())
                .thenReturn(new String[]{"ID", "Abbreviation"})
                .thenReturn(null);

        CSVParser csvParser = new CSVParser(readerMock);
        Map<String, String> parsedMap = csvParser.getNext();

        assertEquals(parsedMap, null);
    }

    @Test(expected=NullPointerException.class)
    public void testNullReader() throws Exception {
        CSVParser csvParser = new CSVParser(null);
        Map<String, String> parsedMap = csvParser.getNext();
    }
}
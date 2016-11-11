package ru.spbstu.frauddetection.core.generator;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CSVParser implements Parser {
    private CSVReader csvReader;
    private String[] headers;

    private int counter = 0;

    public CSVParser(CSVReader csvReader) throws IOException {
        this.csvReader = csvReader;
        headers = csvReader.readNext();
    }

    public Map<String, String> getNext() throws IOException {
        String[] nextLine = csvReader.readNext();

        if (nextLine == null) {
            return null;
        }

        Map<String, String> item = new LinkedHashMap<>();

//        debug -> properties
        counter++;

        try {
            for (int i = 0; i < headers.length; ++i) {
                item.put(headers[i], nextLine[i]);
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("#" + counter);
            System.out.println("headers: " + headers.length);
            System.out.println("next line: " + nextLine.length);

            for (int i = 0; i < nextLine.length; ++i) {
                System.out.println(nextLine[i]);
            }

            throw e;
        }

        return item;
    }
}

package ru.spbstu.frauddetection.core.generator;

import com.opencsv.CSVReader;
import org.apache.kafka.common.utils.Utils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InfiniteCSVParser implements Parser {
    private CSVReader csvReader;
    private String[] headers;
    private List<String[]> csvObjects;
    private int iterator = 0;

    private int counter = 0;

    public InfiniteCSVParser(CSVReader csvReader) throws IOException {
        this.csvReader = csvReader;
        headers = csvReader.readNext();
        csvObjects = csvReader.readAll();
    }

    @Override
    public Map<String, String> getNext() throws IOException {
        if (iterator == csvObjects.size()) {
            iterator = 0;
        }

        String[] nextLine = csvObjects.get(iterator);
        Map<String, String> item = new LinkedHashMap<>();

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

        iterator++;
        Utils.sleep(1000);

        return item;
    }
}

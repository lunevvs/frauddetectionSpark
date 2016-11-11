package ru.spbstu.frauddetection.core.generator;

import com.opencsv.CSVReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Generator {
    public static void main(String[] args) throws IOException, ParserConfigurationException {
        InputStream is = Generator.class.getResourceAsStream("/MedicineUseCase.csv");
        CSVReader csvReader = new CSVReader(new InputStreamReader(is), ',', '"', '\0');

        DataAdapter dataAdapter = new DataAdapter(new InfiniteCSVParser(csvReader),
                new XMLBuilder(), new HTTPSender());

        dataAdapter.process();
    }
}

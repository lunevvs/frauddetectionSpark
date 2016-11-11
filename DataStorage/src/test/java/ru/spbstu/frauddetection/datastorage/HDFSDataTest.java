package ru.spbstu.frauddetection.datastorage;

import ru.spbstu.frauddetection.FraudConfig.ObjectModel.Field;
import ru.spbstu.frauddetection.FraudConfig.ObjectModel.Type;
import ru.spbstu.frauddetection.InputDataCalculator.InputGroup;
import ru.spbstu.frauddetection.InputDataCalculator.InputType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HDFSDataTest {
    private static String inFile = System.getProperty("user.dir") + "/src/main/resources/in/medicine.xml";
    private static String nameBase = "Medicine";

    @Test
    public void testWrite() {
        try {
            inFile = new Scanner(new File(inFile)).useDelimiter("\\Z").next();
            HDFSData dataBase = new HDFSData(nameBase);
            dataBase.addValue(inFile);
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
        assertTrue(true);
    }

    @Test
    public void testRead() {
        try {
            List<Field> arrayList = new ArrayList<>();
            arrayList.add(new Field());
            arrayList.get(0).setXpathName("first_name");
            arrayList.get(0).setType(Type.String);
            arrayList.add(new Field());
            arrayList.get(1).setXpathName("age");
            arrayList.get(1).setType(Type.Integer);

            AbstractData dataBase = new HDFSData(nameBase);
            List<InputGroup> valueGroups = dataBase.getValues(arrayList);
            int count = 0;
            for (InputGroup group : valueGroups) {
                System.out.println("\nrecord " + count++);
                for (InputType val : group.getValues()) {
                    System.out.println(val.getT());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
        assertTrue(true);
    }
}

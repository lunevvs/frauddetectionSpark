package ru.spbstu.frauddetection.detection;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class KMeansDetectionTest extends TestCase implements Serializable {

    static final Integer NUM_SAMPLES = 100000;

    @Test
    public void testTwo() throws Exception
    {
/*
        Scanner s = new Scanner(new File("src/main/resources/kmeans_test_data_2.txt"));
        ArrayList<String> list = new ArrayList<String>();
        while (s.hasNextLine()){
            list.add(s.nextLine());
        }
        s.close();

        KMeansDetection detector = new KMeansDetection();
        assertFalse(detector.detect(list, "1000 85"));
        assertTrue(detector.detect(list, "0 5"));
        assertFalse(detector.detect(list, "25 25"));
        assertTrue(detector.detect(list, "15 15"));
        detector.close();
    }
    @Test
    public void testThree() throws Exception
    {

        Scanner s = new Scanner(new File("src/main/resources/kmeans_test_data_3.txt"));
        ArrayList<String> list = new ArrayList<String>();
        while (s.hasNextLine()){
            list.add(s.nextLine());
        }
        s.close();

        KMeansDetection detector = new KMeansDetection();
        assertFalse(detector.detect(list, "1000 85 100"));
        assertTrue(detector.detect(list, "0 5 2"));
        detector.close();
*/
    }
}

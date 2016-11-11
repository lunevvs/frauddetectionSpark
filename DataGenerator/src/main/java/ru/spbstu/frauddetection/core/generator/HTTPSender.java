package ru.spbstu.frauddetection.core.generator;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPSender implements Sender {
    private final String urlStr = "http://localhost:8080/receive_input_xml";

    @Override
    public void send(String string) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // to make POST requests
        conn.setDoOutput(true);
//        disable encoding
        conn.setRequestProperty("Content-Type",
                "application/xml; charset=utf-8");

        try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8")) {
            writer.write(string);
            writer.flush();

            System.out.println("Response code " + conn.getResponseCode());
        }
    }
}

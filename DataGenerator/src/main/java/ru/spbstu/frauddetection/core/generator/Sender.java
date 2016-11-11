package ru.spbstu.frauddetection.core.generator;

import java.io.IOException;

/**
 * Created by kokoster on 15.07.16.
 */
public interface Sender {
    void send(String data) throws IOException;
}

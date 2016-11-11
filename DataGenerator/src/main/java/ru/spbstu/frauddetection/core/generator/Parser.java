package ru.spbstu.frauddetection.core.generator;

import java.io.IOException;
import java.util.Map;

public interface Parser {
    Map<String, String> getNext() throws IOException;
}

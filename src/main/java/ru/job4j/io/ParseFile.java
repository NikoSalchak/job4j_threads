package ru.job4j.io;

import java.io.*;
import java.util.function.Predicate;

public final class ParseFile {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public synchronized String getContentPredicate(Predicate<Integer> predicate) {
        StringBuilder output = new StringBuilder();
        try (InputStream input = new FileInputStream(file)) {
            int data;
            while ((data = input.read()) != -1) {
                if (predicate.test(data)) {
                    output.append((char) data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}

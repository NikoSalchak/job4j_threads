package ru.job4j.thread;

import java.util.HashMap;
import java.util.Map;

public class ArgsValidator {

    private final Map<String, String> args = new HashMap<>();

    private ArgsValidator() { }

    public String get(String key) {
        if (!args.containsKey(key)) {
            throw new IllegalArgumentException(String.format("This key: '%s' is missing", key));
        }
        return args.get(key);
    }

    private void parse(String[] args) {
        for (String s : args) {
            String[] substring = s.split("=", 2);
            validateSubstring(s, substring);
            this.args.put(substring[0].substring(1), substring[1]);
        }
    }

    private void validateSubstring(String s, String[] substring) {
        if (!s.startsWith("-")) {
            throw new IllegalArgumentException(
                    String.format("Error: This argument '%s' does not start with a '-' character", s)
            );
        }
        if (!s.contains("=")) {
            throw new IllegalArgumentException(
                    String.format("Error: This argument '%s' does not contain an equal sign", s)
            );
        }
        if (substring[0].substring(1).isBlank()) {
            throw new IllegalArgumentException(
                    String.format("Error: This argument '%s' does not contain a key", s)
            );
        }
        if (substring[1].isBlank()) {
            throw new IllegalArgumentException(
                    String.format("Error: This argument '%s' does not contain a value", s)
            );
        }
    }

    public static ArgsValidator of(String[] args) {
        ArgsValidator validator = new ArgsValidator();
        if (args.length == 0) {
            throw new IllegalArgumentException("Arguments not passed to program");
        }
        validator.parse(args);
        return validator;
    }
}

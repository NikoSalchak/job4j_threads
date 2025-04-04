package ru.job4j.thread;

import ru.job4j.thread.exceptions.NotCorrectValueException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class WgetValidator {

    private boolean urlValidator(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (URISyntaxException | MalformedURLException exception) {
            return false;
        }
    }

    public static ArgsValidator validateArgs(String[] args) {
        ArgsValidator argsValidator = ArgsValidator.of(args);
        if (!new WgetValidator().urlValidator(argsValidator.get("url"))) {
            throw new IllegalArgumentException(
                    String.format("Invalid URL: %s", argsValidator.get("url"))
            );
        }
        try {
            Integer.parseInt(argsValidator.get("speed"));
        } catch (NumberFormatException e) {
            throw new NotCorrectValueException(
                    String.format("The value '%s' is not a number ", argsValidator.get("speed"))
            );
        }
        return argsValidator;
    }
}

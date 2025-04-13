package ru.job4j.cash.exception;

public class AmountBelowZeroException extends RuntimeException {
    public AmountBelowZeroException(String message) {
        super(message);
    }
}

package ru.job4j.pool.mail;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void emailTo(User user) {
        executor.submit(() -> {
            String subject = String.format("Notification %s, to email %s", user.username(), user.email());
            String body = String.format("Add a new event to %s", user.username());
            send(subject, body, user.email());
        });
    }

    public void send(String subject, String body, String email) {
    }

    public void close() {
        executor.shutdown();
    }
}

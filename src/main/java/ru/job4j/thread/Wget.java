package ru.job4j.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;


import java.net.URISyntaxException;
import java.net.URL;

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        try (InputStream input = new URL(url).openStream();
             FileOutputStream output = new FileOutputStream(new File(new URI(url).getPath()).getName())) {
            byte[] buffer = new byte[1024];
            long sleepTime = 0;
            int bytesRead;
            int countRead = 0;
            long downloadAt = System.currentTimeMillis();
            while ((bytesRead = input.read(buffer, 0, buffer.length)) != -1) {
                countRead += bytesRead;
                output.write(buffer, 0, bytesRead);
                if (countRead >= speed) {
                    long elapsed = System.currentTimeMillis() - downloadAt;
                    System.out.println("скачено байтов: " + countRead + " время скачивания: " + elapsed);
                }
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ArgsValidator argsValidator = WgetValidator.validateArgs(args);
        String url = argsValidator.get("url");
        int speed = Integer.parseInt(argsValidator.get("speed"));
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        try {
            wget.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

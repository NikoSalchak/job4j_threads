package ru.job4j.thread;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
             FileOutputStream output = new FileOutputStream("wgetTemp.xml")) {
            byte[] buffer = new byte[1024];
            long sleepTime = 0;
            int bytesRead;
            int countRead = 0;
            while ((bytesRead = input.read(buffer, 0, buffer.length)) != -1) {
                countRead += bytesRead;
                long downloadAt = System.nanoTime();
                output.write(buffer, 0, bytesRead);
                long elapsed = System.nanoTime() - downloadAt;
                if (countRead >= speed) {
                    try {
                        sleepTime = (long) (((double) speed / elapsed) * 1_000_000 / 1000);
                        Thread.sleep(sleepTime);

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("Read " + speed + " bytes : " + sleepTime + " milli.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        try {
            wget.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

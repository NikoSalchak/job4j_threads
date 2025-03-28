package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    @Override
    public void run() {
        int index = 0;
        var proccess = new char[]{'-', '\\', '|', '/'};
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(500);
                System.out.print("\rloading... " + proccess[index++]);
                if (index == proccess.length) {
                    index = 0;
                }
            }
        } catch (InterruptedException e) {
            System.out.println(System.lineSeparator() + "completed");
        }

    }

    public static void main(String[] args) {
        Thread t = new Thread(new ConsoleProgress());
        try {
            t.start();
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        t.interrupt();
    }
}

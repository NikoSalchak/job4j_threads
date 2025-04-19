package ru.job4j;

public class CountBarrier {
    private final Object monitor = this;

    private final int total;

    private int count = 0;

    public CountBarrier(final int total) {
        this.total = total;
    }

    public void count() {
        synchronized (monitor) {
            System.out.println("Поток " + Thread.currentThread().getName() + " начал работу.");
            while (count < total) {
                System.out.println(count);
                count++;
            }
            notifyAll();
        }
    }

    public void await() {
        synchronized (monitor) {
            while (count < total) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Поток был прерван");
                }
            }
            System.out.println("Поток " + Thread.currentThread().getName() + " начал работу. count = " + count);
        }
    }
}

class MultiJob {
    public static void main(String[] args) {
        CountBarrier cb = new CountBarrier(10);
        Thread count = new Thread(cb::count, "Count");
        Thread await = new Thread(cb::await, "Await");
        count.start();
        await.start();
    }
}

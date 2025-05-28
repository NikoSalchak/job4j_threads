package ru.job4j.collection;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
class SimpleBlockingQueueTest {

    @Test
    public void whenMaxOfferFourNumAndPoll() {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(4);
        Runnable job1 = () -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    queue.offer(i);
                    System.out.println(Thread.currentThread().getName() + " insert new element " + i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        Runnable job2 = () -> {
            for (int i = 1; i <= 6; i++) {
                try {
                    int element = queue.poll();
                    System.out.println(Thread.currentThread().getName() + " take element " + element);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        Thread consumer = new Thread(job1, "Consumer");
        Thread producer = new Thread(job2, "Producer");
        consumer.start();
        producer.start();
        try {
            consumer.join();
            producer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Queue<Integer> expected = new LinkedList<>();
        expected.add(7);
        expected.add(8);
        expected.add(9);
        expected.add(10);
        assertThat(queue).containsAll(expected);
    }

    @Test
    public void whenFetchAllThenQueueTest() {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);
        Thread producer = new Thread(() -> {
            IntStream.range(0, 5).forEach((element) -> {
                        try {
                            queue.offer(element);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
            );
        });
        producer.start();
        Thread consumer = new Thread(() -> {
            while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                try {
                    buffer.add(queue.poll());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        consumer.start();
        try {
            producer.join();
            consumer.interrupt();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assertThat(buffer).containsExactly(0, 1, 2, 3, 4);
    }

    @Test
    public void whenQueueWithTwoProducerThenFetchAll() {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        Runnable job = new Thread(() -> {
            IntStream.range(0, 5).forEach((element) -> {
                        try {
                            queue.offer(element);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
            );
        });
        Thread producer1 = new Thread(job, "Producer1");
        producer1.start();
        Thread producer2 = new Thread(job, "Producer2");
        producer2.start();
        Thread consumer = new Thread(() -> {
            while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                try {
                    buffer.add(queue.poll());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        consumer.start();
        try {
            producer1.join();
            producer2.join();
            consumer.interrupt();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assertThat(buffer).hasSize(10);

    }
}

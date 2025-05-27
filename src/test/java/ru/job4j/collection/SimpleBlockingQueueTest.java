package ru.job4j.collection;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

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
}

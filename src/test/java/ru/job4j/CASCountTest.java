package ru.job4j;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CASCountTest {

    @Test
    public void whenIncrementTwoThreadThenCorrect() {
        CASCount casCount = new CASCount();
        Runnable job1 = () -> {
            for (int i = 0; i < 50; i++) {
                casCount.increment();
            }
        };

        Thread t1 = new Thread(job1);
        Thread t2 = new Thread(job1);
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assertThat(casCount.get()).isEqualTo(100);
    }
}

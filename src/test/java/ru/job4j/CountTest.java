package ru.job4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountTest {

    @Test
    public void whenExecute2ThreadThen2() throws InterruptedException {
        var count = new Count();
        var first = new Thread(count::increment);
        var second = new Thread(count::increment);
        first.start();
        second.start();
        first.join();
        second.join();
        assertEquals(count.get(), 2);
    }
}

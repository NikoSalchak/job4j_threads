package ru.job4j;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
public class CASCount {
    private final AtomicInteger count = new AtomicInteger();

    public void increment() {
        int next;
        int ref;
        do {
            ref = count.get();
            next = ref + 1;
        } while (!count.compareAndSet(ref, next));
    }

    public int get() {
        return count.get();
    }
}

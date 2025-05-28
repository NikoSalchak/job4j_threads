package ru.job4j.collection;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> implements Iterable<T> {
    @GuardedBy("this")
    private final int size;
    @GuardedBy("this")
    private int counter = 0;
    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();
    
    public SimpleBlockingQueue(int size) {
        this.size = size;
    }

    public synchronized void offer(T value) throws InterruptedException {
        while (counter >= size) {
            wait();
        }
        if (queue.size() < size) {
            queue.offer(value);
            counter++;
        }
        notifyAll();
    }

    public synchronized T poll() throws InterruptedException {
        while (counter == 0) {
            wait();
        }
        T value = queue.poll();
        counter--;
        notifyAll();
        return value;
    }

    public synchronized boolean isEmpty() {
        return counter == 0;
    }

    @Override
    public synchronized String toString() {
        return "SimpleBlockingQueue{" + queue + '}';
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return new LinkedList<>(queue).iterator();
    }
}

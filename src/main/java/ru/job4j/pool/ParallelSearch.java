package ru.job4j.pool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelSearch<T> extends RecursiveTask<Integer> {
    private final T[] array;
    private final T target;
    private final int from;
    private final int to;

    public ParallelSearch(T[] array, T target) {
        this.array = array;
        this.target = target;
        this.from = 0;
        this.to = array.length - 1;
    }

    public ParallelSearch(T[] array, T target, int from, int to) {
        this.array = array;
        this.target = target;
        this.from = from;
        this.to = to;
    }

    @Override
    protected Integer compute() {
        if (to - from < 10) {
            return findIndex();
        }
        int middle = (from + to) / 2;
        ParallelSearch<T> leftSearch = new ParallelSearch<>(array, target, from, middle);
        ParallelSearch<T> rightSearch = new ParallelSearch<>(array, target, middle + 1, to);
        leftSearch.fork();
        rightSearch.fork();
        int left = leftSearch.join();
        int right = rightSearch.join();
        if (left != -1) {
            return left;
        } else {
            return right;
        }
    }

    private int findIndex() {
        for (int i = from; i <= to; i++) {
            if (array[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        String[] array = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
        ParallelSearch<String> search = new ParallelSearch<>(array, "18");
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        System.out.println(forkJoinPool.invoke(search));
    }
}

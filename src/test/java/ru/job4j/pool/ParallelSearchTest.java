package ru.job4j.pool;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;

import static org.assertj.core.api.Assertions.assertThat;

class ParallelSearchTest {

    @Test
    public void whenParallelRecursionSearchStringArray() {
        String[] array = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
        ParallelSearch<String> search = new ParallelSearch<>(array, "18");
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        assertThat(forkJoinPool.invoke(search)).isEqualTo(17);
    }

    @Test
    public void whenParallelRecursionSearchIntegerArray() {
        Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
                16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
        ParallelSearch<Integer> search = new ParallelSearch<>(array, 18);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        assertThat(forkJoinPool.invoke(search)).isEqualTo(17);
    }

    @Test
    public void whenSmallArrayThenLinearSearch() {
        Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ParallelSearch<Integer> search = new ParallelSearch<>(array, 4);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        assertThat(forkJoinPool.invoke(search)).isEqualTo(3);
    }

    @Test
    public void whenParallelRecursionSearchNotFound() {
        Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
                16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
        ParallelSearch<Integer> search = new ParallelSearch<>(array, 100);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        assertThat(forkJoinPool.invoke(search)).isEqualTo(-1);
    }
}

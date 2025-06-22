package ru.job4j.pools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RolColSum {
    private static final Logger LOGGER = LoggerFactory.getLogger(RolColSum.class);

    public static Sums[] sum(int[][] matrix) {
        Sums[] sums = new Sums[matrix.length];
        int sumRow = 0;
        int sumCol = 0;
        int row = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                sumRow += matrix[i][j];
                sumCol += matrix[row][i];
                if (row < matrix.length - 1) {
                    row++;
                } else {
                    row = 0;
                }
            }
            Sums sum = new Sums();
            sum.setRowSum(sumRow);
            sum.setColSum(sumCol);
            sums[i] = sum;
            sumRow = 0;
            sumCol = 0;
        }
        return sums;
    }

    public static Sums[] asyncSum(int[][] matrix) {
        Sums[] sumsTasks = new Sums[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            Sums sum = new Sums();
            try {
                sum.setRowSum(asyncRowSum(matrix, i).get());
                sum.setColSum(asyncColSum(matrix, i).get());
                sumsTasks[i] = sum;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.info("Поток был прерван ", e);
            } catch (ExecutionException e) {
                LOGGER.info("Ошибка в получение из future ", e);
            }
        }
        return sumsTasks;
    }

    private static CompletableFuture<Integer> asyncRowSum(int[][] matrix, int index) {
        return CompletableFuture.supplyAsync(() -> {
            int sumRow = 0;
            for (int j = 0; j < matrix.length; j++) {
                sumRow += matrix[index][j];
            }
            return sumRow;
        });
    }

    private static CompletableFuture<Integer> asyncColSum(int[][] matrix, int index) {
        return CompletableFuture.supplyAsync(() -> {
            int sumCol = 0;
            int row = 0;
            for (int j = 0; j < matrix.length; j++) {
                sumCol += matrix[row][index];
                if (row < matrix.length - 1) {
                    row++;
                } else {
                    row = 0;
                }
            }
            return sumCol;
        });
    }

    public static class Sums {
        private int rowSum;
        private int colSum;

        public int getRowSum() {
            return rowSum;
        }

        public void setRowSum(int rowSum) {
            this.rowSum = rowSum;
        }

        public int getColSum() {
            return colSum;
        }

        public void setColSum(int colSum) {
            this.colSum = colSum;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Sums sums = (Sums) o;
            return rowSum == sums.rowSum && colSum == sums.colSum;
        }

        @Override
        public int hashCode() {
            return Objects.hash(rowSum, colSum);
        }

        @Override
        public String toString() {
            return "Sums{"
                    + "rowSum=" + rowSum
                    + ", colSum=" + colSum
                    + '}';
        }
    }

    public static void main(String[] args) {
        int[][] matrix = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        System.out.println(Arrays.toString(sum(matrix)));
        System.out.println();
        System.out.println(Arrays.toString(asyncSum(matrix)));
    }
}

package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import ru.job4j.pools.RolColSum.*;

import static org.assertj.core.api.Assertions.assertThat;

class RolColSumTest {

    @Test
    public void whenCountSumSingleThreadRunTask() {
        int[][] matrix = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        Sums sumOne = new Sums();
        Sums sumTwo = new Sums();
        Sums sumThree = new Sums();
        sumOne.setRowSum(6);
        sumOne.setColSum(12);
        sumTwo.setRowSum(15);
        sumTwo.setColSum(15);
        sumThree.setRowSum(24);
        sumThree.setColSum(18);
        Sums[] expected = new Sums[]{sumOne, sumTwo, sumThree};
        assertThat(RolColSum.sum(matrix)).isEqualTo(expected);
    }

    @Test
    public void whenCountSumMultiThreadRunTask() {
        int[][] matrix = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        Sums sumOne = new Sums();
        Sums sumTwo = new Sums();
        Sums sumThree = new Sums();
        sumOne.setRowSum(6);
        sumOne.setColSum(12);
        sumTwo.setRowSum(15);
        sumTwo.setColSum(15);
        sumThree.setRowSum(24);
        sumThree.setColSum(18);
        Sums[] expected = new Sums[]{sumOne, sumTwo, sumThree};
        assertThat(RolColSum.asyncSum(matrix)).isEqualTo(expected);
    }

    @Test
    public void whenCountFourElementSumMultiThreadRunTask() {
        int[][] matrix = new int[][]{
                {1, 2, 3, 6},
                {4, 5, 6, 10},
                {7, 8, 9, 12},
                {15, 16, 17, 20}
        };
        Sums sumOne = new Sums();
        Sums sumTwo = new Sums();
        Sums sumThree = new Sums();
        Sums sumFour = new Sums();
        sumOne.setRowSum(12);
        sumOne.setColSum(27);
        sumTwo.setRowSum(25);
        sumTwo.setColSum(31);
        sumThree.setRowSum(36);
        sumThree.setColSum(35);
        sumFour.setRowSum(68);
        sumFour.setColSum(48);
        Sums[] expected = new Sums[]{sumOne, sumTwo, sumThree, sumFour};
        assertThat(RolColSum.asyncSum(matrix)).isEqualTo(expected);
    }
}

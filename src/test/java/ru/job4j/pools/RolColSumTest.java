package ru.job4j.pools;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Класс RolColSumTest тестирует класс RolColSum
 *
 * @author Nikolay Polegaev
 * @version 1.0 12.09.2021
 */
public class RolColSumTest {
    /**
     * Тестирование последовательного выполнения
     */
    @Test
    public void sumTest() {
        int[][] matrix = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[] actuals = new int[2 * matrix.length];
        int[] expecteds = {6, 12, 15, 15, 24, 18};
        RolColSum.Sums[] res = RolColSum.sum(matrix);
        for (int i = 0, j = 0; i < res.length; i++, j+=2) {
            actuals[j] = res[i].getRowSum();
            actuals[j+1] = res[i].getColSum();

        }
        assertEquals(Arrays.toString(expecteds), Arrays.toString(actuals));
    }

    /**Тестирование асинхронного выполнения*/
    @Test
    public void asyncSumTest() throws InterruptedException {
        RolColSum.Sums sums = new RolColSum.Sums();
        int[][] matrix = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[] actuals = new int[2 * matrix.length];
        int[] expecteds = {6, 12, 15, 15, 24, 18};
        RolColSum.Sums[] resAsync = RolColSum.asyncSum(matrix);
        Thread.sleep(10);
        for (int i = 0, j = 0; i < resAsync.length; i++, j += 2) {
            actuals[j] = resAsync[i].getRowSum();
            actuals[j + 1] = resAsync[i].getColSum();

        }
        assertEquals(Arrays.toString(expecteds), Arrays.toString(actuals));
    }
}


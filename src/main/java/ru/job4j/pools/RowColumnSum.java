package ru.job4j.pools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Класс RowColumnSum считает сумму чисел по строкам и столбцам матрицы
 * в последовательном выполнении и асинхронном выполнении.
 *
 * @author Nikolay Polegaev
 * @version 1.0 27.09.2021
 */
public class RowColumnSum {

    public static Sums[] sum(int[][] matrix) {
        Sums[] rsl = new Sums[matrix.length];
        for (int row = 0; row < matrix.length; row++) {
            rsl[row] = getSums(matrix, row);
        }
        return rsl;
    }

    private static Sums getSums(int[][] matrix, int row) {
        int rowRsl = 0;
        int colRsl = 0;
        for (int cell = 0; cell < matrix.length; cell++) {
            rowRsl += matrix[row][cell];
            colRsl += matrix[cell][row];
        }
        return new Sums(rowRsl, colRsl);
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        Sums[] rsl = new Sums[matrix.length];
        Map<Integer, CompletableFuture<Sums>> futures = new HashMap<>();
        for (int row = 0; row < matrix.length; row++) {
            futures.put(row, getTask(matrix, row));
        }
        for (int i = 0; i < rsl.length; i++) {
            rsl[i] = futures.get(i).get();
        }
        return rsl;
    }

    private static CompletableFuture<Sums> getTask(int[][] matrix, int row) {
        return CompletableFuture.supplyAsync(() -> getSums(matrix, row));
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[][] matrix = new int[10000][10000];
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 10000; j++) {
                matrix[i][j] = (i + 1) * (j + 1);
            }
        }
        long start = System.currentTimeMillis();
        Sums[] sum = RowColumnSum.sum(matrix);
        System.out.println(sum[10].getColSum());
        System.out.println(sum[86].getRowSum());
        System.out.println(System.currentTimeMillis() - start);

        long start2 = System.currentTimeMillis();
        Sums[] sumAsync = RowColumnSum.asyncSum(matrix);
        System.out.println(sumAsync[10].getColSum());
        System.out.println(sumAsync[86].getRowSum());
        System.out.println(System.currentTimeMillis() - start2);
    }
}

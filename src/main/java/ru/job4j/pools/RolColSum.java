package ru.job4j.pools;

import java.util.concurrent.CompletableFuture;

/**
 * Класс RolColSum считает сумму чисел по строкам и столбцам матрицы
 * в последовательном выполнении и асинхронном выполнении.
 * Класс с тестами RolColSumTest.
 *
 * @author Nikolay Polegaev
 * @version 1.0 12.09.2021
 */
public class RolColSum {
    /**Статический вложенный класс Sums содержит результаты суммирования элементов матрицы
     * по строкам rowSum и столбцам colSum*/
    public static class Sums {
        private int rowSum = 0;
        private int colSum = 0;

        /* Getter and Setter */
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
    }

    /**Метод sum() вычисляет сумму строк и стобцов элементов матрицы при
     * последовательном выполнении*/
    public static Sums[] sum(int[][] matrix) {
        Sums[] sums = new Sums[matrix.length];
        for (int i = 0; i < sums.length; i++) {
            sums[i] = new RolColSum.Sums();
        }

        //считаем сумму строк
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                sums[row].setRowSum(sums[row].getRowSum() + matrix[row][col]);
            }
        }

        //считаем сумму столбцов
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                sums[i].setColSum(sums[i].getColSum() + matrix[j][i]);
            }
        }
        return sums;
    }

    /**Метод asyncSum() вычисляет сумму строк и стобцов элементов матрицы
     * при асинхронном выполнении.
     * Класс CompletableFuture выполняет здачу асинхронно.
     * Метод .runAsync выполняет задачу асинхронно и ничего не возвращает
     * @param matrix - матрица, для которой производится расчет
     * @return - массив объектов Sum, которые содержат пары результатов
     * для каждой строки и столбца*/
    public static Sums[] asyncSum(int[][] matrix) {
        Sums[] sums = new Sums[matrix.length];
        for (int i = 0; i < sums.length; i++) {
            sums[i] = new RolColSum.Sums();
        }

        CompletableFuture.runAsync(() -> {
            //считаем сумму строк асинхронно
            for (int row = 0; row < matrix.length; row++) {
                for (int col = 0; col < matrix[row].length; col++) {
                    sums[row].setRowSum(sums[row].getRowSum() + matrix[row][col]);
                }
            }
        });

        CompletableFuture.runAsync(() -> {
            //считаем сумму столбцов асинхронно
            for (int i = 0; i < matrix[0].length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    sums[i].setColSum(sums[i].getColSum() + matrix[j][i]);
                }
            }
        });
        return sums;
    }

    /**Ручное тестирование класса RolColSum*/
    public static void main(String[] args) throws InterruptedException {
        int[][] matrix = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        //вычисление последователно
        Sums[] res = RolColSum.sum(matrix);
        for (int i = 0; i < res.length; i++) {
            System.out.println("П:Сумма элементов " + i + " строки = " + res[i].getRowSum() + " ");
            System.out.println("П:Сумма элементов " + i + " столбца = " + res[i].getColSum() + " ");

        }
        //вычисление асинхронно
        Sums[] resAsync = RolColSum.asyncSum(matrix);
        Thread.sleep(10);
        for (int i = 0; i < res.length; i++) {
            System.out.println("A:Сумма элементов " + i + " строки = "
                    + resAsync[i].getRowSum() + " ");
            System.out.println("A:Сумма элементов " + i + " столбца = "
                    + resAsync[i].getColSum() + " ");
        }
    }
}

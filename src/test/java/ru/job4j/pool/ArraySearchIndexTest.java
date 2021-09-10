package ru.job4j.pool;

import org.junit.Test;

import java.util.concurrent.ForkJoinPool;

import static org.junit.Assert.*;

/**
 * Класс ArraySearchIndexTest тестирует класс ArraySearchIndex
 *
 * @author Nikolay Polegaev
 * @version 1.0 10.09.2021
 */
public class ArraySearchIndexTest {

    /**Класс ForkJoinPool и метод invoke()*/
    @Test
    public void whenInvoke() {
        int expected = 3;
        int[] array = {1, 2, 3, 4, 5, 6};
        int element = 4;
        ForkJoinPool pool = ForkJoinPool.commonPool();
        int actual = pool.invoke(new ArraySearchIndex(array, element));
        assertEquals(actual, expected);
    }

    /**Метод fork() и join()*/
    @Test
    public void whenForkAndJoin() {
        int expected = 3;
        int[] array = {1, 2, 3, 4, 5, 6};
        int element = 4;
        ArraySearchIndex as = new ArraySearchIndex(array, element);
        as.fork();
        int actual = as.join();
        assertEquals(actual, expected);
    }

}
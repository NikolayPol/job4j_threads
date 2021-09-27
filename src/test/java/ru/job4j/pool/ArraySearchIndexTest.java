package ru.job4j.pool;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Класс ArraySearchIndexTest тестирует класс ArraySearchIndex
 *
 * @author Nikolay Polegaev
 * @version 2.0 27.09.2021
 */
public class ArraySearchIndexTest {

    /**Класс ForkJoinPool и метод invoke()*/
    @Test
    public void whenInvoke() {
        int expected = 3;
        int[] array = {1, 2, 3, 4, 5, 6};
        int element = 4;
        int actual = new ArraySearchIndex(array, element, 0, array.length - 1).execute();
        assertEquals(actual, expected);
    }

    /**Метод fork() и join()*/
    @Test
    public void whenForkAndJoin() {
        int expected = 3;
        int[] array = {1, 2, 3, 4, 5, 6};
        int element = 4;
        ArraySearchIndex as = new ArraySearchIndex(array, element, 0, array.length - 1);
        as.fork();
        int actual = as.join();
        assertEquals(actual, expected);
    }

}
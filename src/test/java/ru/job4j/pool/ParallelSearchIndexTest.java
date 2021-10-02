package ru.job4j.pool;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Класс ArraySearchIndexTest тестирует класс ArraySearchIndex
 *
 * @author Nikolay Polegaev
 * @version 3.0 02.10.2021
 */
public class ParallelSearchIndexTest {

    private Integer[] array;

    private int el;

    @Before
    public void setUp() {
        int j = 4_192;
        int length = 10_000;
        array = new Integer[length];
        for (int i = 0; i < length; i++) {
            array[i] = 17 * i - 151;
        }
        el = 17 * j - 151;
    }

    @Test
    public void whenFoundIndex() {
        int rsl = ParallelSearchIndex.indexOf(array, el);
        assertThat(rsl, is(4_192));
    }

    @Test
    public void whenNotFoundIndex() {
        int rsl = ParallelSearchIndex.indexOf(array, 28745);
        assertThat(rsl, is(-1));
    }
}
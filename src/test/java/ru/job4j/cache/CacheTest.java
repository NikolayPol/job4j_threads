package ru.job4j.cache;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Класс CacheTest тестирует класс Cache.
 *
 * @author Nikolay Polegaev
 * @version 3.1 12-09-2021
 */
public class CacheTest {
    /**
     * Тестирование сообщения в исключении
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void whenAddThenTrue() {
        Cache cache = new Cache();
        assertTrue(cache.add(new Base(100, 1)));
    }

    @Test
    public void when2AddThenFalse() {
        Cache cache = new Cache();
        cache.add(new Base(100, 1));
        assertFalse(cache.add(new Base(100, 1)));
    }

    @Test
    public void whenUpdate() {
        Cache cache = new Cache();
        Base base = new Base(100, 1);
        cache.add(base);
        Base newBase = new Base(100, 1);
        assertTrue(cache.update(newBase));
    }

    @Test(expected = OptimisticException.class)
    public void whenUpdateDifferenceVersion() {
        Cache cache = new Cache();
        Base base = new Base(100, 1);
        Base base2 = new Base(100, 2);
        cache.add(base);
        cache.update(base2);
    }

    @Test
    public void whenDelete() {
        Cache cache = new Cache();
        Base base = new Base(100, 1);
        assertTrue(cache.add(base));
        assertTrue(cache.delete(base));
    }
}
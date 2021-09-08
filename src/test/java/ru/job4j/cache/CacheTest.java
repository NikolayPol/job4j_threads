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
 * @version 1.0 08-09-2021
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
    public void whenAddAndGet() {
        Cache cache = new Cache();
        Base base = new Base(100, 1);
        cache.add(base);
        assertThat(cache.getMemory().get(100), is(base));
    }

    @Test
    public void whenUpdate() {
        Cache cache = new Cache();
        Base base = new Base(100, 1);
        cache.add(base);
        Base newBase = new Base(100, 1);
        assertTrue(cache.update(newBase));
        assertEquals(cache.getMemory().get(100).getVersion(), 2);
    }

    @Test(expected = OptimisticException.class)
    public void whenUpdateDifferenceVersion() {
        Cache cache = new Cache();
        Base base = new Base(100, 1);
        cache.add(base);
        base.setVersion(2);
        //System.out.println(base.getVersion());
        //System.out.println(cache.getMemory().get(100).getVersion());
        cache.update(base);
    }

    /**
     * Тестирование сообщения в исключении
     */
    @Test
    public void whenUpdateThenExceptionMessageTest() {
        thrown.expect(OptimisticException.class);
        thrown.expectMessage("Модели имеют разные version");
        Cache cache = new Cache();
        Base base = new Base(100, 1);
        cache.add(base);
        base.setVersion(2);
        cache.update(base);
    }

    @Test
    public void whenDelete() {
        Cache cache = new Cache();
        Base base = new Base(100, 1);
        assertTrue(cache.add(base));
        assertTrue(cache.delete(base));
        assertNull(cache.getMemory().get(100));
    }

    @Test(expected = OptimisticException.class)
    public void whenDeleteThenException(){
        Cache cache = new Cache();
        Base base = new Base(100, 1);
        cache.add(base);
        base = new Base(100, 2);
        cache.delete(base);
    }

    /**
     * Тестирование сообщения в исключении
     */
    @Test
    public void whenDeleteThenExceptionMessageTest(){
        thrown.expectMessage("Объекты не совпадают");
        Cache cache = new Cache();
        Base base = new Base(100, 1);
        cache.add(base);
        base = new Base(100, 2);
        cache.delete(base);
    }


}
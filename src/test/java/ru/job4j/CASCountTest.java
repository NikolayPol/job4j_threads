package ru.job4j;

import org.junit.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Класс CASCountTest тестирует класс CASCount.
 *
 * @author Nikolay Polegaev
 * @version 1.0 08-09-2021
 */
public class CASCountTest {
    @Test
    public void when1IncrementThenGetTest() {
        CASCount casCount = new CASCount();
        casCount.increment();
        assertEquals(1, casCount.get());
    }

    @Test
    public void when3IncrementThenGetTest() {
        CASCount casCount = new CASCount();
        casCount.increment();
        casCount.increment();
        casCount.increment();
        assertEquals(3, casCount.get());
    }

    @Test
    public void when2ThreadIncrementAndGet() throws InterruptedException {
        CASCount casCount = new CASCount();
        Runnable r = () -> {
            for (int i = 1; i < 6; i++) {
                casCount.increment();
            }
        };
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        assertEquals(10, casCount.get());
    }
}
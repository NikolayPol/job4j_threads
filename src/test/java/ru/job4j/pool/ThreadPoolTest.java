package ru.job4j.pool;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Класс ThreadPoolTest тестирует класс ThreadPool.
 *
 * @author Niokolay Polegaev
 * @version 1.0 09-09-2021
 */
public class ThreadPoolTest {
    @Test
    public void workTest() {
        ThreadPool tp = new ThreadPool();
        Runnable r = () -> System.out.println(Thread.currentThread().getName()
                + Thread.currentThread().getState());
        for (int i = 0; i < 4; i++) {
            tp.work(r);
        }
        assertEquals(tp.getTasks().getQueueSize(), 4);
    }

    @Test
    public void runTest() {
        ThreadPool tp = new ThreadPool();
        String sb = "Thread-0RUNNABLE, "
                + "Thread-1RUNNABLE, "
                + "Thread-2RUNNABLE, "
                + "Thread-3RUNNABLE";
        List<String> expected = List.of(sb);
        List<String> actual = new ArrayList<>();
        Runnable r = () -> actual.add(Thread.currentThread().getName()
                + Thread.currentThread().getState());
        for (int i = 0; i < 4; i++) {
            tp.work(r);
        }
        tp.run();
        assertEquals(expected.toString(), actual.toString());
    }
}
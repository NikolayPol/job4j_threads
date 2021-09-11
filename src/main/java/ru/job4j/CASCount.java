package ru.job4j;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Класс CASCount является потокобезопасным за счет использования атомарного
 * класса AtomicReference и cas(compare-and-swap)-операций.
 * Тестовый класс CASCountTest
 *
 * @version 1.1 11-09-2021
 * @author Nikolay Polegaev
 */
@ThreadSafe
public class CASCount {
    private final AtomicReference<Integer> count = new AtomicReference<>();

    public CASCount() {
        count.set(0);
    }

    public void increment() {
        int currentCount;
        do {
            currentCount = count.get();
        } while (!count.compareAndSet(currentCount, currentCount + 1));
    }

    public int get() {
        return count.get();
    }
}
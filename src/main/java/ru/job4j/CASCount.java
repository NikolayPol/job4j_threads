package ru.job4j;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Класс CASCount является потокобезопасным за счет использования атомарного
 * класса AtomicReference и cas(compare-and-swap)-операций.
 * Тестовый класс CASCountTest
 *
 * @version 1.0 08-09-2021
 * @author Nikolay Polegaev
 */
@ThreadSafe
public class CASCount {
    private final AtomicReference<Integer> count = new AtomicReference<>();

    public CASCount() {
        count.set(0);
    }

    public void increment() {
        //throw new UnsupportedOperationException("Count is not impl.");
        int currentCount;
        do {
            currentCount = count.get();
        } while (!count.compareAndSet(currentCount, currentCount + 1));
    }

    public int get() {
        //throw new UnsupportedOperationException("Count is not impl.");
        return count.get();
    }
}
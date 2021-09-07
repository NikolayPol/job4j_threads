package ru.job4j.wait;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Класс SimpleBlockingQueue<T> реализовывает шаблон Producer Consumer с блокирующей очередью.
 * Producer помещает данные в очередь, а Consumer извлекает данные из очереди.
 * <p>
 * Тестовый класс SimpleBlockingQueueTest.
 *
 * @author Nikolay Polegaev
 * @version 1.0 07-09-2021
 */
@ThreadSafe

public class SimpleBlockingQueue<T> {
    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();

    public SimpleBlockingQueue() {
    }

    public synchronized int getQueueSize() {
        return queue.size();
    }

    /**
     * Метод offer() добавляет элементы в очередь в синхронизированном режиме
     */
    public void offer(T value) {
            synchronized (this) {
                int limit = 5;
                if (queue.size() == limit) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                queue.offer(value);
                System.out.println("offer size " + queue.size());
                this.notify();
            }
    }

    /**
     * Метод poll() извлекает элементы из очереди в синхронизированном режиме.
     * Если в коллекции объектов нет, то текущий поток переводится в состояние ожидания.
     * когда поток переводится в состояние ожидания, то он отпускает объект монитор
     * и другой поток тоже может выполнить этот метод.
     */
    public T poll() {
            synchronized (this) {
                if (queue.isEmpty()) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                T value = queue.poll();
                System.out.println("poll " + queue.size());
                this.notify();
                return value;
            }

    }
}

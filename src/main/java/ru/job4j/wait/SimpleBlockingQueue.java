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
 * @version 1.2 11-09-2021
 */
@ThreadSafe

public class SimpleBlockingQueue<T> {
    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();
    private final int limit;

    public SimpleBlockingQueue(int limit) {
        this.limit = limit;
    }

    public synchronized int getQueueSize() {
        return queue.size();
    }

    /**
     * Метод offer() добавляет элементы в очередь в синхронизированном режиме
     */
    public void offer(T value) throws InterruptedException {
        synchronized (this) {
            if (queue.size() == limit) {
                this.wait();
            }
            queue.offer(value);
            this.notify();
        }
    }

    /**
     * Метод poll() извлекает элементы из очереди в синхронизированном режиме.
     * Если в коллекции объектов нет, то текущий поток переводится в состояние ожидания.
     * когда поток переводится в состояние ожидания, то он отпускает объект монитор
     * и другой поток тоже может выполнить этот метод.
     */
    public T poll() throws InterruptedException {
        synchronized (this) {
            if (queue.isEmpty()) {
                this.wait();
            }
            T value = queue.poll();
            this.notify();
            return value;
        }
    }

}


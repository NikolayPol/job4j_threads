package ru.job4j.pool;

import ru.job4j.wait.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

/**
 * Класс ThreadPool реализует пул потоков.
 *
 * @author Niokolay Polegaev
 * @version 2.0 12-09-2021
 */
public class ThreadPool {
    private int size = Runtime.getRuntime().availableProcessors();
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>(5);

    public ThreadPool() throws InterruptedException {
        for (int i = 0; i < size; i++) {
            threads.add(new Thread(tasks.poll()));
        }
    }

    public void work(Runnable job) throws InterruptedException {
        tasks.offer(job);
        startPool();
    }

    private void startPool() {
        while (tasks.getQueueSize() != 0) {
            for (int i = 0; i < size; i++) {
                Thread thread = threads.get(i);
                thread.start();
            }
        }
    }

    public void shutdown() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    /**
     * Тестирование класса ThreadPool
     */
    public static void main(String[] args) throws InterruptedException {
        ThreadPool tp = new ThreadPool();
        Runnable r = () -> System.out.println(Thread.currentThread().getName()
                + Thread.currentThread().getState());
        for (int i = 0; i < 4; i++) {
            tp.work(r);
        }
    }
}
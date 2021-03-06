package ru.job4j.pool;

import ru.job4j.wait.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

/**
 * Класс ThreadPool реализует пул потоков.
 *
 * @author Niokolay Polegaev
 * @version 3.0 27-09-2021
 */
public class ThreadPool {
    private final int size = Runtime.getRuntime().availableProcessors();
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>(size);

    public ThreadPool() {
        for (int i = 0; i < size; i++) {
            Thread thread = new Thread(
                    () -> {
                        try {
                            while (!Thread.currentThread().isInterrupted()) {
                                tasks.poll().run();
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
            );
            thread.start();
            threads.add(thread);
        }
    }

    public void work(Runnable job) throws InterruptedException {
        if (job != null) {
            tasks.offer(job);
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
package ru.job4j.pool;

import ru.job4j.wait.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

/**
 * Класс ThreadPool реализует пул потоков.
 *
 * @author Niokolay Polegaev
 * @version 1.0 09-09-2021
 */
public class ThreadPool {
    private int size;
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>(5);

    public ThreadPool() {
        size = Runtime.getRuntime().availableProcessors();
    }

    public int getSize() {
        return size;
    }

    public List<Thread> getThreads() {
        return threads;
    }

    public SimpleBlockingQueue<Runnable> getTasks() {
        return tasks;
    }

    public void work(Runnable job) throws InterruptedException {
        tasks.offer(job);
    }

    public void run() throws InterruptedException {
        while (tasks.getQueueSize() != 0) {
            if (tasks.getQueueSize() < size) {
                size = tasks.getQueueSize();
            }
            for (int i = 0; i < size; i++) {
                threads.add(new Thread(tasks.poll()));
            }
            for (int i = 0; i < size; i++) {
                Thread thread = threads.get(i);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            threads.clear();
        }
    }

    public void shutdown() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}
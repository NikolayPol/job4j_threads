package ru.job4j.pool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Класс реализует параллельный поиск индекса в массиве.
 * См. habr.com/en/post/565924/
 * www.youtube.com/watch?v=fGuvosd-L98
 * @author Nikolay Polegaev
 * @version 3.0 02.10.2021
 */

public class ParallelSearchIndex<T> extends RecursiveTask<Integer> {
    private static final int THRESHOLD = Runtime.getRuntime().availableProcessors();
    private final T[] array;
    private final T element;
    private final int start;
    private final int end;

    public ParallelSearchIndex(T[] array, T element, int start, int end) {
        this.array = array;
        this.element = element;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if ((end - start) <= array.length / THRESHOLD) {
            return indexOf();
        }
        return ForkJoinTask.invokeAll(createTasks())
                .stream()
                .mapToInt(ForkJoinTask::join)
                .max()
                .orElse(-1);
    }

    private Collection<ParallelSearchIndex<T>> createTasks() {
        List<ParallelSearchIndex<T>> tasks = new ArrayList<>();
        int middle = (start + end) / 2;
        tasks.add(new ParallelSearchIndex<>(array, element, start, middle));
        tasks.add(new ParallelSearchIndex<>(array, element, middle + 1, end));
        return tasks;
    }

    private int indexOf() {
        for (int i = start; i <= end; i++) {
            if (array[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int indexOf(T[] array, T el) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new ParallelSearchIndex<>(array, el, 0, array.length - 1));
    }
}
package ru.job4j.pool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Класс реализует параллельный поиск индекса в массиве.
 * См. https://habr.com/en/post/565924/
 * https://www.youtube.com/watch?v=fGuvosd-L98
 *
 * @author Nikolay Polegaev
 * @version 2.0 27.09.2021
 */
public class ArraySearchIndex extends RecursiveTask<Integer> {

    private final int[] array;
    private final int element;
    private final int start;
    private final int end;
    private final ForkJoinPool pool = ForkJoinPool.commonPool();

    public ArraySearchIndex(int[] array, int element, int start, int end) {
        this.array = array;
        this.element = element;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        Integer index = null;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == element) {
                index = i;
            }
        }
        return index;
    }

    public int execute() {
        return pool.invoke(new ArraySearchIndex(array, element, start, end));
    }
}
package ru.job4j.pool;

import java.util.concurrent.RecursiveTask;

/**
 * Класс реализует параллельный поиск индекса в массиве.
 * См. https://habr.com/en/post/565924/
 * https://www.youtube.com/watch?v=fGuvosd-L98
 *
 * @author Nikolay Polegaev
 * @version 1.0 10.09.2021
 */
public class ArraySearchIndex extends RecursiveTask<Integer> {

    private final int[] array;
    private final int element;

    public ArraySearchIndex(int[] array, int element) {
        this.array = array;
        this.element = element;
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
}
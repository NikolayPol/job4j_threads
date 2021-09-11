package ru.job4j.wait;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Класс SimpleBlockingQueueTest тестирует класс SimpleBlockingQueue<T>
 * <p>
 * * @author Nikolay Polegaev
 * * @version 2.0 11-09-2021
 */
public class SimpleBlockingQueueTest {
    @Test
    public void offerTest() {
        SimpleBlockingQueue<Integer> sb = new SimpleBlockingQueue<>(5);
        Runnable  produser = () -> {
            for (int i = 1; i < 6; i++) {
                try {
                    sb.offer(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        Thread p = new Thread(produser);
        p.start();
        try {
            p.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(5, sb.getQueueSize());
    }

    @Test
    public void offerAndPollTest() {
        List<Integer> expected = List.of(1, 2, 3, 4, 5);
        List<Integer> actual = new ArrayList<>();
        SimpleBlockingQueue<Integer> sb = new SimpleBlockingQueue<>(5);
        Runnable produser = () -> {
            for (int i = 1; i < 6; i++) {
                try {
                    sb.offer(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable consumer = () -> {
            while (sb.getQueueSize() != 0 || Thread.currentThread().isInterrupted()) {
                try {
                    actual.add(sb.poll());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread p = new Thread(produser);
        Thread c = new Thread(consumer);
        p.start();
        c.start();
        try {
            p.join();
            c.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(expected, actual);
    }

    @Test
    public void whenFetchAllThenGetIt() {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);
        Thread producer = new Thread(
                () -> {
                    for (int i = 1; i < 6; i++) {
                        try {
                            queue.offer(i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    while (queue.getQueueSize() != 0
                            || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        try {
            producer.join();
            consumer.interrupt();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(buffer, is(Arrays.asList(1, 2, 3, 4, 5)));
    }
}

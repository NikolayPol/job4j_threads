package ru.job4j.wait;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Класс SimpleBlockingQueueTest тестирует класс SimpleBlockingQueue<T>
 * <p>
 * * @author Nikolay Polegaev
 * * @version 1.1 08-09-2021
 */
public class SimpleBlockingQueueTest {
    @Test
    public void offerTest() throws InterruptedException {
        SimpleBlockingQueue<Integer> sb = new SimpleBlockingQueue<>();
        Runnable produser = () -> IntStream.range(1, 6).forEach(sb::offer);
        Thread p = new Thread(produser);
        p.start();
        p.join();
        assertEquals(5, sb.getQueueSize());
    }

    @Test
    public void offerAndPollTest() throws InterruptedException {
        List<Integer> expected = List.of(1, 2, 3, 4, 5);
        List<Integer> actual = new ArrayList<>();
        SimpleBlockingQueue<Integer> sb = new SimpleBlockingQueue<>();
        Runnable produser = () -> IntStream.range(1, 6).forEach(sb::offer);
        Runnable consumer = () -> {
            while (sb.getQueueSize() != 0 || Thread.currentThread().isInterrupted()) {
                // try {
                actual.add(sb.poll());
                // } catch (InterruptedException e) {
                //     Thread.currentThread().interrupt();
                // }
            }
        };
        Thread p = new Thread(produser);
        Thread c = new Thread(consumer);
        p.start();
        Thread.sleep(1000);
        c.start();
        p.join();
        //c.interrupt();
        c.join();
        //System.out.println(expected);
        //System.out.println(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();
        Thread producer = new Thread(
                () -> {
                    IntStream.range(0, 5).forEach(
                            queue::offer
                    );
                }
        );
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
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer, is(Arrays.asList(0, 1, 2, 3, 4)));
    }
}

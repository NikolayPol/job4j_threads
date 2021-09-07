package ru.job4j.wait;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class SimpleBlockingQueueTest {
    @Test
    public void offerAndPollTest() throws InterruptedException {
        List<Integer> expected = List.of(1, 2, 3, 4, 5);
        List<Integer> actual = new ArrayList<>();
        SimpleBlockingQueue<Integer> sb = new SimpleBlockingQueue<>();
        Runnable produser = () -> IntStream.range(1, 6).forEach(sb::offer);
        Runnable consumer = () -> {
            while (sb.getQueueSize() != 0) {
                actual.add(sb.poll());
            }
        };
        Thread p = new Thread(produser);
        Thread c = new Thread(consumer);
        p.start();
        Thread.sleep(1000);
        c.start();
        p.join();
        c.join();
        System.out.println(expected);
        System.out.println(actual);
        assertEquals(expected, actual);
    }
}
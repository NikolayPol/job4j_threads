package ru.job4j.concurrent;

public class ConcurrentOutput {
    public static void main(String[] args) {
        Thread another = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );
        Thread second = new Thread(
                () -> System.out.println(Thread.currentThread().getName()));
        Runnable r = () -> System.out.println(Thread.currentThread().getName());

        another.start(); //2 второй поток
        second.start(); //3 поток
        new Thread(r).start(); //4 поток
        System.out.println(Thread.currentThread().getName()); //1 поток
    }
}

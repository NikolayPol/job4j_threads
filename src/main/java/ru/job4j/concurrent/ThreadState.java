package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Runnable r = () -> {
        };
        var first = new Thread(r);
        var second = new Thread(r);
        System.out.println("Первый поток: " + first.getName()
                + " Состояние: " + first.getState());
        System.out.println("Второй поток: " + second.getName()
                + " Состояние: " + second.getState());
        first.start();
        second.start();
        while (first.getState() != Thread.State.TERMINATED
                || second.getState() != Thread.State.TERMINATED) {
            System.out.println("Первый поток: " + first.getState());
            System.out.println("Второй поток: " + second.getState());
        }
        System.out.println("Работа завершена");
    }
}

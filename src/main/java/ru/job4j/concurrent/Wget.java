package ru.job4j.concurrent;

public class Wget {
    public static void main(String[] args) {
        Thread thread = new Thread(
                () -> {
                    for (int index = 0; index <= 100; index += 10) {
                        System.out.print("\rLoading : " + index + "%");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("\nLoaded");
                }
        );
        thread.start();
        System.out.println("Main");
    }
}
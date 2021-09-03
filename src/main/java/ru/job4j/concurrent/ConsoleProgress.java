package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {
    @Override
    public void run() {
        String[] arr = new String[]{"\\", "|", "/"};
        while (!Thread.currentThread().isInterrupted()) {
            try {
                for (String el : arr) {
                    System.out.print("\r load: " + el);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(10000); /* симулируем выполнение параллельной задачи в течение 10 секунд. */
        progress.interrupt();
    }
}

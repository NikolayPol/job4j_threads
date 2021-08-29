package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(50);
                System.out.print("\r load: " + "\\");
                Thread.sleep(50);
                System.out.print("\r load: " + "|");
                Thread.sleep(50);
                System.out.print("\r load: " + "/");
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

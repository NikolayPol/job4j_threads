package ru.job4j;

/**
 *  Класс, который блокирует выполнение по условию счетчика.
 *
 * @version 1.0 5-09-2021
 * @author Nikolay Polegaev
 */
public class CountBarrier {
    /** Переменная сделана для наглядности объекта монитора*/
    private final Object monitor = this;
    /** Переменная total содержит количество вызовов метода count().*/
    private final int total;
    /** Счетчик, показывающий количество вызовов класса*/
    private int count = 0;

    public CountBarrier(final int total) {
        this.total = total;
    }

    /** Метод count изменяет состояние программы. Это значит,
     * что внутри метода count нужно вызывать метод notifyAll.*/
    public void count() {
        synchronized (monitor) {
            count++;
            this.notifyAll();
        }
    }

    /** Нити, которые выполняют метод await, могут начать работу если поле count >= total.
     * Если оно не равно, то нужно перевести нить в состояние wait.*/
    public void await() {
        synchronized (monitor) {
            while (count < total) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /** Тестирование класса CountBarrier
     * 5 и 6 потоки заканчивают выполнение последними*/
    public static void main(String[] args) {
        CountBarrier countBarrier = new CountBarrier(4);

        Runnable r = () -> {
            System.out.println(Thread.currentThread().getName() + " started");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            countBarrier.count();
            System.out.println(Thread.currentThread().getName() + " stopped");

        };

        Runnable r2 = () -> {
            System.out.println(Thread.currentThread().getName() + " started");
            countBarrier.await();
            System.out.println(Thread.currentThread().getName() + " stopped");

        };
        new Thread(r2, "Пятый поток").start();
        new Thread(r2, "Шестой поток").start();
        new Thread(r, "Первый поток").start();
        new Thread(r, "Второй поток").start();
        new Thread(r, "Третий поток").start();
        new Thread(r, "Четвертый поток").start();
    }
}

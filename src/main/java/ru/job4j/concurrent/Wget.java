package ru.job4j.concurrent;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * Класс Wget скачивает файл из сети с ограничением по скорости скачки.
 * Засекается время скачивания 1024 байт. Если время меньше указанного,
 * то выставляется пауза за счет Thread.sleep.
 *
 * @author Nikolay Polegaev
 * @version 2.0
 */
public class Wget implements Runnable {
    /**
     * Поля:
     * -url - url-адрес ресурса для скачивания.
     * -speed - скорость скаивания.
     */
    private final String url;
    private final int speed;
    private String fileName = "default.xml";

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    public Wget(String url, int speed, String fileName) {
        this.url = url;
        this.speed = speed;
        this.fileName = fileName;
    }

    /**
     * Меотод run() скачивает данные с ресурса в файл.
     * Если скорость меньше заданной, то поток переводится в режим
     * ожидания, чтобы выдержать эту паузу.
     */
    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            long startTime, stopTime, workTime, pauseTime;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                startTime = System.nanoTime();
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                stopTime = System.nanoTime();
                workTime = stopTime - startTime;
                pauseTime = speed * 100000L - workTime;
                if (workTime < speed * 100000L) {
                    System.out.println("Process waiting: " + pauseTime + " ns");
                    Thread.sleep(pauseTime / 100000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод main() тестирует класс Wget.
     *
     * @param args - 1й аргумент - это url-адрес скачиваемого ресурса.
     *             - 1й аргумент - это скорость скачивания.
     *             - 3й необязательный аргумент - это имя файла-результата.
     *             Если его не указать, результатом будет файл с именем default.xml
     * @throws InterruptedException - исключение, если передаваемые аргументы
     *              невалидные.
     */
    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Not valid arguments");
        }
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        String fileName;
        Thread wget;
        if (args.length > 2) {
            fileName = args[2];
            wget = new Thread(new Wget(url, speed, fileName));
        } else {
            wget = new Thread(new Wget(url, speed));
        }
        wget.start();
        wget.join();
    }
}

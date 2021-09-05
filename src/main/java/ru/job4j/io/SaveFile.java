package ru.job4j.io;

import java.io.*;

/**
 * Класс SaveFile сохраняет данные в файл.
 * Поле file является final, синхронизация не требуется.
 * Метод saveContent() является синхронизированным.
 * Класс является Immutable.
 *
 * @author Nikolay Polegaev
 * @version 1.0
 */
public class SaveFile {
    private final File file;

    public SaveFile(File file) {
        this.file = file;
    }

    public synchronized void saveContent(String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Тестирование класса SaveFile.
     */
    public static void main(String[] args) {
        File file = new File("src/main/java/ru/job4j/io/test.txt");
        SaveFile sf = new SaveFile(file);
        sf.saveContent("\nТестовая строка");
    }
}

package ru.job4j.io;

import java.io.*;
import java.util.function.Predicate;

/**
 * Класс ParseFile по значению фильтра выводит в консоль содержание файла.
 * Поле file является final, синхронизация не требуется.
 * Метод getContent() является синхронизированным.
 * Класс является Immutable.
 *
 * @author Nikolay Polegaev
 * @version 1.0
 */
public class ParseFile {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public synchronized String getContent(Predicate<Character> filter) {
        int data;
        StringBuilder output = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file)))) {
            while ((data = br.read()) != -1) {
                if (filter.test((char) data)) {
                    output.append((char) data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    /**
     * Тестирование класса ParseFile.
     * В зависимости от условия в фильтре метод getContent(filter) выведет
     * на консоль символы по таблице Unicode.
     * Латинские символы начинаются с 0x0041.
     * Русские ссимволы начинаются с 0x0410
     */
    public static void main(String[] args) {
        File file = new File("src/main/java/ru/job4j/io/test.txt");
        ParseFile pf = new ParseFile(file);
        Predicate<Character> filter1 = data -> true;
        Predicate<Character> filter2 = data -> data < 0x80;
        Predicate<Character> filter3 = data -> data < 0x0079;
        System.out.println(pf.getContent(filter1));
    }
}


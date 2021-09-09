package ru.job4j.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/***
 * Класс EmailNotification еализует сервис для рассылки почты.
 *
 * @version 1.0 09-09-2021
 * @author Nikolay Polegaev
 */
public class EmailNotification {
    private final ExecutorService pool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());
    private final String subject = "Notification {username} to email {email}";
    private final String body = "Add a new event to {username}";

    public EmailNotification() {
    }

    /**
     * Метод emailTo(User user) через ExecutorService отправлять почту.
     */
    public void emailTo(User user) {
        String buff = subject.replace("{username}", user.getUserName());
        String subjectToEmail = buff.replace("{email}", user.getEmail());
        String bodyToEmail = body.replace("{username}", user.getUserName());
        pool.submit(() -> {
            System.out.println(subjectToEmail);
            System.out.println(bodyToEmail);
        });
        pool.submit(() -> send(subjectToEmail, bodyToEmail, user.getEmail()));
    }

    /**
     * Метод close() закpывает pool
     */
    public void close() {
        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String subject, String body, String email) {

    }

    /**Тестирование класса EmailNotification*/
    public static void main(String[] args) {
        EmailNotification en = new EmailNotification();
        User user = new User("Parfiry", "123@mail.ru");
        en.emailTo(user);
        en.close();
    }
}

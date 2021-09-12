package ru.job4j.jcip;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Objects;

/**
 * Класс UserStorage реализует блокирующий кеш для модели User.
 * В классе реализованы синхронизированные методы добавления, обновления, удаления User-ов
 * Объектом-минитором является класс UserStorage.
 * ThreadSafe - класс можно использовать в многопоточном режиме и он будет работать правильно.
 * GuardedBy("this") - выставляется над общим ресурсом. Аннотация имеет входящий параметр.
 * Он указывает на объект монитора, по которому мы будем синхронизироваться.
 * Класс протестирован библиотекой JCIP.
 *
 * @version 3.0 12-09-2021
 * @author Nikolay Polegaev
 */
@ThreadSafe
public class UserStorage {
    @GuardedBy("this")
    private final HashMap<Integer, User> users = new HashMap<>();

    public synchronized boolean add(User user) {
        return Objects.isNull(users.putIfAbsent(user.getId(), user));
    }

    public synchronized boolean update(User user) {
        return Objects.nonNull(users.replace(user.getId(), user));
    }

    public synchronized boolean delete(User user) {
        return Objects.nonNull(users.remove(user.getId()));
    }

    /**
     * Метод transfer(int fromId, int toId, int amount) проводит транзакцию со счета fromId
     * на счет toId в размере amount.
     *
     * @param fromId - счет списания.
     * @param toId - счет зачислеия.
     * @param amount - размер изменения счета.
     */
    public synchronized void transfer(int fromId, int toId, int amount) {
        User userFrom = users.get(fromId);
        User userTo = users.get(toId);
        if (userFrom == null || userTo == null || userFrom.getAmount() < amount) {
            System.err.println("Ошибка выполнения транзакции");
            return;
        }
        userFrom.setAmount(userFrom.getAmount() - amount);
        userTo.setAmount(userTo.getAmount() + amount);
        System.out.println("Танзакция выполнена успешно");
        System.out.println(users.get(fromId));
        System.out.println(users.get(toId));

    }

    /**
     * Метод для тестирования класса UserStorage.
     *
     */
    public static void main(String[] args) {
        UserStorage us = new UserStorage();
        //добавляем пользователей
        us.add(new User(1, 1000));
        us.add(new User(2, 2000));
        //производит транзакцию с исключением
        us.transfer(2, 1, 2200);
        //производит транзакцию без исключения
        us.transfer(2, 1, 200);
        us.update(new User(2, 5000));
        //добавление и удаление user
        us.add(new User(3, 6000));
        us.delete(new User(3, 6000));
    }
}

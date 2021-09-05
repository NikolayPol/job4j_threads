package ru.job4j.jcip;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс UserStorage реализует блокирующий кеш для модели User.
 * В классе реализованы синхронизированные методы добавления, обновления, удаления User-ов
 * Объектом-минитором является класс UserStorage.
 * ThreadSafe - класс можно использовать в многопоточном режиме и он будет работать правильно.
 * GuardedBy("this") - выставляется над общим ресурсом. Аннотация имеет входящий параметр.
 * Он указывает на объект монитора, по которому мы будем синхронизироваться.
 * Класс протестирован библиотекой JCIP.
 *
 * @version 1.0 5-09-2021
 * @author Niklay Polegaev
 */
@ThreadSafe
public class UserStorage {
    @GuardedBy("this")
    private final ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    public ConcurrentHashMap<Integer, User> getUsers() {
        return users;
    }

    public synchronized boolean add(User user) {
        boolean res = false;
        User userReturn = users.put(id.incrementAndGet(), new User(id.get(), user.getAmount()));
        if (user.equals(userReturn)) {
            res = true;
        }
        return res;
    }

    public synchronized boolean update(User user) {
        boolean res = false;
        int id = user.getId();
        User userReturn = users.replace(id, new User(id, user.getAmount()));
        if (user.equals(userReturn)) {
            res = true;
        }
        return res;
    }

    public synchronized boolean delete(User user) {
        boolean res = false;
        User userReturn = users.remove(user.getId());
        if (user.equals(userReturn)) {
            res = true;
        }
        return res;
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
        if (userFrom.getAmount() < amount) {
            //throw new IllegalStateException("Недостаточно средств для выполнения транзакции");
            System.err.println("Недостаточно средств для выполнения транзакции");
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
        us.add(new User(1, 2000));
        //производит транзакцию с исключением
        us.transfer(2, 1, 2200);
        //производит транзакцию без исключения
        us.transfer(2, 1, 200);
        us.update(new User(2, 5000));
        System.out.println(us.getUsers());
        //добавление и удаление user
        us.add(new User(3, 6000));
        System.out.println(us.getUsers());
        us.delete(new User(3, 6000));
        System.out.println(us.getUsers());
    }
}

package ru.job4j.cache;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс Cache реализует неблокирующий кеш.
 * Тестовый класс CacheTest.
 *
 * @author Nikolay Polegaev
 * @version 3.0 12-09-2021
 */
@ThreadSafe
public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    /**
     * Для обеспечения потокобезопасности применяются CAS-методы и атомарные операции
     */
    public boolean add(Base model) {
        return Objects.isNull(memory.putIfAbsent(model.getId(), model));
    }

    /**
     * В кеше же нужно перед обновлением данных проверить поле version.
     * Если version у модели и в кеше одинаковы, то можно обновить.
     * Если нет, то выбросить OptimisticException.
     * Перед обновлением данных необходимо проверять текущую версию и ту,
     * что обновляем и увеличивать на единицу каждый раз, когда произошло обновление.
     * Если версии не равны -  кидать исключение.
     */
    public boolean update(Base model) {
        return Objects.nonNull(memory.computeIfPresent(model.getId(), (key, v) -> {
            Base stored = memory.get(model.getId());
            if (stored.getVersion() != model.getVersion()) {
                throw new OptimisticException("Версия обновляемой модели не совпадают");
            }
            model.setVersion(model.getVersion() + 1);
            return model;
        }));
    }

    public boolean delete(Base model) {
        return Objects.nonNull(memory.remove(model.getId(), model));
    }
}
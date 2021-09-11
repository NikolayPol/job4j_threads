package ru.job4j.cache;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс Cache реализует неблокирующий кеш.
 * Тестовый класс CacheTest.
 *
 * @author Nikolay Polegaev
 * @version 2.0 11-09-2021
 */
public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public Map<Integer, Base> getMemory() {
        return memory;
    }

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
        model.setVersion(model.getVersion() + 1);
        return Objects.nonNull(memory.computeIfPresent(model.getId(), (key, v) -> model));
    }

    public boolean delete(Base model) {
        return Objects.nonNull(memory.remove(model.getId(), model));
    }
}
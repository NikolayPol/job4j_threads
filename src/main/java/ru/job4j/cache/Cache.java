package ru.job4j.cache;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс Cache реализует неблокирующий кеш.
 * Тестовый класс CacheTest.
 *
 * @author Nikolay Polegaev
 * @version 1.0 08-09-2021
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
        Base newModel = null;
        try {
            newModel = (Base) model.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return memory.putIfAbsent(model.getId(), newModel) == null;
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
        Base base = null;
        if (memory.get(model.getId()).getVersion() != model.getVersion()) {
            throw new OptimisticException("Модели имеют разные version");
        }
        try {
            base = (Base) model.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assert base != null;
        base.setVersion(model.getVersion() + 1);
        Base stored = memory.replace(model.getId(), base);
        if (!Objects.equals(stored, model)) {
            throw new OptimisticException("Update failed");
        }
        return true;
    }

    public boolean delete(Base model) {
        Base stored = memory.get(model.getId());
        if (!stored.equals(model)) {
            throw new OptimisticException("Объекты не совпадают");
        }
        return memory.remove(model.getId(), model);
    }
}
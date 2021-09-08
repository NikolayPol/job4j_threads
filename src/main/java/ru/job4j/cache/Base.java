package ru.job4j.cache;

import java.util.Objects;

/***
 * Класс Base описывает базовую модель данных.
 * ID - уникальный идентификатор. В системе будет только один объект с таким ID.
 * В кеше должна быть возможность проверять актуальность данных.
 * Для этого в модели данных используется поле int version.
 * Это поле должно увеличиваться на единицу каждый раз, когда модель изменили,
 * то есть вызвали метод update.
 * Даже если все поля остались не измененными поле version должно увеличиться на 1.
 * Например. Два пользователя прочитали объект task ID = 1.
 * Первый пользователь изменил поле имя и второй сделал то же самое.
 * Теперь пользователи сохраняют изменения. Для этого они вызывают метод update.
 *
 * В этом случае возникает ситуация, которая называется "последний выиграл".
 * То есть в кеше сохраняться данные только последнего пользователя.
 *
 * В кеше же нужно перед обновлением данных проверить поле version.
 * Если version у модели и в кеше одинаковы, то можно обновить.
 * Если нет, то выбросить OptimisticException.
 * Перед обновлением данных необходимо проверять текущую версию и ту что обновляем и
 * увеличивать на единицу каждый раз, когда произошло обновление.
 * Если версии не равны -  кидать исключение.
 *
 * Поле name - это поля бизнес модели.
 */

public class Base implements Cloneable {
    private final int id;
    private int version;
    private String name;

    public Base(int id, int version) {
        this.id = id;
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Base base = (Base) o;
        return id == base.id && version == base.version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }
}
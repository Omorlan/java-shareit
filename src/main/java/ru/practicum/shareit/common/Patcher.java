package ru.practicum.shareit.common;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public class Patcher {
    public static void patch(Object updObj, Object newObj) throws IllegalAccessException {
        Class<?> clazz = updObj.getClass();
        Field[] fieldsObj = clazz.getDeclaredFields();
        for (Field field : fieldsObj) {
            field.setAccessible(true);
            Object value = field.get(newObj);
            if (value != null) {
                field.set(updObj, value);
            }
            field.setAccessible(false);
        }
    }
}

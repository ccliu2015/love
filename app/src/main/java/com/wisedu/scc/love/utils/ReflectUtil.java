package com.wisedu.scc.love.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {
	public static Field getField(Class<?> type, String name) {
		for (Class<?> clazz = type; clazz != Object.class; clazz = clazz
				.getSuperclass()) {
			try {
				return clazz.getDeclaredField(name);
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}

	public static Method getMethod(Class<?> type, String name,
			Class<?>... params) {
		for (Class<?> clazz = type; clazz != Object.class; clazz = clazz
				.getSuperclass()) {
			try {
				return clazz.getDeclaredMethod(name, params);
			} catch (NoSuchMethodException e) {
			}
		}
		return null;
	}

    public static Object getFieldValue(Object object, String name) {
        Field field = getField(object.getClass(), name);
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}

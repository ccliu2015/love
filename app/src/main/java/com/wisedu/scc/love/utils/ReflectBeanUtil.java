package com.wisedu.scc.love.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过Java反射机制获取JavaBean对象
 */
public class ReflectBeanUtil {

    // 不属于JavaBean自身的属性
    private static final String NOFIELDNAME = "serialVersionUID";

    /**
     * 根据字符串获取JavaBean
     * @param beanName, 例如：com.package.MyClass
     */
    public static Object getInstanceByBeanName(String beanName) {
        try {
            return Class.forName(beanName).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据字符串获取JavaBean
     * @param beanName, 例如：com.package.MyClass
     */
    public static Class<?> getClazzByBeanName(String beanName) {
        try {
            return Class.forName(beanName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据JavaBean获取属性名称和类型
     * @param beanName  例如：com.package.MyClass
     * @return
     */
    public static Map<String, Field> getFields(String beanName) {
        try {
            Object obj = getInstanceByBeanName(beanName);
            Map<String, Field> results = new HashMap<>();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                String key = field.getName();
                if (!NOFIELDNAME.equals(key)) {
                    results.put(key, field);
                }
            }
            return results;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据属性名称和JavaBean获取属性值
     * @param fieldName
     * @param obj
     * @return
     */
    public static Object getFieldValueByName(Object obj, String fieldName) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = obj.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(obj, new Object[]{});
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断属性值是否是规定的类型
     */
    public static boolean isBaseType(Object value) {
        return value instanceof String || value instanceof Integer
                || value instanceof Date || value instanceof Boolean
                || value instanceof Double || value instanceof Float
                || value instanceof Byte || value instanceof Short
                || value instanceof Long;
    }

}
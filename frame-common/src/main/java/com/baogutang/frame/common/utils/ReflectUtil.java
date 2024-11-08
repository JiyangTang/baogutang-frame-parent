package com.baogutang.frame.common.utils;

import lombok.experimental.UtilityClass;
import org.springframework.core.convert.Property;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 *
 */
@UtilityClass
public class ReflectUtil extends ReflectionUtils {


    /**
     * 获取 bean 的属性信息
     * @param propertyType 类型
     * @param propertyDescriptor PropertyDescriptor
     * @param propertyName 属性名
     * @return {Property}
     */
    public static Property getProperty(Class<?> propertyType, PropertyDescriptor propertyDescriptor, String propertyName) {
        Method readMethod = propertyDescriptor.getReadMethod();
        Method writeMethod = propertyDescriptor.getWriteMethod();
        return new Property(propertyType, readMethod, writeMethod, propertyName);
    }

    /**
     * 获取 类属性信息
     * @param propertyType 类型
     * @param propertyDescriptor PropertyDescriptor
     * @param propertyName 属性名
     * @return {Property}
     */
    public static TypeDescriptor getTypeDescriptor(Class<?> propertyType, PropertyDescriptor propertyDescriptor, String propertyName) {
        Method readMethod = propertyDescriptor.getReadMethod();
        Method writeMethod = propertyDescriptor.getWriteMethod();
        Property property = new Property(propertyType, readMethod, writeMethod, propertyName);
        return new TypeDescriptor(property);
    }

    /**
     * 获取 类属性
     * @param clazz 类信息
     * @param fieldName 属性名
     * @return Field
     */
    @Nullable
    public static Field getField(Class<?> clazz, String fieldName) {
        while (clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 获取 所有 field 属性上的注解
     * @param clazz 类
     * @param fieldName 属性名
     * @param annotationClass 注解
     * @param <T> 注解泛型
     * @return 注解
     */
    @Nullable
    public static <T extends Annotation> T getAnnotation(Class<?> clazz, String fieldName, Class<T> annotationClass) {
        Field field = ReflectUtil.getField(clazz, fieldName);
        if (field == null) {
            return null;
        }
        return field.getAnnotation(annotationClass);
    }
}

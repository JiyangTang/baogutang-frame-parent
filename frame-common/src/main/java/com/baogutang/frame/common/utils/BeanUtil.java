package com.baogutang.frame.common.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author
 * @apiNote bean转换类
 */
public class BeanUtil {

    /**
     * @author:
     * @date:
     * @params: source：源对象；t：目标类class对象
     * @return: 目标对象
     * @description: source类属性复制给目标类
     */
    public static <T> T convertBean(Object source, Class<T> t) {
        T instance = BeanUtils.instantiateClass(t);
        if (ObjectUtils.isEmpty(source)) {
            return instance;
        }
        BeanUtils.copyProperties(source, instance);
        return instance;
    }

    public static <T> List<T> convertList(Collection<?> source, Class<T> t) {
        if (CollectionUtil.isEmpty(source)) {
            return new ArrayList<>();
        }
        List<T> conList = new ArrayList<>(source.size());
        for (Object o : source) {
            T bean = convertBean(o, t);
            conList.add(bean);
        }
        return conList;
    }

    public static <S, T> List<T> copyCollectToBean(Collection<S> sources, Supplier<T> target) {
        List<T> conList = new ArrayList<T>(sources.size());
        for (S source : sources) {
            T t = target.get();
            BeanUtils.copyProperties(source, t);
            conList.add(t);
        }
        return conList;
    }

}

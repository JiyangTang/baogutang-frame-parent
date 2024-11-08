package com.baogutang.frame.common.utils;

import com.baogutang.frame.common.res.AbstractPageParam;
import com.baogutang.frame.common.response.Pages;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;


/**
 * @description:
 * @author: nikooh
 * @date: 2024/03/07 : 20:17
 */
public class PageUtil {

    private PageUtil() {
        // private empty constructor
    }

    /**
     * general method of paging query
     *
     * @param queryFunction    page query function
     * @param pageParam        page param
     * @param beanCastFunction bean cast function
     * @param <T>              entity class
     * @param <U>              query parameter
     * @param <R>              return bean
     * @return Page
     */
    public static <T, U extends AbstractPageParam, R> Pages<R> page(UnaryOperator<Page<T>> queryFunction, U pageParam, Function<T, R> beanCastFunction) {
        Page<T> pageReq = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        Page<T> pageRes = queryFunction.apply(pageReq);
        List<R> rPageRes = pageRes.getRecords().stream()
                .map(beanCastFunction)
                .collect(Collectors.toList());
        return new Pages<>(rPageRes, (int) pageRes.getTotal(), (int) pageRes.getSize(), (int) pageRes.getCurrent());
    }

    /**
     *
     *
     * @param orders
     * @param queryWrapper
     * @param <T>          T
     */
    public static <T> void defaultOrders(List<AbstractPageParam.OrderBy> orders, QueryWrapper<T> queryWrapper) {
        defaultOrder(orders, queryWrapper, null);
    }

    /**
     * ,
     *
     * @param orders
     * @param queryWrapper
     * @param defaultColumn
     * @param <T>           T
     */
    public static <T, R> void defaultOrder(List<AbstractPageParam.OrderBy> orders, QueryWrapper<T> queryWrapper, SFunction<T, R> defaultColumn) {
        if (CollectionUtils.isEmpty(orders)) {
            defaultOrderProcess(queryWrapper, defaultColumn);
            return;
        }
        orders.forEach(orderBy -> {
            //
            if (StringUtils.isBlank(orderBy.getColumnName())) {
                defaultOrderProcess(queryWrapper, defaultColumn);
            } else {
                if (orderBy.isDesc()) {
                    queryWrapper.orderByDesc(orderBy.getColumnName());
                } else {
                    queryWrapper.orderByAsc(orderBy.getColumnName());
                }
            }
        });
    }

    /**
     * @param queryWrapper
     * @param defaultColumn
     * @param <T>           t
     * @param <R>           r
     */
    private static <T, R> void defaultOrderProcess(QueryWrapper<T> queryWrapper, SFunction<T, R> defaultColumn) {
        if (defaultColumn != null) {
            queryWrapper.lambda()
                    .orderBy(true, false, defaultColumn);
        }
    }
}

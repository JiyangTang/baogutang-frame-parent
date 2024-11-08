package com.baogutang.frame.common.utils;

import com.baogutang.frame.common.annotation.RequestParamChecker;
import com.baogutang.frame.common.exception.ParamsValidException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参数校验工具类
 *
 */
public class ParamsValidUtils {

    public static void checkParam(Object args) throws ParamsValidException {
        try {
            if (args != null) {
                Field[] field = args.getClass().getDeclaredFields();
                for (int j = 0; j < field.length; j++) {
                    RequestParamChecker check = field[j].getAnnotation(RequestParamChecker.class);
                    if (check != null) {
                        field[j].setAccessible(true);
                        validateFiled(check, field[j], args);
                    }
                }
            } else {
                throw new ParamsValidException("missing required parameter");
            }
        } catch (ParamsValidException e) {
            throw e;
        } catch (Exception e) {
            throw new ParamsValidException("Parameter exception", e);
        }
    }

    /**
     * 校验参数规则
     *
     * @param check
     * @param field 要校验的字段
     * @param args  该字段的值
     * @return
     * @throws Exception
     */
    private static void validateFiled(RequestParamChecker check, Field field, Object args)
            throws ParamsValidException, IllegalAccessException {
        field.setAccessible(true);
        Object value = field.get(args);
        // 获取field长度
        int length = 0;
        String fileName = field.getName();
        if (value != null) {
            length = String.valueOf(value).length();
        }

        // 为空校验
        if (check.notNull()) {
            if (value == null || "".equals(value)) {
                throw new ParamsValidException(fileName + "Can not be empty");
            }
        }

        // 最大长度校验
        if (check.maxLen() > 0 && (length > check.maxLen())) {
            throw new ParamsValidException(fileName + "不能超过" + check.maxLen() + "位");
        }

        // 最小长度校验
        if (check.minLen() > 0 && (length < check.minLen())) {
            throw new ParamsValidException(fileName + "不能少于" + check.minLen() + "位");
        }

        // 长度校验
        if (check.fixLen() > 0 && (length != check.fixLen())) {
            throw new ParamsValidException(fileName + "长度应该为" + check.fixLen() + "位");
        }

        // 数字校验
        if (check.isNum()) {
            BigDecimal v = null;
            try {
                v = new BigDecimal(String.valueOf(value));
            } catch (Exception e) {
                throw new ParamsValidException(fileName + "应该全为数字");
            }

            // 最小值校验
            if (check.minVal() != -999999999) {
                if (v.compareTo(new BigDecimal(check.minVal())) < 0) {
                    throw new ParamsValidException(fileName + "最小值为" + check.minVal());
                }
            }

            // 最大值校验
            if (check.maxVal() != -999999999) {
                if (v.compareTo(new BigDecimal(check.maxVal())) > 0) {
                    throw new ParamsValidException(fileName + "最大值为" + check.maxVal());
                }
            }
        }

        // 正则表达式校验
        String regex = check.pattern();
        if (!"*".equals(regex) && length > 0) {
            Pattern pattern = Pattern.compile(regex);
            Matcher m = pattern.matcher(String.valueOf(value));
            if (!m.matches()) {
                throw new ParamsValidException(fileName + "格式错误");
            }
        }
    }

    /**
     * 根据类和方法名得到方法
     */
    @SuppressWarnings("rawtypes")
    private Method getMethodByClassAndName(Class c, String methodName) throws Exception {
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

}

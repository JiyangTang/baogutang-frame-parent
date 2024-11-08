package com.baogutang.frame.common.utils;


import com.baogutang.frame.common.response.Response;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description: check tool
 * @author: nikooh
 * @date: 2021/12/27 : 2:55 PM
 */
public class ValidateUtil {

    private ValidateUtil() {
        // private empty constructor
    }

    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();

    public static final String PARAM_ERROR = "param can not be null!";

    private static <T> List<String> validate(T t, Class<?>... groups) {
        Validator validator = FACTORY.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t, groups);

        List<String> messageList = new ArrayList<>();
        for (ConstraintViolation<T> constraintViolation : constraintViolations) {
            messageList.add(constraintViolation.getMessage());
        }

        return messageList;
    }

    public static <T> Response<T> validate(T t, String msg, Class<?>... groups) {
        if (t == null) {
            return Response.failed(Response.PARAM_ILLEGAL_CODE, msg);
        }
        List<String> validateList = validate(t, groups);
        if (CollectionUtils.isEmpty(validateList)) {
            return null;
        }
        Set<String> set = new HashSet<>(validateList);
        validateList = new ArrayList<>(set);
        StringBuilder sb = new StringBuilder();
        for (String message : validateList) {
            sb.append(message).append(" ");
        }
        return Response.failed(Response.PARAM_ILLEGAL_CODE, sb.toString());

    }
}

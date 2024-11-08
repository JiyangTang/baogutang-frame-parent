package com.baogutang.frame.common.exception;

import com.baogutang.frame.common.component.DingDingSender;
import com.baogutang.frame.common.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.MDC;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常捕获类
 *
 * @author N1KO
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    private DingDingSender dingDingSender;

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Throwable.class)
    public Response<Object> handleException(Throwable e) {
        log.error("An error occurred in the request, error message::", e);
        return responseResult(Response.FAIL_CODE, "An error occurred, please try again later～");
    }

    @ExceptionHandler({BindException.class})
    public Object methodArgumentNotValidHandler(BindException e) {
        log.error("An error occurred in the request parameter, the error message：", e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder errMsg = new StringBuilder();
        for (FieldError err : fieldErrors) {
            errMsg.insert(0, err.getDefaultMessage() + ";");
        }

        return responseResult(Response.PARAM_ILLEGAL_CODE, errMsg.toString());
    }

    @ExceptionHandler({ParamsValidException.class})
    public Response<Object> businessException(ParamsValidException e) {
        log.error("An error occurred in the request parameter, the error message:", e);
        return responseResult(Response.PARAM_ILLEGAL_CODE, e.getMessage());
    }

    /**
     * 处理所有不可知的系统异常
     */
    @ExceptionHandler(Exception.class)
    public Response<Object> handleException(Exception e) {
        log.error("An error occurred in the request, error message:", e);
        return responseResult(Response.FAIL_CODE, "An error occurred, please try again later～");
    }

    /**
     * 处理客户端链接断开异常
     */
    @ExceptionHandler(ClientAbortException.class)
    public Response<Object> handleClientAbortException() {
        log.error("ClientAbortException: java.io.IOException: Broken pipe: {}", "Client connection broken!");
        return responseResult(Response.FAIL_CODE, "An error occurred, please try again later～");
    }

    /**
     * 统一处理多个异常
     */
    @ExceptionHandler({IllegalArgumentException.class, MissingServletRequestParameterException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpRequestMethodNotSupportedException.class,
            IllegalStateException.class})
    public Response<Object> handleMultiException(Exception e) {
        log.error("An error occurred in the request, error message:", e);
        return responseResult(Response.FAIL_CODE, e.getMessage());
    }


    /**
     * 处理所有的业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Response<Object> businessException(BusinessException e) {
        log.error("An error occurred in the request, error message:code={},message={}", e.getErrorCode(), e.getMessage());
        return responseResult(e.getErrorCode(), e.getMessage());
    }


    @ExceptionHandler(Business2DdException.class)
    public Response<Object> business2DdException(Business2DdException e) {
        log.error("An error occurred in the request, error message:code={},message={}", e.getErrorCode(), e.getMessage());
        return response2DdResult(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(TokenException.class)
    public Response<Object> tokenException(TokenException e) {
        log.warn("token invalid message:code={},message={}", e.getErrorCode(), e.getMessage());
        return responseResult(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Object> parameterExceptionHandler(MethodArgumentNotValidException e) {
        // 获取异常信息
        BindingResult exceptions = e.getBindingResult();
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
        if (exceptions.hasErrors()) {
            List<FieldError> errors = exceptions.getFieldErrors();
            if (!errors.isEmpty()) {
                // 这里列出了全部错误参数，按正常逻辑，只需要第一条错误即可
                String msg = errors.stream()
                        .map(v -> v.getField() + ":" + v.getDefaultMessage())
                        .collect(Collectors.joining(";"));
                return Response.restResult(null, Response.PARAM_ILLEGAL_CODE, msg);
            }
        }
        return responseResult(Response.PARAM_ILLEGAL_CODE, e.getMessage());
    }


    private Response<Object> responseResult(int code, String msg) {
        Response<Object> response = Response.restResult(null, code, msg);
        String rid = MDC.get("X-Request-Id");
        response.setRid(rid);
        return response;
    }

    private Response<Object> response2DdResult(int code, String msg) {
        Response<Object> response = Response.restResult(null, code, msg);
        String rid = MDC.get("X-Request-Id");
        dingDingSender.dingSend(msg + "rid:" + rid);
        response.setRid(rid);
        return response;
    }

}

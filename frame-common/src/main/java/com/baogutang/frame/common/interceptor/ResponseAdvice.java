//package com.baogutang.frame.common.interceptor;
//
//import lombok.extern.slf4j.Slf4j;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//
///**
// * @author N1KO
// */
//@Slf4j
//@ControllerAdvice
//public class ResponseAdvice implements ResponseBodyAdvice<Object> {
//
//    @Override
//    public boolean supports(@NotNull MethodParameter returnType, @NotNull Class converterType) {
//        return true;
//    }
//
//    @Override
//    public Object beforeBodyWrite(Object body, @NotNull MethodParameter returnType, @NotNull MediaType selectedContentType,
//                                  @NotNull Class selectedConverterType, @NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response) {
//        return body;
//    }
//
//}

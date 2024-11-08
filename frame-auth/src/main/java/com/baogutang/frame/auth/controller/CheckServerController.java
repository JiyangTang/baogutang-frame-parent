package com.baogutang.frame.auth.controller;

import com.baogutang.frame.common.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author N1KO
 */
@RestController
@Slf4j
public class CheckServerController {

    @Value(value = "${spring.application.name}")
    private String serverName;

    @GetMapping("/checkServer")
    public Response<?> checkServer() {
        log.info("checkServer success!,serverName:{}", serverName);
        return Response.ok();
    }
}
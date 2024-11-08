package com.baogutang.frame.common.prop;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author N1KO
 */
@Data
@Accessors(chain = true)
@Component
@ConfigurationProperties(prefix = "baogutang.swagger")
public class SwaggerProp {

    private ScanningMode scanningMode = ScanningMode.API_OPERATION;

    private String basePackage;

    @Getter
    @AllArgsConstructor
    public enum ScanningMode {
        /**
         * ScanningMode
         */

        REQUEST_MAPPING("注解RequestMapping", "RequestMapping"),

        API_OPERATION("注解ApiOperation", "ApiOperation"),

        BASE_PACKAGE("包扫描basePackage", "basePackage"),
        ;

        private final String cn;

        private final String en;

    }


}

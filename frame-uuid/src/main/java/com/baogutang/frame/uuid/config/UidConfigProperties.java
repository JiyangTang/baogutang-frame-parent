package com.baogutang.frame.uuid.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "uid")
public class UidConfigProperties {
    private Integer timeBits = 28;
    private Integer workerBits = 22;
    private Integer seqBits = 13;
    private String epochStr = "2021-11-20";
}

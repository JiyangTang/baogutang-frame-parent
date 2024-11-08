package com.baogutang.frame.auth.common.header;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author N1KO
 */
@Data
@ApiModel(value = "统一请求头参数", description = "统一请求头参数")
public class HeadParams {

    @ApiModelProperty(value = "JWT标准token")
    private String token;

    @ApiModelProperty(value = "网络环境")
    private String networkEnv;

    @ApiModelProperty(value = "网络供应商")
    private String networkProvider;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty(value = "ip")
    private String ip;


    public static Map<String, String> buildExtraMap(HeadParams headParams) {
        Map<String, String> extraMap = new HashMap<String, String>(64);
        extraMap.put("latitude", headParams.getLatitude());
        extraMap.put("longitude", headParams.getLongitude());
        extraMap.put("ip", headParams.getIp());
        extraMap.put("networkEnv", headParams.getNetworkEnv());
        extraMap.put("networkProvider", headParams.getNetworkProvider());
        extraMap.put("token", headParams.getToken());
        List<String> notBlankValKeys = extraMap.keySet()
                .stream()
                .filter(key -> StringUtils.isNotEmpty(extraMap.get(key)))
                .collect(Collectors.toList());
        Map<String, String> notBlankExtraMap = new HashMap<>(notBlankValKeys.size());
        for (String key : notBlankValKeys) {
            notBlankExtraMap.put(key, extraMap.get(key));
        }
        return notBlankExtraMap;
    }
}

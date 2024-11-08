package com.baogutang.frame.auth.common.header;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author N1KO
 */

public enum Header {
    /**
     * header
     */
    OS("OS", "操作系统: iOS, Android, Window,Mac"),
    PLATFORM("Platform", "平台：H5, API, APP, Open-API, PC , miniApp"),
    OS_VERSION("OS-Version", "操作系统版本， 可选, 比如 Android,  iOS，Window， Mac 的 版本"),
    APP_VERSION("APP-Version", "App 端build 版本， 客户端 【打包时候】 版本比如 3.3.1 "),
    DEVICE_ID("Device-Id", "设备号， 可选, 唯一标识设备的"),
    TOKEN("X-AUTH-TOKEN", "JWT 标准走， 可选"),
    MOBILE_BRAND("Mobile-Brand", "手机品牌， 可选： Huawei, xiaomi"),
    MOBILE_MODEL("Mobile-Type", "手机品牌对应型号， 可选"),
    NETWORK_ENV("Network-Env", "网络环境：  4G/5G/....， 可选"),
    NETWORK_PROVIDER("Network-Provider", "网络供应商， 可选, CN_MOBILE 等"),
    MEDIA_CHANNEL("Media-Channel", "媒体渠道， 哪里引导下载渠道包的地方！第一次用户注册使用， 不要和发行渠道混淆！他们可能一样也可能不一样"),
    PKG_DELIVERY_CODE(
            "Pkg-Delivery-Code",
            "app 发行渠道， 比如华为应用市场,google play, 应用宝， iOS: app store, IPA, 企业签名等等？  第一次注册  在渠道 App， 或者落地页注册时候用"),
    LANGUAGE("Language", "对应语言， ISO 标准 比如  zh-CN , 本地环境，或者用户配置"),
    OA_ID("Oa-Id", "安卓设备号"),
    LONGITUDE("Longitude", "经度，如121.437866"),
    LATITUDE("Latitude", "纬度，如31.199190"),
    IP("ip", "请求ip"),

    ;

    public static final String HEADER_PREFIX = "X-";

    public final String key;
    public final String upperKey;
    public final String description;
    public final boolean optional;
    public final ErrorCode errorCode;
    public final int scope;
    public final Verify verify;

    Header(String key, String description) {
        this(key, description, true, AllPassVerify.INSTANCE);
    }

    Header(String key, String description, Verify verify) {
        this(key, description, true, verify);
    }

    Header(String key, String description, boolean optional, Verify verify) {
        this(key, description, optional, ErrorCode.NA.NA, verify);
    }

    Header(String key, String description, boolean optional, ErrorCode errorCode, Verify verify) {
        this(key, description, optional, errorCode, Scope.ANY.flag, verify);
    }

    Header(
            String key,
            String description,
            boolean optional,
            ErrorCode errorCode,
            int scope,
            Verify verify) {
        this.key = key;
        this.upperKey = key.toUpperCase();
        this.description = description;
        this.optional = optional;
        this.errorCode = errorCode;
        this.scope = scope;
        this.verify = verify;
    }

    public String getValue() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return ((ServletRequestAttributes) requestAttributes).getRequest().getHeader(key);
        }
        return null;
    }

    private static final Header[] VALS = values();

    public static Header fromOrdinal(int ord) {
        return ord >= 0 && ord < VALS.length ? VALS[ord] : null;
    }
}

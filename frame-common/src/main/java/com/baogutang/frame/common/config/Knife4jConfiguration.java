package com.baogutang.frame.common.config;

import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration implements WebMvcConfigurer {

    // 对于管控了权限的应用，应放行以下资源
    public static String[] RESOURCE_URLS = new String[]{"/webjars/**", "/swagger**", "/v2/api-docs", "/doc.html"};

    @Bean
    public Docket docket() {
        //指定使用Swagger2规范
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(groupApiInfo()).globalOperationParameters(setGlobalOperationParameters())
                .select()
                //这里指定Controller扫描包路径自己的包，不然会是空的
                .apis(RequestHandlerSelectors.basePackage(getProjectBasePackage())
                                .and(RequestHandlerSelectors.withClassAnnotation(RestController.class)
                                        .or(RequestHandlerSelectors.withClassAnnotation(Controller.class))
                                ))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo groupApiInfo() {
        return new ApiInfoBuilder()
                .title("BAOGUTANG - 接口文档")
                .description("<div style='font-size:14px;color:red;'>YS RESTful APIs</div>")
                .termsOfServiceUrl("http://www.group.com/")
                .contact(new Contact("", "", "tnikooh@gmail.com"))
                .version("1.0")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 获取项目包前缀
     */
    private String getProjectBasePackage() {
        String projectBasePackage;
        String currPackageName = this.getClass().getPackage().getName();
        String[] packageItemArr = currPackageName.split("\\.");
        if (packageItemArr.length > 2) {
            projectBasePackage = String.join(".", packageItemArr[0], packageItemArr[1]);
        } else {
            projectBasePackage = currPackageName;
        }
        log.info("Base package to scan api is -> {}", projectBasePackage);
        return projectBasePackage;
    }

    public List<Parameter> setGlobalOperationParameters(){
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new ParameterBuilder().name("X-AUTH-TOKEN")
                .description("TOKEN令牌")
                .modelRef(new ModelRef("string"))
                .required(false)
                .parameterType("header")
                .build());

        parameters.add(new ParameterBuilder().name("ip")
                .description("ip")
                .modelRef(new ModelRef("string"))
                .required(false)
                .parameterType("header")
                .build());

        parameters.add(new ParameterBuilder().name("OS")
                .description("操作系统: iOS, Android, Window,Mac")
                .modelRef(new ModelRef("string"))
                .required(false)
                .parameterType("header")
                .build());

        parameters.add(new ParameterBuilder().name("OS-Version")
                .description("操作系统版本， 可选, 比如 Android,  iOS，Window， Mac 的 版本")
                .modelRef(new ModelRef("string"))
                .required(false)
                .parameterType("header")
                .build());

        parameters.add(new ParameterBuilder().name("PLATFORM")
                .description("平台：H5, API, APP")
                .modelRef(new ModelRef("string"))
                .required(false)
                .parameterType("header")
                .build());

        parameters.add(new ParameterBuilder().name("Device-Id")
                .description("设备号")
                .modelRef(new ModelRef("string"))
                .required(false)
                .parameterType("header")
                .build());

        parameters.add(new ParameterBuilder().name("Mobile-Brand")
                .description("手机品牌， 可选： Huawei, xiaomi")
                .modelRef(new ModelRef("string"))
                .required(false)
                .parameterType("header")
                .build());

        parameters.add(new ParameterBuilder().name("Mobile-Type")
                .description("手机品牌对应型号")
                .modelRef(new ModelRef("string"))
                .required(false)
                .parameterType("header")
                .build());

        parameters.add(new ParameterBuilder().name("Network-Env")
                .description("网络环境：4G/5G")
                .modelRef(new ModelRef("string"))
                .required(false)
                .parameterType("header")
                .build());

        parameters.add(new ParameterBuilder().name("Network-Provider")
                .description("网络供应商")
                .modelRef(new ModelRef("string"))
                .required(false)
                .parameterType("header")
                .build());

        parameters.add(new ParameterBuilder().name("Media-Channel")
                .description("媒体渠道")
                .modelRef(new ModelRef("string"))
                .required(false)
                .parameterType("header")
                .build());

        parameters.add(new ParameterBuilder().name("Pkg-Delivery-Code")
                .description("app 发行渠道google play,app store")
                .modelRef(new ModelRef("string"))
                .required(false)
                .parameterType("header")
                .build());

        parameters.add(new ParameterBuilder().name("Language")
                .description("本机语言：zh-CN")
                .modelRef(new ModelRef("string"))
                .required(false)
                .parameterType("header")
                .build());

        parameters.add(new ParameterBuilder().name("Longitude")
                .description("经度")
                .modelRef(new ModelRef("string"))
                .required(false)
                .parameterType("header")
                .build());

        parameters.add(new ParameterBuilder().name("Latitude")
                .description("纬度")
                .modelRef(new ModelRef("string"))
                .required(false)
                .parameterType("header")
                .build());

        return parameters;
    }
    /**
     * 显示自定义枚举类型注释
     * <p>
     * <br/> 参考<a
     * href="https://blog.gelu.me/2021/Knife4j-Swagger%E8%87%AA%E5%AE%9A%E4%B9%89%E6%9E%9A%E4%B8%BE%E7%B1%BB%E5%9E%8B
     * /">here</a>
     */
    @Component
    @SuppressWarnings("unchecked")
    public static class Knife4jSwaggerEnumPlugin implements ModelPropertyBuilderPlugin, ParameterBuilderPlugin {

        private static final Field parameterDescriptionField;

        private static final Field modelPropertyBuilderDescriptionField;


        static {
            // swagger3用: parameterDescriptionField = ReflectionUtils.findField(RequestParameterBuilder.class, "description");
            parameterDescriptionField = ReflectionUtils.findField(ParameterBuilder.class, "description");
            Objects.requireNonNull(parameterDescriptionField, "parameterDescriptionField should noe be null.");
            ReflectionUtils.makeAccessible(parameterDescriptionField);

            // swagger3用: modelPropertyBuilderDescriptionField = ReflectionUtils.findField(PropertySpecificationBuilder.class, "description");
            modelPropertyBuilderDescriptionField = ReflectionUtils.findField(ModelPropertyBuilder.class, "description");
            Objects.requireNonNull(modelPropertyBuilderDescriptionField, "ModelPropertyBuilder_descriptionField should noe be null.");
            ReflectionUtils.makeAccessible(modelPropertyBuilderDescriptionField);
        }

        /**
         * {@link ApiModelProperty}相关
         * <p>
         * 主要处理枚举对象直接作为方法参数的内部字段的情况. 如：
         * <pre>
         *  &nbsp;  @Data
         *  &nbsp;  public class LoginTokenRespVO {
         *  &nbsp;
         *  &nbsp;      @ApiModelProperty("用户类型")
         *  &nbsp;      private UserTypeEnum userType;
         *  &nbsp;  }
         * </pre>
         */
        @Override
        public void apply(ModelPropertyContext context) {
            Optional<BeanPropertyDefinition> optional = context.getBeanPropertyDefinition();
            if (!optional.isPresent()) {
                return;
            }
            // 对应被@ApiModelProperty标注的字段
            BeanPropertyDefinition beanPropertyDefinition = optional.get();
            Class<?> fieldType = beanPropertyDefinition.getField().getRawType();
            if (!Enum.class.isAssignableFrom(fieldType)) {
                return;
            }
            Class<Enum<?>> enumType = (Class<Enum<?>>) fieldType;
            Enum<?>[] enumConstants = enumType.getEnumConstants();
            // swagger3用: PropertySpecificationBuilder modelPropertyBuilder = context.getSpecificationBuilder();
            ModelPropertyBuilder modelPropertyBuilder = context.getBuilder();
            Object oldValue = ReflectionUtils.getField(modelPropertyBuilderDescriptionField, modelPropertyBuilder);
            // 解析枚举
            List<String> enumDescList =
                    Arrays.stream(enumConstants).map(this::obtainEnumDescription).collect(Collectors.toList());
            /*
             * swagger3用:
             *   modelPropertyBuilder.description((oldValue == null ? BaseConstant.EMPTY : oldValue) + buildHtmlUnOrderList(enumDescList))
             *                       .type(new ModelSpecificationBuilder().scalarModel(ScalarType.UUID).build());
             */
            modelPropertyBuilder.description((oldValue == null ? "" : oldValue) + buildHtmlUnOrderList(enumDescList))
                    .type(context.getResolver().resolve(Enum.class));
        }

        /**
         * {@link ApiParam}、{}相关.
         * <p> 主要处理：枚举对象直接作为方法参数的情况. 如：
         * <pre>
         *  &nbsp;  @PostMapping("/test1")
         *  &nbsp;  @ApiOperation(value = "测试1")
         *  &nbsp;  public void test1(@ApiParam(value = "用户类型", required = true) UserTypeEnum userTypeEnum)
         * </pre>
         */
        @Override
        public void apply(ParameterContext context) {
            Class<?> type = context.resolvedMethodParameter().getParameterType().getErasedType();
            // swagger3用: RequestParameterBuilder parameterBuilder = context.requestParameterBuilder();
            ParameterBuilder parameterBuilder = context.parameterBuilder();
            if (!Enum.class.isAssignableFrom(type)) {
                return;
            }
            Class<Enum<?>> enumType = (Class<Enum<?>>) type;
            Enum<?>[] enumConstants = enumType.getEnumConstants();
            // 解析枚举
            List<String> enumDescList = Arrays.stream(enumConstants).map(this::obtainEnumDescription).collect(Collectors.toList());
            Object oldValue = ReflectionUtils.getField(parameterDescriptionField, parameterBuilder);
            parameterBuilder.description((oldValue == null ? "" : oldValue) + buildHtmlUnOrderList(enumDescList));
        }

        /**
         * 此插件是否支持处理该DocumentationType
         */
        @Override
        public boolean supports(DocumentationType documentationType) {
            return true;
        }

        /**
         * 获取枚举描述
         *
         * @param enumObj 枚举对象
         *
         * @return 枚举描述
         */
        private String obtainEnumDescription(@NonNull Enum<?> enumObj) {
            /*
             * 枚举说明器示例:
             *
             * public interface EnumDescriptor {
             *     // 获取枚举项说明
             *     String obtainDescription();
             * }
             */
//            if (enumObj instanceof EnumDescriptor) {
//                return name + "：" + ((EnumDescriptor) enumObj).obtainDescription();
//            }
            return enumObj.name();
        }

        /**
         * 构建无序列表html
         *
         * @param itemList 列表元素
         *
         * @return 无序列表html
         */
        private String buildHtmlUnOrderList(@Nullable List<String> itemList) {
            if (CollectionUtils.isEmpty(itemList)) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("<ul>");
            for (String item : itemList) {
                sb.append("<li>");
                sb.append(item);
                sb.append("</li>");
            }
            sb.append("</ul>");
            return sb.toString();
        }
    }
}

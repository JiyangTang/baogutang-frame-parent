package com.baogutang.frame.common.annotation;

import java.lang.annotation.*;

/**
 * 此注解仅限于开关属性赋值使用，具体逻辑参考 c端 resource GlobalSwitchSetting 类
 * 暂时不支持对象套用的赋值
 * @author
 */

@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface SwitchSettings {

    boolean down() default false;
}
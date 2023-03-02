package com.newlandnpt.varyar.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 位置守护
 * @author ljx
 * @date 2023/3/2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LocationGuard {

    String value();

    String name();
}

/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.gongshw.inout4j.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gongshiwei
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface In {
}

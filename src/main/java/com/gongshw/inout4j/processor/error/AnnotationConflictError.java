/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.gongshw.inout4j.processor.error;

import java.util.Arrays;

import javax.lang.model.element.Element;

/**
 * @author gongshiwei
 */
public class AnnotationConflictError extends CompileError{
    public AnnotationConflictError(Element element, Class<?> ... annotations){
        super(element,"conflict annotations: %s", Arrays.toString(annotations));
    }
}

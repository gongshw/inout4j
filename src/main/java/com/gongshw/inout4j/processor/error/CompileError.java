/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.gongshw.inout4j.processor.error;

import javax.lang.model.element.Element;

/**
 * @author gongshiwei
 */
public class CompileError extends Error {

    CompileError(Element element, String message, Object... args) {
        super(String.format("compile error: [" + element + "]" + message, args));
    }
}

/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.gongshw.inout4j.processor;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.gongshw.inout4j.annotation.ReadOnly;
import com.gongshw.inout4j.annotation.In;
import com.gongshw.inout4j.annotation.Writable;
import com.gongshw.inout4j.annotation.Out;
import com.google.auto.service.AutoService;

/**
 * @author gongshiwei
 */
@AutoService(Processor.class)
public class ConflictCheckProcessor extends BaseInout4jProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        checkConflict(env, ElementKind.METHOD, ReadOnly.class, Writable.class);
        checkConflict(env, ElementKind.PARAMETER, In.class, Out.class);
        return false;
    }

    private void checkConflict(RoundEnvironment env, ElementKind elementKind,
                               Class<? extends Annotation> a1,
                               Class<? extends Annotation> a2) {
        env.getElementsAnnotatedWith(a1).stream()
                .filter(element -> element.getKind() == elementKind)
                .filter(element -> element.getAnnotation(a2) != null)
                .forEach(element -> {
                    String message = String.format("conflicted annotation: [%s, %s]", a1, a2);
                    messager.printMessage(Diagnostic.Kind.ERROR, message, element);
                });
    }
}

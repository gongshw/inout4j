/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.gongshw.inout4j.processor;

import java.io.PrintStream;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import com.gongshw.inout4j.annotation.Const;
import com.gongshw.inout4j.annotation.NonConst;
import com.gongshw.inout4j.processor.error.AnnotationConflictError;

/**
 * @author gongshiwei
 */
@SupportedAnnotationTypes({
        "com.gongshw.inout4j.annotation.Const",
        "com.gongshw.inout4j.annotation.NonConst",
        "com.gongshw.inout4j.annotation.In",
        "com.gongshw.inout4j.annotation.Out",
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class Inout4jProcessor extends AbstractProcessor {

    private boolean enableDebug = true;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        enableDebug = Boolean.parseBoolean(System.getProperty("inout4j.debug"));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        for (Element element:env.getElementsAnnotatedWith(Const.class)){
            if (element.getKind() == ElementKind.METHOD){
                ExecutableElement method = ExecutableElement.class.cast(element);
                if (method.getAnnotation(NonConst.class)!=null){
                    throw new AnnotationConflictError(element,NonConst.class,Const.class);
                }
            }
        }
        return false;
    }

    private void debug(String message, Object... args) {
        if (enableDebug) {
            PrintStream out = System.out;
            out.print(getClass().getName() + ": ");
            out.printf(message, args);
            out.println();
        }
    }
}

/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.gongshw.inout4j.processor;

import java.io.PrintStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import com.gongshw.inout4j.annotation.Const;
import com.gongshw.inout4j.annotation.In;
import com.gongshw.inout4j.annotation.NonConst;
import com.gongshw.inout4j.annotation.Out;
import com.sun.source.util.Trees;

/**
 * @author gongshiwei
 */
public abstract class BaseInout4jProcessor extends AbstractProcessor {

    protected Messager messager;

    protected Elements elementUtils;

    protected Types typeUtils;

    protected Trees trees;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        messager = env.getMessager();
        elementUtils = env.getElementUtils();
        typeUtils = env.getTypeUtils();
        trees = Trees.instance(env);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>(4);
        set.add(In.class.getName());
        set.add(Out.class.getName());
        set.add(Const.class.getName());
        set.add(NonConst.class.getName());
        return Collections.unmodifiableSet(set);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public abstract boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env);
}

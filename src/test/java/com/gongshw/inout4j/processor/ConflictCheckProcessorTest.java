/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.gongshw.inout4j.processor;

import static com.google.testing.compile.CompilationSubject.assertThat;

import org.junit.Test;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;

/**
 * @author gongshiwei
 */
public class ConflictCheckProcessorTest {
    @Test
    public void process() throws Exception {
        Compilation compilation = Compiler.javac().withProcessors(new ConflictCheckProcessor())
                .compile(JavaFileObjects.forResource("com/gongshw/inout4j/test/ConflictAnnotation.java"));
        compilation.errors().forEach(System.err::println);
        assertThat(compilation).hadErrorCount(2);
    }
}

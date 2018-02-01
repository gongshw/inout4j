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
public class ConstMethodCheckTest {
    @Test
    public void process() throws Exception {
        Compilation compilation = Compiler.javac().withProcessors(new ReadOnlyMethodCheckProcessor())
                .compile(JavaFileObjects.forResource("com/gongshw/inout4j/test/ConstChangeField.java"));
        compilation.errors().forEach(System.err::println);
        assertThat(compilation).hadErrorCount(4);
    }
}

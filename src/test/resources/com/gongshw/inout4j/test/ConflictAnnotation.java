/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.gongshw.inout4j.test;

import com.gongshw.inout4j.annotation.In;
import com.gongshw.inout4j.annotation.Out;
import com.gongshw.inout4j.annotation.ReadOnly;
import com.gongshw.inout4j.annotation.Writable;

class ConflictAnnotation {
    @ReadOnly
    @Writable // error
    private void confilict() {
    }

    @ReadOnly
    private void readOnly() {
    }

    @Writable // error
    private void writable() {
    }

    private void confilict(@In @Out Object param) { // error
    }

    private void in(@In Object param) { // error
    }

    private void out(@In Object param) { // error
    }
}

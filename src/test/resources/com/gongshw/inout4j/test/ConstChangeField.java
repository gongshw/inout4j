/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.gongshw.inout4j.test;

import com.gongshw.inout4j.annotation.ReadOnly;
import com.gongshw.inout4j.annotation.Writable;

class ConstChangeField {
    private Object field = null;

    @ReadOnly
    private void update() {
        new Runnable() {
            @Override
            public void run() {
                this.field = new Object(); // error
            }
        };
    }

    @Writable
    private void update(int param) {
    }

    @ReadOnly
    private Object get() {
        System.out.print("get something");
        this.update(0); // error
        update(0); // error
        return this.field;
    }

    @Writable
    public void set() {

    }

    @ReadOnly
    private void getInvokeSet() {
        this.set(0); // error
    }
}

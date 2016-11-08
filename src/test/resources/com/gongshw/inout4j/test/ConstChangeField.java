/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.gongshw.inout4j.test;

import com.gongshw.inout4j.annotation.Const;

class ConstChangeField {
    private Object field = null;

    @Const
    private void update() {
        new Runnable() {
            @Override
            public void run() {
                this.field = new Object(); // error
            }
        };
    }

    @Const
    private Object get() {
        this.update(); // error
        return this.field;
    }
}

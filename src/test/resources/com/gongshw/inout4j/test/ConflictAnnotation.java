/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.gongshw.inout4j.test;

import com.gongshw.inout4j.annotation.Const;
import com.gongshw.inout4j.annotation.In;
import com.gongshw.inout4j.annotation.NonConst;
import com.gongshw.inout4j.annotation.Out;

class ConflictAnnotation {
    @NonConst
    @Const // error
    private void confilict(){
    }

    @Const
    private void confilict(@In @Out Object param){ // error
    }
}

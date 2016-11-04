/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.gongshw.inout4j.annotation;

import org.junit.Test;

/**
 * @author gongshiwei
 */
public class CompileUnitTest {

    private class updateResult{
        @NonConst
        public void update(){

        }
        @Const
        public void getValue(){

        }
    }

    private class UpdateParam{
        @NonConst
        public void update(){

        }
        @Const
        public void getValue(){

        }
    }

    @NonConst
    private void update(@In UpdateParam updateParam, @Out updateResult updateResult){
        updateParam.update(); // error!
    }

    @NonConst
    @Const // error
    private void confilict(){
    }

    private void getValue(){
        update(null,null); // error!
    }

    @Test
    public void test(){
    }
}

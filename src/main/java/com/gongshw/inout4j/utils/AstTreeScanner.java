/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.gongshw.inout4j.utils;

import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;

/**
 * @author gongshiwei
 */
public abstract class AstTreeScanner extends TreePathScanner<Void, Void> {
    public void scan(TreePath treePath) {
        super.scan(treePath, null);
    }

    public void scan(Tree tree) {
        super.scan(tree, null);
    }
}

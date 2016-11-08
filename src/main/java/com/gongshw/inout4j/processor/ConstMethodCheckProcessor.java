/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.gongshw.inout4j.processor;

import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import com.gongshw.inout4j.annotation.Const;
import com.gongshw.inout4j.utils.AstTreeScanner;
import com.sun.codemodel.internal.ClassType;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;

/**
 * @author gongshiwei
 */
public class ConstMethodCheckProcessor extends BaseInout4jProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        env.getElementsAnnotatedWith(Const.class).stream()
                .filter(element -> element.getKind() == ElementKind.METHOD)
                .forEach(this::checkConstMethod);
        return false;
    }

    private void checkConstMethod(Element e) {
        ExecutableElement methodElement = (ExecutableElement) e;
        if (!methodElement.getModifiers().contains(Modifier.ABSTRACT)) {
            TreePath treePath = trees.getPath(methodElement);
            checkAssignThis(treePath);
            checkUseNonConst(treePath);
        }
    }

    private void checkAssignThis(TreePath treePath) {
        new AstTreeScanner() {
            public Void visitAssignment(AssignmentTree tree, Void aVoid) {
                new AstTreeScanner() {
                    @Override
                    public Void visitIdentifier(IdentifierTree identifierTree, Void aVoid) {
                        if (identifierTree.getName().contentEquals("this")) {
                            Element assignElement = trees.getElement(treePath);
                            String msg = "@Const applied on an illegal method";
                            messager.printMessage(Diagnostic.Kind.ERROR, msg, assignElement);
                        }
                        return null;
                    }
                }.scan(getCurrentPath());
                return null;
            }
        }.scan(treePath);
    }

    private void checkUseNonConst(TreePath treePath) {
        new AstTreeScanner() {
            @Override
            public Void visitMethodInvocation(MethodInvocationTree methodInvocationTree, Void aVoid) {
                ExpressionTree methodSelect = methodInvocationTree.getMethodSelect();
                boolean isThisMethod = false;
                String methodName = null;
                if (methodSelect.getKind() == Tree.Kind.IDENTIFIER) {
                    isThisMethod = true;
                    methodName = ((IdentifierTree) methodSelect).getName().toString();
                } else if (methodSelect.getKind() == Tree.Kind.MEMBER_SELECT) {
                    MemberSelectTree memberSelectTree = (MemberSelectTree) methodSelect;
                    methodName = memberSelectTree.getIdentifier().toString();
                    ExpressionTree expression = memberSelectTree.getExpression();
                    if (expression.getKind() == Tree.Kind.IDENTIFIER) {
                        isThisMethod = ((IdentifierTree) expression).getName().contentEquals("this");
                    }
                }
                if (isThisMethod) {
                    System.out.println(methodName);
                    TypeElement typeElement = getParentClass(treePath);
                    TypeMirror typeMirror = typeElement.asType();
                    System.out.println(typeMirror);
                }
                return null;
            }
        }.scan(treePath);
    }

    private TypeElement getParentClass(TreePath treePath){
        TreePath current = treePath;
        while (current!=null){
            if (current.getLeaf().getKind()== Tree.Kind.CLASS){
                return (TypeElement) trees.getElement(current);
            }
            current = current.getParentPath();
        }
        throw new AssertionError();
    }
}

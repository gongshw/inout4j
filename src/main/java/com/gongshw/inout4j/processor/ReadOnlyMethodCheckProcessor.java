/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.gongshw.inout4j.processor;

import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.gongshw.inout4j.annotation.ReadOnly;
import com.gongshw.inout4j.annotation.Writable;
import com.gongshw.inout4j.utils.AstTreeScanner;
import com.google.auto.service.AutoService;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.util.Name;

/**
 * @author gongshiwei
 */
@AutoService(Processor.class)
public class ReadOnlyMethodCheckProcessor extends BaseInout4jProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        env.getElementsAnnotatedWith(ReadOnly.class).stream()
                .filter(element -> element.getKind() == ElementKind.METHOD)
                .forEach(this::checkConstMethod);
        return false;
    }

    private void checkConstMethod(Element e) {
        ExecutableElement methodElement = (ExecutableElement) e;
        if (!methodElement.getModifiers().contains(Modifier.ABSTRACT)) {
            TreePath treePath = trees.getPath(methodElement);
            checkAssignThis(treePath);
            checkUseWritable(treePath);
        }
    }

    private void checkAssignThis(TreePath treePath) {
        new AstTreeScanner() {
            public Void visitAssignment(AssignmentTree tree, Void aVoid) {
                new AstTreeScanner() {
                    @Override
                    public Void visitIdentifier(IdentifierTree identifierTree, Void aVoid) {
                        if (identifierTree.getName().contentEquals("this")) {
                            CompilationUnitTree root = treePath.getCompilationUnit();
                            String msg = "@ReadOnly method assign this";
                            trees.printMessage(Diagnostic.Kind.ERROR, msg, tree, root);
                        }
                        return null;
                    }
                }.scan(getCurrentPath());
                return null;
            }
        }.scan(treePath);
    }

    private void checkUseWritable(TreePath treePath) {
        new AstTreeScanner() {
            @Override
            public Void visitMethodInvocation(MethodInvocationTree methodInvocationTree, Void aVoid) {
                CompilationUnitTree root = treePath.getCompilationUnit();
                ExpressionTree methodSelect = methodInvocationTree.getMethodSelect();
                boolean isThisMethod = false;
                Name methodName = null;
                if (methodSelect.getKind() == Tree.Kind.IDENTIFIER) {
                    isThisMethod = true;
                    methodName = (Name) ((IdentifierTree) methodSelect).getName();
                } else if (methodSelect.getKind() == Tree.Kind.MEMBER_SELECT) {
                    MemberSelectTree memberSelectTree = (MemberSelectTree) methodSelect;
                    methodName = (Name) memberSelectTree.getIdentifier();
                    ExpressionTree expression = memberSelectTree.getExpression();
                    if (expression.getKind() == Tree.Kind.IDENTIFIER) {
                        isThisMethod = ((IdentifierTree) expression).getName().contentEquals("this");
                    }
                }
                if (!isThisMethod) {
                    return null;
                }
                if (methodName.toString().startsWith("set")) {
                    String msg = "@ReadOnly method invoke a setter method";
                    trees.printMessage(Diagnostic.Kind.ERROR, msg, methodInvocationTree, root);
                    return null;
                }
                Symbol.ClassSymbol typeElement = (Symbol.ClassSymbol) getParentClass(treePath);
                Iterable<Symbol> symbols = typeElement.members().getElementsByName(
                        methodName, e -> e.getKind() == ElementKind.METHOD
                );
                Symbol.MethodSymbol symbol = (Symbol.MethodSymbol) symbols.iterator().next();
                for (Attribute.Compound compound : symbol.getAnnotationMirrors()) {
                    String annotationName = compound.getAnnotationType().toString();
                    if (annotationName.equals(Writable.class.getName())) {
                        String msg = "@ReadOnly method invoke a @Writable method";
                        trees.printMessage(Diagnostic.Kind.ERROR, msg, methodInvocationTree, root);
                    }
                }
                return null;
            }
        }.scan(treePath);
    }

    private TypeElement getParentClass(TreePath treePath) {
        TreePath current = treePath;
        while (current != null) {
            if (current.getLeaf().getKind() == Tree.Kind.CLASS) {
                return (TypeElement) trees.getElement(current);
            }
            current = current.getParentPath();
        }
        throw new AssertionError();
    }
}

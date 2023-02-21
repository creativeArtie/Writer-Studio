package com.creativeartie.humming.document;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

import com.google.common.base.*;

public abstract class DivisionTestBase<T extends Division> extends SpanTestBase<SpanBranch> {
    private Class<T> checkClass;

    public DivisionTestBase(boolean testIdx, Class<T> used) {
        super(testIdx);
        checkClass = used;
    }

    protected abstract Executable testChild(int index, Object expect, T child);

    protected class TestChild {
        private ArrayList<TestChild> children;
        private int index;
        private Class<?> spanClass;
        private int childrenSize;
        private String spanName;
        private Object[] spanData;

        protected TestChild(int i, Class<?> clazz, int size, String name, Object... data) {
            children = new ArrayList<>();
            spanClass = clazz;
            childrenSize = size;
            index = i;
            spanName = name;
            spanData = data;
        }

        protected TestChild newChild(int index, Class<?> childClass, int size, String name, Object... data) {
            Preconditions.checkElementIndex(index, childrenSize);
            TestChild child = new TestChild(index, childClass, size, name, data);
            children.add(child);
            return child;
        }

        private void test(SpanParent test) {
            List<Executable> tests = new ArrayList<>();

            if (checkClass.isInstance(test)) {
                T tested = checkClass.cast(test);
                int i = 0;

                for (Object data : spanData) {
                    tests.add(testChild(i, data, tested));
                    i++;
                }
            }
            tests.add(() -> Assertions.assertEquals(childrenSize, test.size(), "child size"));
            tests.add(() -> Assertions.assertInstanceOf(spanClass, test, "child class"));
            Assertions.assertAll(spanName, tests.toArray(new Executable[tests.size()]));

            // Get children
            if (test instanceof Document) {
                Document doc = (Document) test;

                for (TestChild child : children) {
                    Division div = doc.get(child.index);
                    child.test(div);
                }
            } else {
                SpanBranch branch = (SpanBranch) test;

                for (TestChild child : children) {
                    Span span = branch.get(child.index);
                    Assertions.assertInstanceOf(SpanBranch.class, span);
                    child.test((SpanBranch) span);
                }
            }
        }
    }

    protected void testChildren() {
        Document doc = getDocument();
        printChild("", rootChild);
        System.out.println();
        printChild("", doc, "");
        System.out.println("\n");

        rootChild.test(doc);
    }

    private void printChild(String tab, TestChild child) {
        String format = "%s%s(%s) => index: %d, size: %d\n";
        System.out
                .printf(format, tab, child.spanName, child.spanClass.getSimpleName(), child.index, child.childrenSize);

        for (TestChild ch : child.children) {
            printChild(tab + "=", ch);
        }
    }

    private static char replaced = '-';

    private void printChild(String tab, SpanParent span, String index) {
        String name = span.getClass().getSimpleName();

        switch (replaced) {
            case '-':
                replaced = '.';
                break;
            case '.':
                replaced = '-';
        }
        String aTab = tab.replace(' ', (char) (' ' + tab.length()));
        String full = "%-20s".formatted(aTab + (name == "" ? "LineSpan" : name)).replace(' ', replaced);

        String format = "%-20s %s size: %d, index: %s\n";
        String isDiv = (span instanceof Division) || (span instanceof Document) ? "++++" : " => ";
        System.out.printf(format, full, isDiv, span.size(), index);
        int i = 0;

        if (isDiv == " => ") {
            return;
        }

        if (span instanceof Document) {

            for (Division child : (Document) span) {
                printChild(tab + " ", child, Integer.toString(i));
                i++;
            }
        } else if (span instanceof SpanBranch) {

            for (Span child : (SpanBranch) span) {

                if (child instanceof SpanBranch) {
                    printChild(tab + " ", (SpanBranch) child, index + ":" + Integer.toString(i));
                }
                i++;
            }
        }
    }

    private TestChild rootChild;

    protected TestChild addChild(int index, Class<?> childClass, int childrenSize, String name, Object... data) {
        rootChild.childrenSize += 1;
        return rootChild.newChild(index, childClass, childrenSize, name, data);
    }

    @Override
    protected void moreBeforeEach() {
        rootChild = new TestChild(0, Document.class, 0, "root");
    }

    protected void newDoc(String input) {
        getDocument().updateText(input);

        for (SpanBranch child : getDocument()) {
            printSpan(child, input);
        }
    }
}

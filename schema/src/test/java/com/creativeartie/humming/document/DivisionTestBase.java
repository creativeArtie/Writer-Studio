package com.creativeartie.humming.document;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

@SuppressWarnings("nls")
abstract class DivisionTestBase<T extends Division> extends SpanTestBase<SpanBranch> {
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
        private boolean isReady[];

        private TestChild(String name, int idx) {
            spanName = name;
            children = new ArrayList<>();
            index = idx;
            isReady = new boolean[] { false, false, false };
        }

        protected TestChild setClass(Class<?> clazz) {
            spanClass = clazz;
            isReady[0] = true;
            return this;
        }

        protected TestChild setSize(int size) {
            childrenSize = size;
            isReady[1] = true;
            return this;
        }

        protected TestChild setData(Object... expect) {
            spanData = expect;
            isReady[2] = true;
            return this;
        }

        protected TestChild newChildAtIndex(String name, int idx) {
            TestChild child = new TestChild(name, idx);
            children.add(child);
            return child;
        }

        private void test(SpanParent test) {
            List<Executable> tests = new ArrayList<>();

            if (checkClass.isInstance(test)) {
                T tested = checkClass.cast(test);
                int i = 0;
                assert isReady[2] == true : "Data not set";

                for (Object data : spanData) {
                    tests.add(testChild(i, data, tested));
                    i++;
                }
            }

            assert isReady[1] == true : "Child size not set";
            tests.add(() -> Assertions.assertEquals(childrenSize, test.size(), "child size"));

            assert isReady[0] == true : "Child class not set";
            tests.add(() -> Assertions.assertInstanceOf(spanClass, test, "child class"));
            Assertions.assertAll(spanName, tests.toArray(new Executable[tests.size()]));

            // Get children
            if (test instanceof Manuscript) {
                Manuscript doc = (Manuscript) test;

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
        Manuscript doc = getDocument();
        printChild("", rootChild);
        System.out.println();

        rootChild.test(doc);
    }

    @AfterEach
    private void moreTest() {
        int idx = 0;
        Manuscript root = getDocument();
        printSpanFormatted(root, "");
        for (DivisionSec child : root) {
            Optional<SpanParent> parent = child.getParent();
            parent.ifPresent((span) -> {
                if (span != root) {
                    printSpanFormatted(root, "");
                    printSpanFormatted(span, "");
                }
            });
            Assertions.assertAll(
                    "Child at" + Integer.toString(idx), () -> Assertions.assertTrue(parent.isPresent()),
                    () -> Assertions.assertSame(root, parent.get())
            );
            idx++;
            testParent(child, Integer.toString(idx));
        }
    }

    private void printSpanFormatted(Span span, String tabs) {
        String baseTab = "    ";
        if (span instanceof Manuscript) {
            Manuscript doc = (Manuscript) span;
            System.out.println("{");
            for (DivisionSec child : doc) {
                printSpanFormatted(child, baseTab);
            }
            System.out.println("}");

        } else if (span instanceof Division) {
            String name = span.getClass().getSimpleName();
            System.out.println(tabs + name + " [");
            for (Span child : (Division) span) {
                printSpanFormatted(child, tabs + baseTab);
            }
            System.out.println(tabs + "] //" + name);

        } else {
            System.out.println(tabs + span.toString());
        }
    }

    private void testParent(SpanBranch test, String name) {
        int idx = 0;
        for (Span child : test) {
            Optional<SpanParent> parent = child.getParent();
            Assertions.assertAll(
                    "Child at" + name + ":" + Integer.toString(idx), () -> Assertions.assertTrue(parent.isPresent()),
                    () -> Assertions.assertSame(test, parent.get())
            );
            if (child instanceof SpanBranch) {
                testParent((SpanBranch) child, name + ":" + Integer.toString(idx));
            }
            idx++;
        }
    }

    private void printChild(String tab, TestChild child) {
        String format = "%s%s(%s) => index: %d, size: %d\n";
        System.out
                .printf(format, tab, child.spanName, child.spanClass.getSimpleName(), child.index, child.childrenSize);

        for (TestChild ch : child.children) {
            printChild(tab + "=", ch);
        }
    }

    private TestChild rootChild;

    protected TestChild newChildAtIndex(String name, int idx) {
        rootChild.childrenSize += 1;
        return rootChild.newChildAtIndex(name, idx);
    }

    @Override
    protected void moreBeforeEach() {
        rootChild = new TestChild("root", 0).setSize(0).setClass(Manuscript.class);
    }

    protected void newDoc(String input) {
        getDocument().updateText(input);

        for (SpanBranch child : getDocument()) {
            printSpan(child, input);
        }
        Assertions.assertEquals(input, getDocument().getText(), "Manuscript Text");
    }
}

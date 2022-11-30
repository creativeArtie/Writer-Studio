package com.creativeartie.writer.writing;

import java.util.*;

import org.junit.jupiter.api.*;

import com.creativeartie.writer.writing.LinePhrase.*;

public class ChildSpanTester {
    public abstract class ChildSpanExecutor<T extends Span> {
        private final Class<T> spanClass;

        ChildSpanExecutor(Class<T> span) {
            spanClass = span;
        }

        private void prepSpan(Span child, int idx) {
            Assertions.assertEquals(spanClass, child.getClass(), "Wrong class");
            testSpan(spanClass.cast(child), idx);
        }

        abstract void testSpan(T child, int idx);
    }

    private ArrayList<ChildSpanExecutor<?>> testers;

    public ChildSpanTester() {
        testers = new ArrayList<>();
    }

    public ChildSpanTester addTextSpan(String text, boolean... formats) {
        testers.add(
            new ChildSpanTester.ChildSpanExecutor<TextSpan>(TextSpan.class) {
                @Override
                void testSpan(TextSpan child, int idx) {
                    Assertions.assertAll(
                        () -> Assertions.assertEquals(
                            text, child.getText(), "text " + Integer.toString(
                                idx
                            )
                        ), () -> Assertions.assertArrayEquals(
                            formats, child.getFormats(), "formats " + Integer
                                .toString(idx)
                        )
                    );
                }
            }
        );
        return this;
    }

    public ChildSpanTester addTodoSpan(String todo) {
        testers.add(
            new ChildSpanTester.ChildSpanExecutor<TodoPhrase>(
                TodoPhrase.class
            ) {
                @Override
                void testSpan(TodoPhrase child, int idx) {
                    Assertions.assertEquals(
                        todo, child.getTodoText(), "Todo Text"
                    );
                }
            }
        );
        return this;
    }

    public void test(SpanBranch branch) {
        Assertions.assertEquals(testers.size(), branch.size(), "Children size");
        Iterator<ChildSpanExecutor<?>> it = testers.iterator();
        int i = 0;
        for (Span child : branch) {
            it.next().prepSpan(child, i);
            i++;
        }
    }
}

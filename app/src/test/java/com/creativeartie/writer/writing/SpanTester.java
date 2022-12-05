package com.creativeartie.writer.writing;

import java.util.*;

import org.junit.jupiter.api.*;

import com.creativeartie.writer.writing.LinePhrase.*;
import com.google.common.base.*;

public class SpanTester {
    public static abstract class ChildSpanExecutor<T extends Span> {
        private final Class<T> spanClass;

        ChildSpanExecutor(Class<T> span) {
            spanClass = span;
        }

        private void prepSpan(Span child) {
            Assertions.assertEquals(spanClass, child.getClass(), "Wrong class");
            testSpan(spanClass.cast(child));
        }

        abstract void testSpan(T child);
    }

    private ArrayList<ChildSpanExecutor<?>> testers;

    public SpanTester() {
        testers = new ArrayList<>();
    }

    public SpanTester addTextSpan(String text, boolean... formats) {
        testers.add(createTextSpanTester(text, formats));
        return this;
    }

    protected static ChildSpanExecutor<TextSpan>
        createTextSpanTester(String text, boolean... formats) {
        return new ChildSpanExecutor<TextSpan>(TextSpan.class) {
            @Override
            void testSpan(TextSpan child) {
                Assertions.assertAll(
                    () -> Assertions
                        .assertEquals(text, child.getText(), "text "),
                    () -> Assertions.assertArrayEquals(
                        formats, child.getFormats(), "formats "
                    )
                );
            }
        };
    }

    public SpanTester addTodoSpan(String todo) {
        testers.add(createTodoPhraseTester(todo));
        return this;
    }

    protected static ChildSpanExecutor<IdMarkerPhrase> createIdMarkerTester(
        IdTypes idType, boolean isId, List<String> idCategories, String idName
    ) {
        return new ChildSpanExecutor<IdMarkerPhrase>(IdMarkerPhrase.class) {
            @Override
            void testSpan(IdMarkerPhrase child) {
                Assertions.assertAll(
                    () -> Assertions
                        .assertEquals(idType, child.getIdType(), "type"),
                    () -> {
                        if (isId) {
                            Assertions.assertTrue(child.isId(), "isId");
                            Assertions.assertFalse(child.isRef(), "isRef");
                        } else {
                            Assertions.assertTrue(child.isRef(), "isRef");
                            Assertions.assertFalse(child.isId(), "isId");
                        }
                    },
                    () -> Assertions.assertArrayEquals(
                        idCategories.toArray(), child.getCategories().toArray(),
                        "categories"
                    ),
                    () -> Assertions
                        .assertEquals(idName, child.getName(), "name"),
                    () -> Assertions.assertEquals(
                        buildId(idCategories, idName), child.getId()
                    )
                );
            }
        };
    }

    protected static ChildSpanExecutor<TodoPhrase>
        createTodoPhraseTester(String todo) {
        return new SpanTester.ChildSpanExecutor<TodoPhrase>(TodoPhrase.class) {
            @Override
            void testSpan(TodoPhrase child) {
                Assertions.assertEquals(todo, child.getTodoText(), "Todo Text");
            }
        };
    }

    protected static ChildSpanExecutor<IdRefPhrase> createIdRefPhraseTest(
        IdTypes idType, boolean isId, List<String> idCategories, String idName
    ) {
        return new ChildSpanExecutor<IdRefPhrase>(IdRefPhrase.class) {

            @Override
            void testSpan(IdRefPhrase child) {
                Assertions.assertAll(
                    () -> createIdMarkerTester(
                        idType, isId, idCategories, idName
                    ).testSpan(child.getId()),
                    () -> Assertions.assertEquals(
                        buildId(idCategories, idName), child.getId().getId()
                    ), () -> Assertions.assertEquals(idType, child.getType())
                );

            }

        };
    }

    protected static ChildSpanExecutor<BasicText.EscapeText>
        createEscapeText(char expect) {
        return new ChildSpanExecutor<BasicText.EscapeText>(
            BasicText.EscapeText.class
        ) {

            @Override
            void testSpan(BasicText.EscapeText child) {
                Assertions.assertEquals(expect + "", child.getText());

            }

        };
    }

    protected SpanTester addEscapeText(char expect) {
        testers.add(createEscapeText(expect));
        return this;
    }

    protected SpanTester addAtomicTest(String expect) {
        testers.add(createAtomicText(expect));
        return this;
    }

    protected static ChildSpanExecutor<BasicText.AtomicText>
        createAtomicText(String expect) {
        return new ChildSpanExecutor<BasicText.AtomicText>(
            BasicText.AtomicText.class
        ) {

            @Override
            void testSpan(BasicText.AtomicText child) {
                Assertions.assertEquals(expect, child.getText());
            }

        };
    }

    private static String buildId(List<String> idCategories, String idName) {
        String id = Joiner.on("-").join(idCategories);
        if (id.length() != 0) {
            id += "-";
        }
        id += idName;
        return id;
    }

    public void test(SpanBranch branch) {
        Assertions.assertEquals(testers.size(), branch.size(), "Children size");
        Iterator<ChildSpanExecutor<?>> it = testers.iterator();
        for (Span child : branch) {
            it.next().prepSpan(child);
        }
    }
}

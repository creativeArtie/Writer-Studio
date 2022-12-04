package com.creativeartie.writer.writing;

import java.util.*;

import org.fxmisc.richtext.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

public abstract class SpanDebugger<T extends Span> {

    private Class<T> extendedFrom;

    private class StyleDebugger {
        int length;
        String[] styles;

        private StyleDebugger(int l, String[] s) {
            length = l;
            styles = s;
        }
    }

    private ArrayList<SpanDebugger<?>> children;
    private ArrayList<StyleDebugger> stylesDebugger;

    public SpanDebugger(Class<T> extended) {
        extendedFrom = extended;
        children = new ArrayList<>();
        stylesDebugger = new ArrayList<>();
    }

    protected void addChild(SpanDebugger<?> child) {
        children.add(child);
    }

    protected void addStyle(int length, String... styles) {
        stylesDebugger.add(new StyleDebugger(length, styles));
    }

    public void test(DocBuilder builder, T span) {
        ArrayList<Executable> tests = new ArrayList<>();
        tests.addAll(Arrays.asList(testSelf(span)));
        if (span instanceof SpanBranch) {
            tests.add(
                () -> Assertions
                    .assertAll("Children", testChildren((SpanBranch) span))
            );
        }
        tests.add(() -> Assertions.assertAll("styles", testStyles(builder)));
    }

    /**
     * get span specific tests. Style spans and children are test in separate
     * method.
     *
     * @see #test(DocBuilder, Span) helpee method
     */
    abstract Executable[] testSelf(T actual);

    /**
     * test styles
     *
     * @see #test(DocBuilder, Span) helpee method
     */
    private Executable[] testStyles(DocBuilder actual) {
        ArrayList<Executable> tests = new ArrayList<>();

        StyleSpans<Collection<String>> styles = actual.getStyles();
        tests.add(
            () -> Assertions.assertEquals(
                stylesDebugger.size(), styles.getSpanCount(), "style span count"
            )
        );

        Iterator<StyleSpan<Collection<String>>> it = styles.iterator();
        int i = 0;
        for (StyleDebugger style : stylesDebugger) {
            StyleSpan<Collection<String>> actualStyle = it.next();
            tests.add(
                () -> Assertions.assertAll(
                    "Sytle span at: " + Integer.toString(i),
                    () -> Assertions.assertEquals(
                        style.length, actualStyle.getLength(), "length"
                    ),
                    () -> Assertions.assertArrayEquals(
                        style.styles,
                        actualStyle.getStyle().toArray(new String[0])
                    )
                )

            );
        }

        return tests.toArray(new Executable[0]);
    }

    /**
     * test span children
     *
     * @see #test(DocBuilder, Span) helpee method
     */
    protected Executable[] testChildren(SpanBranch span) {
        ArrayList<Executable> tests = new ArrayList<>();
        tests.add(
            () -> Assertions
                .assertEquals(children.size(), span.size(), "children count")
        );
        int i = 0;
        Iterator<Span> it = span.iterator();
        for (SpanDebugger<?> child : children) {
            int idx = i;
            tests.add(
                () -> Assertions.assertAll(
                    "child at" + Integer.toString(idx),
                    child.testChild(it.next())
                )
            );
        }

        return tests.toArray(new Executable[0]);
    }

    /**
     * test span that needs to be cast first
     *
     * @see #testChildren(SpanBranch) helpee method
     */
    private Executable[] testChild(Span actual) {
        ArrayList<Executable> tests = new ArrayList<>();
        tests.add(() -> Assertions.assertInstanceOf(extendedFrom, actual));
        tests.addAll(Arrays.asList(testSelf(extendedFrom.cast(actual))));

        return tests.toArray(new Executable[0]);
    }

}

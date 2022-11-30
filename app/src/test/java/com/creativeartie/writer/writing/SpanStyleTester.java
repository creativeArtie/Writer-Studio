package com.creativeartie.writer.writing;

import java.util.*;

import org.fxmisc.richtext.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

import com.google.common.base.*;

public class SpanStyleTester {

    public interface SpanLengthProvider {
        public int forSpanAt(int idx);
    }

    public interface SpanStylesProvider {
        public String[] forSpanAt(int idx);
    }

    public interface SpanExecutableProvider<T extends Span> {

        public Class<T> getSpanClass();

        public void checkSpan(T value);
    }

    private boolean runTest;
    private DocBuilder docBuilder;
    private int spanCount;
    private SpanLengthProvider spanLength;
    private SpanStylesProvider spanStyles;

    public SpanStyleTester(DocBuilder builder, int count) {
        this(builder, count, true);
    }

    public SpanStyleTester(DocBuilder builder, int count, boolean testing) {
        runTest = testing;
        spanCount = count;
        docBuilder = builder;
    }

    public SpanStyleTester addSpanLength(SpanLengthProvider provider) {
        spanLength = provider;
        return this;
    }

    public SpanStyleTester addSpanStyle(SpanStylesProvider provider) {
        spanStyles = provider;
        return this;
    }

    public boolean isReady() {
        return (spanLength != null) && (spanStyles != null);
    }

    public void assertStyles() {
        Preconditions.checkState(isReady());

        StyleSpans<Collection<String>> styles = docBuilder.getStyles();
        if (!runTest) return;
        Assertions.assertEquals(
            spanCount, styles.getSpanCount(), "Span Counts"
        );
        int index = 0;
        for (StyleSpan<Collection<String>> style : styles) {
            int curIndex = index++; // make constant for input
            Executable[] tests = new Executable[2];
            int extra = 0;
            tests[extra++] = () -> Assertions.assertEquals(
                spanLength.forSpanAt(curIndex), style.getLength(), "length"
            );
            tests[extra++] = () -> Assertions.assertArrayEquals(
                spanStyles.forSpanAt(curIndex), style.getStyle().toArray(),
                "styles"
            );
            Assertions.assertAll("Style at " + index, tests);
        }
    }
}

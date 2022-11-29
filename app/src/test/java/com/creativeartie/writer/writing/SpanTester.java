package com.creativeartie.writer.writing;

import java.util.*;

import org.fxmisc.richtext.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

import com.google.common.base.*;

public class SpanTester {

    public interface SpanLengthProvider {
        public int forSpanAt(int idx);
    }

    public interface SpanStylesProvider {
        public String[] forSpanAt(int idx);
    }

    public interface SpanExecutableProvider {
        public void forSpanAt(int idx);
    }

    private boolean runTest;
    private DocBuilder docBuilder;
    private int spanCount;
    private SpanLengthProvider spanLength;
    private SpanStylesProvider spanStyles;
    private SpanExecutableProvider[] spanExecute;

    public SpanTester(DocBuilder builder, int count) {
        this(builder, count, true);
    }

    public SpanTester(DocBuilder builder, int count, boolean testing) {
        runTest = testing;
        spanCount = count;
        docBuilder = builder;
        spanExecute = new SpanExecutableProvider[0];
    }

    public SpanTester addSpanLength(SpanLengthProvider provider) {
        spanLength = provider;
        return this;
    }

    public SpanTester addSpanStyle(SpanStylesProvider provider) {
        spanStyles = provider;
        return this;
    }

    public SpanTester addMoreTests(SpanExecutableProvider... providers) {
        spanExecute = providers;
        return this;
    }

    public boolean isReady() {
        return (spanLength != null) && (spanStyles != null);
    }

    public void assertAll() {
        Preconditions.checkState(isReady());

        StyleSpans<Collection<String>> styles = docBuilder.getStyles();
        if (!runTest) return;
        Assertions.assertEquals(
            spanCount, styles.getSpanCount(), "Span Counts"
        );
        int index = 0;
        for (StyleSpan<Collection<String>> style : styles) {
            int curIndex = index++;
            Executable[] tests = new Executable[spanExecute.length + 2];
            int extra = 0;
            for (SpanExecutableProvider other : spanExecute) {
                tests[extra++] = () -> other.forSpanAt(curIndex);
            }
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

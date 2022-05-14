package com.creativeartie.writer.writing;

import java.util.*;

import org.fxmisc.richtext.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

import com.google.common.base.*;

public class CommonTests {

    public interface SpanLengthProvider {
        public int forSpanAt(int idx);
    }

    public interface SpanStylesProvider {
        public String[] forSpanAt(int idx);
    }

    public interface SpanExecutableProvider {
        public void forSpanAt(int idx);
    }

    public static void assertSpanStyles(
        DocBuilder builder, int size, SpanLengthProvider expectLength,
        SpanStylesProvider expectClasses, SpanExecutableProvider... others
    ) {
        StyleSpans<Collection<String>> styles = builder.getStyles();
        Assertions.assertEquals(size, styles.getSpanCount(), "Span Counts");
        int index = 0;
        for (StyleSpan<Collection<String>> style : styles) {
            int curIndex = index++;
            Executable[] tests = new Executable[others.length + 2];
            int extra = 0;
            for (SpanExecutableProvider other : others) {
                tests[extra++] = () -> other.forSpanAt(curIndex);
            }
            tests[extra++] = () -> Assertions.assertEquals(
                expectLength.forSpanAt(curIndex), style.getLength(), "length"
            );
            tests[extra++] = () -> Assertions.assertArrayEquals(
                expectClasses.forSpanAt(curIndex), style.getStyle().toArray(),
                "styles"
            );
            Assertions.assertAll("Style at " + index, tests);
        }
    }

    public static void printStyle(String text, DocBuilder builder) {
        int start = 0;
        System.out.println(text);
        for (StyleSpan<Collection<String>> style : builder.getStyles()) {
            System.out.printf(
                "%s: \"%s\"\n", Joiner.on(" ").join(style.getStyle()),
                text.substring(start, start + style.getLength())
            );
            start += style.getLength();
        }
        System.out.println();
    }

}

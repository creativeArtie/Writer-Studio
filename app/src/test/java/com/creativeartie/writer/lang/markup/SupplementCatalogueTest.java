package com.creativeartie.writer.lang.markup;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.stream.*;
import java.util.function.*;

import com.creativeartie.writer.lang.*;
import static org.junit.jupiter.api.Assertions.*;

public class SupplementCatalogueTest{

    private static class Footnotes extends IDParameterMethodSource{

        protected String getIdText(){
            return "!^abc:text\n";
        }
        protected String getRefText(){
            return "{^abc}\n";
        }
    }

    public static Stream<Arguments> footnote(){
        return Footnotes.provideText(new Footnotes());
    }

    @ParameterizedTest(name = "Footnote/Endnote/Research {2}: {0}")
    @MethodSource
    public void footnote(CatalogueStatus status, String text, boolean[] ids){
        test(status, text, ids, s ->
            () -> ((FormatSpanPointId)s).getTarget()
        );
    }

    private static class Links extends IDParameterMethodSource{

        protected String getIdText(){
            return "!@abc:www.example.com\n";
        }
        protected String getRefText(){
            return "<@abc>\n";
        }
    }

    public static Stream<Arguments> link(){
        return Links.provideText(new Links());
    }

    @ParameterizedTest(name = "Hyperlink/Bookmark {2}: {0}")
    @MethodSource("link")
    public void link(CatalogueStatus status, String text, boolean[] ids){
        test(status, text, ids, s ->
            () -> ((FormatSpanLinkRef)s).getPathSpan()
        );
    }

    private void test(CatalogueStatus status, String text, boolean[] ids,
            Function<Span, Supplier< Optional<SpanBranch> >> supplier){
        Assumptions.assumeTrue(status != CatalogueStatus.NO_ID,
            "NO_ID not test here.");
        Assumptions.assumeTrue(() -> {
            for (boolean id: ids){ if (!id) return true; }
            return false;
        }, "No testing id span.");
        ArrayList<Supplier< Optional<SpanBranch> >> tests = new ArrayList<>();
        SpanBranch expect = null;

        for(Span span: new WritingText(text).get(0)){
            if (span instanceof LinedSpanParagraph){
                SpanBranch search = (LinedSpanParagraph) span;
                SpanBranch gotton = (FormattedSpan) search.get(0);
                tests.add( supplier.apply(gotton.get(0)) );
            } else {
                expect = (SpanBranch)span;
            }
        }
        SpanBranch expected = expect;
        for (Supplier<Optional<SpanBranch>> test: tests){
            switch (status){
            case NOT_FOUND:
                assertFalse(test.get().isPresent());
                break;
            case MULTIPLE:
                assertFalse(test.get().isPresent());
                break;
            case READY:
                assertAll(
                    () -> assertTrue(test.get().isPresent()),
                    () -> assertEquals(expected, test.get().get())
                );
            }
        }
    }
}

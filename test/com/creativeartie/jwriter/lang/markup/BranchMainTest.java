package com.creativeartie.jwriter.lang.markup;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.*;

import com.google.common.collect.*;

import static org.junit.Assert.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;

public class BranchMainTest {
    private static class MainTest<T extends MainTest<T>> extends
            SpanBranchAssert<T> {
        private int publishCount;
        private int noteCount;

        MainTest(Class<T> clazz){
            super(clazz);
        }

        public T setPublishCount(int count){
            publishCount = count;
            return cast();
        }

        public T setNoteCount(int count){
            noteCount = count;
            return cast();
        }

        @Override
        public void test(SpanBranch span){
            MainSpan test = (MainSpan) span;
            assertEquals(getError("publish", span), publishCount,
                test.getPublishCount());
            assertEquals(getError("note", span), noteCount,
                test.getNoteCount());
        }

    }

    public static class MainNoteTest extends MainTest<MainNoteTest>{

        private ImmutableMultimap.Builder<InfoFieldType, InfoDataSpan<?>> builder;
        private Multimap<InfoFieldType, InfoDataSpan<?>> sources;

        public MainNoteTest(){
            super(MainNoteTest.class);
            builder = ImmutableMultimap.builder();
        }

        public MainNoteTest putData(InfoFieldType key, InfoDataSpan<?> value){
            builder.put(key, value);
            return this;
        }

        @Override
        public void setup(){
            sources = builder.build();
            setStyles(AuxiliaryStyle.MAIN_NOTE, getCatalogueStatus());
        }

        @Override
        public void test(SpanBranch span){
            MainSpanNote test = assertClass(span, MainSpanNote.class);
            assertEquals(getError("sources", test), sources, test.getSources());
            super.test(span);
        }
    }

    public static class MainSectionTest extends MainTest<MainSectionTest>{

        private Multimap<InfoFieldType, InfoDataSpan<?>> sources;

        public MainSectionTest(){
            super(MainSectionTest.class);
        }

        @Override
        public void setup(){
            setStyles(AuxiliaryStyle.MAIN_SECTION, getCatalogueStatus());
        }

        @Override
        public void test(SpanBranch span){
            MainSpanNote test = assertClass(span, MainSpanNote.class);
            /// See SupplementSectionDebug for more details.
            super.test(span);
        }
    }
}
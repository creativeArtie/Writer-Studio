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
        private int publishTotal;
        private int noteTotal;

        MainTest(Class<T> clazz){
            super(clazz);
        }

        public T setPublishTotal(int count){
            publishTotal = count;
            return cast();
        }

        public T setNoteTotal(int count){
            noteTotal = count;
            return cast();
        }

        @Override
        public void test(SpanBranch span){
            MainSpan test = (MainSpan) span;
            assertEquals(getError("publish", span), publishTotal,
                test.getPublishTotal());
            assertEquals(getError("note", span), noteTotal,
                test.getNoteTotal());
        }

    }

    public static class MainNoteTest extends MainTest<MainNoteTest>{

        private ImmutableMultimap.Builder<InfoFieldType, InfoDataSpan> builder;
        private Multimap<InfoFieldType, InfoDataSpan> sources;

        public MainNoteTest(){
            super(MainNoteTest.class);
            builder = ImmutableMultimap.builder();
        }

        public MainNoteTest putData(InfoFieldType key, DocumentAssert doc,
                int ... idx){
            builder.put(key, doc.getChild(InfoDataSpan.class, idx));
            return this;
        }

        @Override
        public void setup(){
            sources = builder.build();
            setStyles(AuxiliaryType.MAIN_NOTE, getCatalogueStatus());
        }

        @Override
        public void test(SpanBranch span){
            MainSpanNote test = assertClass(span, MainSpanNote.class);
            assertEquals(getError("sources", test), sources, test.getSources());
            super.test(span);
        }
    }

    public static class MainSectionTest extends MainTest<MainSectionTest>{

        private EditionType edition;

        public MainSectionTest(){
            super(MainSectionTest.class);
        }

        public MainSectionTest setEdition(EditionType type){
            edition = type;
            return this;
        }

        @Override
        public void setup(){
            setStyles(AuxiliaryType.MAIN_SECTION);
        }

        @Override
        public void test(SpanBranch span){
            MainSpanSection test = assertClass(span, MainSpanSection.class);
            assertEquals(getError("edition", test), edition, test.getEdition());
            /// See SupplementSectionDebug for more details.
            super.test(span);
        }
    }
}
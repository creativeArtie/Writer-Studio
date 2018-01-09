package com.creativeartie.jwriter.lang.markup;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.*;

import com.google.common.collect.*;

import static org.junit.Assert.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;

public class BranchLinesTest {

    public static class NoteCardTest extends SpanBranchAssert<NoteCardTest>{

        private ImmutableMultimap.Builder<InfoFieldType, InfoDataSpan> builder;
        private Multimap<InfoFieldType, InfoDataSpan> sources;
        private int noteTotal;

        public NoteCardTest(){
            super(NoteCardTest.class);
            builder = ImmutableMultimap.builder();
        }

        public NoteCardTest setNoteTotal(int count){
            noteTotal = count;
            return this;
        }

        public NoteCardTest putData(InfoFieldType key, DocumentAssert doc,
            int ... idx){
            SpanBranch span = doc.getChild(idx);

            if (span instanceof InfoDataSpan){
                    builder.put(key, (InfoDataSpan) span);
            } else {
                throw new IllegalArgumentException(span +
                    " is not of type FormatSpanMain. Gotten: " + span.getClass());
            }
            return this;
        }

        @Override
        public void setup(){
            sources = builder.build();
            setStyles(AuxiliaryType.MAIN_NOTE, getCatalogueStatus());
        }

        @Override
        public void test(SpanBranch span){
            NoteCardSpan test = assertClass(span, NoteCardSpan.class);
            assertEquals(getError("sources", test), sources, test.getSources());
            assertEquals(getError("note", span), noteTotal,
                test.getNoteTotal());
        }
    }
/*
    public static class SectionTest extends MainTest<SectionTest>{
        private int publishTotal;
        private int noteTotal;

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

        public NoteCardTest setNoteTotal(int count){
            noteTotal = count;
            return cast();
        }

        public SectionTest setPublishTotal(int count){
            publishTotal = count;
            return cast();
        }

        @Override
        public void test(SpanBranch span){
            MainSpanSection test = assertClass(span, SectionHead.class);
            assertEquals(getError("edition", test), edition, test.getEdition());
            /// See SupplementSectionDebug for more details.
            assertEquals(getError("publish", span), publishTotal,
                test.getPublishTotal());
            assertEquals(getError("note", span), noteTotal,
                test.getNoteTotal());
        }
    }*/
}
package com.creativeartie.jwriter.lang.markup;

import java.util.*;
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


    private static class SectionTest<T extends SectionTest>
            extends SpanBranchAssert<T>{
        private Optional<LinedSpanLevelSection> sectionHeading;
        private int sectionLevel;
        private EditionType sectionEdition;
        private int publishTotal;
        private int noteTotal;
        private List<LinedSpan> sectionLines;
        private List<NoteCardSpan> sectionNotes;

        public SectionTest(Class<T> clazz){
            super(clazz);
            sectionLines = new ArrayList<>();
            sectionNotes = new ArrayList<>();
            sectionHeading = Optional.empty();
            sectionLevel = 1;
            sectionEdition = EditionType.NONE;
        }

        public T setHeading(DocumentAssert doc, int ... idx){
            LinedSpanLevelSection line = doc.getChild(LinedSpanLevelSection
                .class, idx);
            sectionHeading = Optional.of(line);
            sectionLevel = line.getLevel();
            sectionEdition = line.getEdition();
            return cast();
        }

        public T setPublishTotal(int count){
            publishTotal = count;
            return cast();
        }

        public T setNoteTotal(int count){
            noteTotal = count;
            return cast();
        }

        public T addLine(DocumentAssert doc, int ... idx){
            LinedSpan line = doc.getChild(LinedSpan.class, idx);
            sectionLines.add(line);
            return cast();
        }

        public T addNote(DocumentAssert doc, int ... idx){
            NoteCardSpan note = doc.getChild(NoteCardSpan.class, idx);
            sectionNotes.add(note);
            return cast();
        }

        @Override
        public void test(SpanBranch span){
            SectionSpan test = (SectionSpan) span;
            assertSpan("heading", span, sectionHeading, test.getHeading());
            assertEquals(getError("level", span), sectionLevel, test.getLevel());
            assertSame(getError("edition", span), sectionEdition,
                test.getEdition());
            assertEquals(getError("publish", span), publishTotal, test.getPublishTotal());
            assertEquals(getError("note", span), noteTotal, test.getNoteTotal());
            assertEquals(getError("lines", span), sectionLines, test.getLines());
            assertEquals(getError("notes", span), sectionNotes, test.getNotes());
        }
    }
}
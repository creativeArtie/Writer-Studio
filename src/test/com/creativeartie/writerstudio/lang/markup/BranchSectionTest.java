package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.util.function.*;

import com.google.common.collect.*;

import static org.junit.Assert.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

public class BranchSectionTest {

    public static SetupParser getParser(){
        return SectionParseHead.SECTION_1;
    }

    public static class NoteCardTest extends SpanBranchAssert<NoteCardTest>{

        private ImmutableMultimap.Builder<InfoFieldType, InfoDataSpan> builder;
        /// TODO remove sources and replace with its replacements.
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
            NoteCardSpan test = assertClass(span, NoteCardSpan.class);
            assertEquals(getError("note", span), noteTotal,
                test.getNoteTotal());
        }
    }


    private static class SectionTest<T extends SectionTest>
            extends SpanBranchAssert<T>{
        private Optional<LinedSpanLevelSection> sectionHeading;
        private int sectionLevel;
        private EditionType sectionEdition;
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
            sectionEdition = line.getEditionType();
            return cast();
        }

        public T addLine(DocumentAssert doc, int ... idx){
            sectionLines.add(doc.getChild(LinedSpan.class, idx));
            return cast();
        }

        public T addNote(DocumentAssert doc, int ... idx){
            sectionNotes.add(doc.getChild(NoteCardSpan.class, idx));
            return cast();
        }

        public T setLevel(int level){
            sectionLevel = level;
            return cast();
        }

        @Override
        public void test(SpanBranch span){
            SectionSpan test = (SectionSpan) span;
            assertSpan("heading", span, sectionHeading, test.getHeading());
            assertEquals(getError("level", span), sectionLevel, test.getLevel());
            assertSame(getError("edition", span), sectionEdition,
                test.getEditionType());
            assertEquals(getError("lines", span), sectionLines, test.getLines());
            assertEquals(getError("notes", span), sectionNotes, test.getNotes());
        }
    }

    public static class HeadSectionTest extends SectionTest<HeadSectionTest>{
        private List<SectionSpanHead> spanSections;
        private List<SectionSpanScene> spanScenes;
        private List<LinedSpan> allLines;
        private int publishTotal;
        private int noteTotal;

        public HeadSectionTest(){
            super(HeadSectionTest.class);
            spanSections = new ArrayList<>();
            spanScenes = new ArrayList<>();
            allLines = new ArrayList<>();
        }

        public HeadSectionTest addSection(DocumentAssert doc, int ... idx){
            spanSections.add(doc.getChild(SectionSpanHead.class, idx));
            return this;
        }

        public HeadSectionTest addScene(DocumentAssert doc, int ... idx){
            spanScenes.add(doc.getChild(SectionSpanScene.class, idx));
            return this;
        }

        public HeadSectionTest setPublishTotal(int count){
            publishTotal = count;
            return this;
        }

        public HeadSectionTest setNoteTotal(int count){
            noteTotal = count;
            return this;
        }

        public HeadSectionTest addAllLine(DocumentAssert doc, int ... idx){
            allLines.add(doc.getChild(LinedSpan.class, idx));
            return this;
        }

        @Override
        public HeadSectionTest addLine(DocumentAssert doc, int ... idx){
            super.addLine(doc, idx);
            allLines.add(doc.getChild(LinedSpan.class, idx));
            return this;
        }

        @Override
        public void setup(){
            setStyles(AuxiliaryType.SECTION_HEAD);
        }

        @Override
        public void test(SpanBranch span){
            SectionSpanHead test = assertClass(span, SectionSpanHead.class);
            assertEquals(getError("sections", test), spanSections, test.getSections());
            assertEquals(getError("scenes", test), spanScenes, test.getScenes());
            assertEquals(getError("publish", span), publishTotal, test.getPublishTotal());
            assertEquals(getError("note", span), noteTotal, test.getNoteTotal());
            assertEquals(getError("all lines", span), allLines, test.getAllLines());
            super.test(span);
        }
    }

    public static class SceneSectionTest extends SectionTest<SceneSectionTest>{
        private SectionSpanHead parentHead;
        private List<SectionSpanScene> sceneList;

        public SceneSectionTest(){
            super(SceneSectionTest.class);
            sceneList = new ArrayList<>();
        }

        public SceneSectionTest setParentHead(DocumentAssert doc, int ... idx){
            parentHead = doc.getChild(SectionSpanHead.class, idx);
            return this;
        }

        @Override
        public void setup(){
            setStyles(AuxiliaryType.SECTION_SCENE);
        }

        public SceneSectionTest addScene(DocumentAssert doc, int ... idx){
            sceneList.add(doc.getChild(SectionSpanScene.class, idx));
            return this;
        }

        public void test(SpanBranch span){
            SectionSpanScene test = assertClass(span, SectionSpanScene.class);
            assertEquals(getError("head", test), parentHead, test.getSection());
            assertEquals(getError("scenes", test), sceneList, test.getSubscenes());
            super.test(span);
        }

    }
}
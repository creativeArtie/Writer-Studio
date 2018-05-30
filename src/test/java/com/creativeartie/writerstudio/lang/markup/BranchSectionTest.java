package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.function.*;

import java.util.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;


public class BranchSectionTest {

    public static SetupParser getParser(){
        return SectionParseHead.SECTION_1;
    }

    public static class NoteCardTest extends SpanBranchAssert<NoteCardTest>{

        private int[] noteTitle;
        private ArrayList<int[]> noteContents;
        private int[] inText;
        private int[] noteSource;
        private int noteTotal;
        private String lookupText;

        public NoteCardTest(DocumentAssert doc){
            super(NoteCardTest.class, doc);
            noteTitle = null;
            noteContents = new ArrayList<>();
            inText = null;
            noteSource = null;
            noteTotal = 1;
            lookupText = "";
        }

        /** For {@link NoteCardSpan#getTitle()} (no defualt) */
        public NoteCardTest setTitle(int ... indexes){
            noteTitle = indexes;
            return this;
        }

        /** For {@link NoteCardSpan#getContent()} (no defualt) */
        public NoteCardTest addContent(int ... indexes){
            noteContents.add(indexes);
            return this;
        }

        /** For {@link NoteCardSpan#getInTextLine()} (no defualt) */
        public NoteCardTest setInText(int ... indexes){
            inText = indexes;
            return this;
        }

        /** For {@link NoteCardSpan#getInSource()} (no defualt) */
        public NoteCardTest setSource(int ... indexes){
            noteSource = indexes;
            return this;
        }

        /** For {@link NoteCardSpan#getNoteTotal()} (defualt: 1) */
        public NoteCardTest setSource(int total){
            noteTotal = total;
            return this;
        }

        /** For {@link NoteCardSpan#getLookupText()} (defualt: "") */
        public NoteCardTest setSource(String lookup){
            lookupText = lookup;
            return this;
        }

        @Override
        public void setup(){
            setStyles(AuxiliaryType.MAIN_NOTE, getCatalogueStatus());
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            NoteCardSpan test = assertClass(NoteCardSpan.class);
            tests.add(() -> assertChild(FormattedSpan.class, noteTitle,
                () -> test.getTitle(), "getTitle()"));
            tests.add(() -> assertChild(FormattedSpan.class, noteContents,
                () -> test.getContent(), "getContent()"));
            tests.add(() -> assertChild(LinedSpanCite.class, noteSource,
                () -> test.getSource(), "getSource()"));
            tests.add(() -> assertEquals(noteTotal, test.getNoteTotal(),
                "getNoteTotal()"));
            tests.add(() -> assertEquals(lookupText, test.getLookupText(),
                "getLookupText()"));
            assertEquals(noteTotal, test.getNoteTotal(), "getNoteTotal()");
        }
    }


    private static abstract class SectionTest<T extends SectionTest>
            extends SpanBranchAssert<T>{
        private int[] sectionHeading ;

        private ArrayList<int[]> sectionLines;
        private ArrayList<int[]> sectionNotes;
        private int publishCount;
        private int publishTotal;
        private int noteCount;
        private int noteTotal;

        public SectionTest(Class<T> clazz, DocumentAssert doc){
            super(clazz, doc);
            sectionHeading = null;

            sectionLines = new ArrayList<>();
            sectionNotes = new ArrayList<>();

            publishCount = 1;
            publishTotal = 1;
            noteCount = 0;
            noteTotal = 0;
        }

        public T setHeading(int ... indexes){
            sectionHeading = indexes;
            return cast();
        }

        public T setPublishCount(int count){
            publishCount = count;
            return cast();
        }

        public T setNoteCount(int count){
            noteCount = count;
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

        protected T addLine(int ... indexes){
            sectionLines.add(indexes);
            return cast();
        }

        public T addNote(int ... indexes){
            sectionNotes.add(indexes);
            return cast();
        }

        protected abstract SectionSpan moreTest(SpanBranch span,
            ArrayList<Executable> tests);

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            SectionSpan test = moreTest(span, tests);

            tests.add(() -> assertChild(FormattedSpan.class, sectionHeading,
                () -> test.getHeading(), "getHeading()"));

            Optional<LinedSpanLevelSection> section = test.getHeading();
            int level = section.map(s -> s.getLevel()).orElse(1);
            EditionType type = section.map(s -> s.getEditionType()).
                orElse(EditionType.NONE);
            String detail = section.map(s -> s.getEditionDetail()).orElse("");

            tests.add(() -> assertEquals(level, test.getLevel(), "getLevel()"));
            tests.add(() -> assertEquals(type, test.getEditionType(),
                "getEditionType()"));
            tests.add(() -> assertEquals(detail, test.getEditionDetail(),
                "getEditionDetail()"));

            tests.add(() -> assertChild(LinedSpan.class, sectionLines,
                () -> test.getLines(), "getLines()"));
            tests.add(() -> assertChild(NoteCardSpan.class, sectionNotes,
                () -> test.getNotes(), "getNotes()"));
            tests.add(() -> assertEquals(publishCount, test.getPublishCount(),
                "getPublishCount()"));
            tests.add(() -> assertEquals(publishTotal, test.getPublishTotal(),
                "getPublishTotal()"));
            tests.add(() -> assertEquals(noteCount, test.getNoteCount(),
                "getNoteCount()"));
            tests.add(() -> assertEquals(noteTotal, test.getNoteTotal(),
                "getNoteTotal()"));
        }
    }

    public static class HeadSectionTest extends SectionTest<HeadSectionTest>{
        private ArrayList<int[]> spanSections;
        private ArrayList<int[]> spanScenes;
        private ArrayList<int[]> allLines;

        public HeadSectionTest(DocumentAssert doc){
            super(HeadSectionTest.class, doc);
            spanSections = new ArrayList<>();
            spanScenes = new ArrayList<>();
            allLines = new ArrayList<>();
        }

        public HeadSectionTest addSection(int ... idx){
            spanSections.add(idx);
            return this;
        }

        public HeadSectionTest addScene(int ... idx){
            spanScenes.add(idx);
            return this;
        }

        public HeadSectionTest addAllLine(int ... idx){
            allLines.add(idx);
            return this;
        }

        @Override
        public HeadSectionTest addLine(int ... idx){
            super.addLine(idx);
            allLines.add(idx);
            return this;
        }

        @Override
        public void setup(){
            setStyles(AuxiliaryType.SECTION_HEAD);
        }

        @Override
        public SectionSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            SectionSpanHead test = assertClass(SectionSpanHead.class);

            tests.add(() -> assertChild(LinedSpan.class, allLines,
                () -> test.getAllLines(), "getAllLines()"));
            tests.add(() -> assertChild(LinedSpan.class, spanSections,
                () -> test.getSections(), "getSections()"));
            tests.add(() -> assertChild(LinedSpan.class, spanScenes,
                () -> test.getScenes(), "getScenes()"));
            return test;
        }
    }

    public static class SceneSectionTest extends SectionTest<SceneSectionTest>{
        private int[] parentHead;
        private List<int[]> sceneList;

        public SceneSectionTest(DocumentAssert doc){
            super(SceneSectionTest.class, doc);
            parentHead = null;
            sceneList = new ArrayList<>();
        }

        public SceneSectionTest setParentHead(int ... indexes){
            parentHead = indexes;
            return this;
        }

        public SceneSectionTest addScene(int ... indexes){
            sceneList.add(indexes);
            return this;
        }

        @Override
        public void setup(){
            setStyles(AuxiliaryType.SECTION_SCENE);
        }

        @Override
        public SectionSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            SectionSpanScene test = assertClass(SectionSpanScene.class);

            tests.add(() -> assertSpan(LinedSpan.class, parentHead,
                () -> test.getSection(), "getSection()"));
            tests.add(() -> assertChild(LinedSpan.class, sceneList,
                () -> test.getSubscenes(), "getSubscenes()"));
            return test;
        }

    }
}

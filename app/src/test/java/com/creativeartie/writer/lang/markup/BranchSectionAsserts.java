package com.creativeartie.writer.lang.markup;

import org.junit.jupiter.api.function.*;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static org.junit.jupiter.api.Assertions.*;

public class BranchSectionAsserts {

    public static SetupParser getParser(){
        return SectionParseHead.SECTION_1;
    }

    public static final class NoteCardAssert
            extends SpanBranchAssert<NoteCardAssert>{

        private int[] noteTitle;
        private ArrayList<int[]> noteContents;
        private int[] noteSource;
        private int noteTotal;
        private String lookupText;

        public NoteCardAssert(DocumentAssert doc){
            super(NoteCardAssert.class, doc);
            noteTitle = null;
            noteContents = new ArrayList<>();
            noteSource = null;
            noteTotal = 1;
            lookupText = "";
        }

        /** For {@link NoteCardSpan#getTitle()} (no defualt) */
        public NoteCardAssert setTitle(int ... indexes){
            noteTitle = indexes;
            return this;
        }

        /** For {@link NoteCardSpan#getContent()} (no defualt) */
        public NoteCardAssert addContent(int ... indexes){
            noteContents.add(indexes);
            return this;
        }

        /** For {@link NoteCardSpan#getInTextLine()} (no defualt) */
        public NoteCardAssert setInText(int ... indexes){
            return this;
        }

        /** For {@link NoteCardSpan#getInSource()} (no defualt) */
        public NoteCardAssert setSource(int ... indexes){
            noteSource = indexes;
            return this;
        }

        /** For {@link NoteCardSpan#getNoteTotal()} (defualt: 1) */
        public NoteCardAssert setNote(int total){
            noteTotal = total;
            return this;
        }

        /** For {@link NoteCardSpan#getLookupText()} (defualt: "") */
        public NoteCardAssert setLookup(String lookup){
            lookupText = lookup;
            return this;
        }

        @Override
        public void setup(){
            setCatalogued();
            setId(true);
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


    private static abstract class SectionAssert<T extends SectionAssert<?>>
            extends SpanBranchAssert<T>{
        private int[] sectionHeading ;

        private ArrayList<int[]> sectionLines;
        private ArrayList<int[]> sectionNotes;

        private int publishCount;
        private int publishTotal;
        private int noteCount;
        private int noteTotal;

        private int sectionLevel;

        public SectionAssert(Class<T> clazz, DocumentAssert doc){
            super(clazz, doc);
            sectionHeading = null;
            sectionLevel = 1;

            sectionLines = new ArrayList<>();
            sectionNotes = new ArrayList<>();

            publishCount = 1;
            publishTotal = 1;
            noteCount = 0;
            noteTotal = 0;

        }

        public final T setHeading(int ... indexes){
            sectionHeading = indexes;
            return cast();
        }

        public final T setPublishCount(int count){
            publishCount = count;
            return cast();
        }

        public final T setNoteCount(int count){
            noteCount = count;
            return cast();
        }

        public final T setPublishTotal(int count){
            publishTotal = count;
            return cast();
        }

        public final T setNoteTotal(int count){
            noteTotal = count;
            return cast();
        }

        protected T addLine(int ... indexes){
            sectionLines.add(indexes);
            return cast();
        }

        public final T addNote(int ... indexes){
            sectionNotes.add(indexes);
            return cast();
        }

        public final T setLevel(int level){
            sectionLevel = level;
            return cast();
        }

        @Override
        public void setup(){
            moreSetup();
        }

        protected abstract void moreSetup();

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            SectionSpan test = moreTest(span, tests);

            tests.add(() -> assertChild(LinedSpanLevelSection.class, sectionHeading,
                () -> test.getHeading(), "getHeading()"));

            Optional<LinedSpanLevelSection> section = test.getHeading();
            EditionType type = section.map(s -> s.getEditionType()).
                orElse(EditionType.NONE);
            String detail = section.map(s -> s.getEditionDetail()).orElse("");

            tests.add(() -> assertEquals(sectionLevel, test.getLevel(),
                "getLevel()"));
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

        protected abstract SectionSpan moreTest(SpanBranch span,
            ArrayList<Executable> tests);
    }

    public static final class HeadSectionAssert
            extends SectionAssert<HeadSectionAssert>{
        private ArrayList<int[]> spanSections;
        private ArrayList<int[]> spanScenes;
        private ArrayList<int[]> allLines;

        public HeadSectionAssert(DocumentAssert doc){
            super(HeadSectionAssert.class, doc);
            spanSections = new ArrayList<>();
            spanScenes = new ArrayList<>();
            allLines = new ArrayList<>();
        }

        public HeadSectionAssert addSection(int ... idx){
            spanSections.add(idx);
            return this;
        }

        public HeadSectionAssert addScene(int ... idx){
            spanScenes.add(idx);
            return this;
        }

        public HeadSectionAssert addAllLine(int ... idx){
            allLines.add(idx);
            return this;
        }

        @Override
        public HeadSectionAssert addLine(int ... idx){
            super.addLine(idx);
            allLines.add(idx);
            return this;
        }

        @Override
        public void moreSetup(){}

        @Override
        public SectionSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            SectionSpanHead test = assertClass(SectionSpanHead.class);

            tests.add(() -> assertChild(LinedSpan.class, allLines,
                () -> test.getAllLines(), "getAllLines()"));
            tests.add(() -> assertChild(SectionSpanHead.class, spanSections,
                () -> test.getSections(), "getSections()"));
            tests.add(() -> assertChild(SectionSpanScene.class, spanScenes,
                () -> test.getScenes(), "getScenes()"));
            return test;
        }
    }

    public static final class SceneSectionAssert
            extends SectionAssert<SceneSectionAssert>{
        private int[] parentHead;
        private List<int[]> sceneList;

        public SceneSectionAssert(DocumentAssert doc){
            super(SceneSectionAssert.class, doc);
            parentHead = null;
            sceneList = new ArrayList<>();
        }

        public SceneSectionAssert setParentHead(int ... indexes){
            parentHead = indexes;
            return this;
        }

        public SceneSectionAssert addScene(int ... indexes){
            sceneList.add(indexes);
            return this;
        }

        @Override
        public void moreSetup(){}

        @Override
        public SectionSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            SectionSpanScene test = assertClass(SectionSpanScene.class);

            tests.add(() -> assertSpan(SectionSpanHead.class, parentHead,
                () -> test.getSection(), "getSection()"));
            tests.add(() -> assertChild(SectionSpanScene.class, sceneList,
                () -> test.getSubscenes(), "getSubscenes()"));
            return test;
        }

    }
}

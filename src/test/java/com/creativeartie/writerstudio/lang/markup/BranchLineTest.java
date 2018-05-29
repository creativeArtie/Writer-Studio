package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.function.*;

import java.util.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

public class BranchLineTest {
    private static class LineTest<T extends LineTest<T>> extends
            SpanBranchAssert<T> {

        private int publishTotal;
        private int noteTotal;
        private LinedType linedType;

        public LineTest(Class<T> clazz, int publish, int note){
            super(clazz);
            publishTotal = publish;
            noteTotal = note;
        }

        /** For {@link LinedSpan#getPublishTotal()} (default: depends). */
        public T setPublish(int count){
            publishTotal = count;
            return cast();
        }

        /** For {@link LinedSpan#getNoteTotal()} (default: depends). */
        public T setNote(int count){
            noteTotal = count;
            return cast();
        }

        public T setLinedType(LinedType type){
            linedType = type;
            return cast();
        }

        LinedType getLinedType(){
            return linedType;
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            LinedSpan test = (LinedSpan) span;
            tests.add(() -> assertEquals(linedType, test.getLinedType(),
                "getLinedType()"));
            tests.add(() -> assertEquals(publishTotal, test.getPublishTotal(),
                "getPublishTotal()"));
            tests.add(() -> assertEquals(noteTotal, test.getNoteTotal(),
                "getNoteTotal()"));
        }
    }

    public static class CiteLineTest extends LineTest<CiteLineTest>{

        private InfoFieldType infoType;
        private Optional<InfoDataSpan> dataSpan;

        public CiteLineTest(){
            super(CiteLineTest.class, 0, 1);
            setLinedType(LinedType.SOURCE);
            dataSpan = Optional.empty();
        }

        public CiteLineTest setInfoType(InfoFieldType type){
            infoType = type;
            return this;
        }

        public CiteLineTest setDataSpan(DocumentAssert doc, int ... idx){
            dataSpan = Optional.of(doc.getChild(InfoDataSpan.class, idx));
            return this;
        }

        @Override
        public void setup(){
            setLinedType(LinedType.SOURCE);
            setStyles(LinedType.SOURCE, infoType);
            if ( ! dataSpan.isPresent()){
                addStyles(AuxiliaryType.DATA_ERROR);
            }
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanCite test = assertClass(LinedSpanCite.class);

            tests.add(() -> assertEquals(infoType, test.getInfoFieldType(),
                "getInfoFieldType()"));
            tests.add(() -> assertSpan(SpanBranch.class, spanTarget,
                () -> span.getTarget(), "getTarget()"));
            super.test(span, tests);
        }
    }

    /** For when the style is just LinedType.*/
    private static class SimpleStyleTest<T extends SimpleStyleTest<T>>
            extends LineTest<T>{
        private LinedType linedType;

        public SimpleStyleTest(Class<T> clazz, LinedType type){
            super(clazz);
            linedType = type;
        }

        public T setLinedType(LinedType type){
            linedType = type;
            return super.setLinedType(type);
        }

        @Override
        public void setup(){
            setLinedType(linedType);
            setStyles(linedType);
        }
    }


    public abstract static class LevelLineTest<T extends SimpleStyleTest<T>>
            extends SimpleStyleTest<T>{
        private int lineLevel;
        private int[] lineText;

        public LevelLineTest(Class<T> clazz, int publish, int note){
            super(clazz, null, publish, note);
        linedLevel = 1;
            lineText = null;
        }

        /** For {@link LinedSpanLevel#getLevel()} (default: 1)*/
        public T setLevel(int level){
            lineLevel = level;
            return cast();
        }

        /** For {@link LinedSpanLevel#getForamtedSpan()} (no default) */
        public T setFormattedSpan(int... indexes){
            lineText = indexes;
            return cast();
        }

        protected abstract LinedSpanLevel testSubclass(SpanBranch span,
            ArrayList<Executable> tests);

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanLevel test = testSubclass(span, tests);
            tests.add(() -> assertEquals(lineLevel, test.getLevel(), "getLevel"));
            tests.add(() -> assertEquals(FormatedSpan.class, lineTest,
                () -> test.getFormatedSpan(), "getFormatedSpan()"));
            super.test(span, tests);
        }
    }

    public static class HeadLevelLineTest extends
            LevelLineTest<HeadLevelLineTest>{

        private EditionType editionType;
        private int[] editionSpan;
        private String lookupText;

        public HeadLevelLineTest(){
            super(HeadLevelLineTest.class, 1, 0);
        editionType = EditionType.NONE;
        editionSpan = null;
        }

        /** For {@link LinedSpanLevelSection#getEditionType()} (default: NONE)*/
        public HeadLevelLineTest setEdition(EditionType edition){
            editionType = edition;
        lookupText = "";
            return this;
        }

        /** For {@link LinedSpanLevelSection#getEditionSpan()} (no default)*/
        public HeadLevelLineTest setEditionSpan(int ... indexes){
            editionSpan = indexes;
            return this;
        }

        /** For {@link LinedSpanLevelSection#getLookupText()} (default: "")*/
        public HeadLevelLineTest setLookupText(String text){
            lookupText = text;
            return this;
        }

        protected LinedSpanLevel testSubclass(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanLevelSection test = assertClass(LinedSpanLevelSection.class);
            tests.add(() -> assertEquals(edition, test.getEditionType(),
                "getEdtionType()"));
            tests.add(() -> AssertEquals(editionSpan.class, editionSpan, () -> test.getEditionSpan(),
                "getEditionSpan()"));
            tests.add(() -> assertEquals(lookupText, test.getLookupText(),
                "getLookupText()"));
            return test;
        }
    }

    public static class ListLevelLineTest extends
                LevelLineTest<ListLevelLineTest>{

        public ListLevelLineTest(){
            super(ListLevelLineTest.class, 1, 0);
        }

        protected LinedSpanLevel testSubclass(SpanBranch span, ArrayList<Executable> tests){
            return assertClass(LinedSpanLevelList.class);
        }
    }

    public static class PointerLinkTest extends LineTest<PointerLinkTest>{
        private String linkPath;
        private String lookupText;

        public PointerLinkTest(){
            super(PointerLinkTest.class, 1, 0);
            linkPath = "";
            lookupText = "";
        }

        /** For {@link LinedSpanPointLink#getPath()} (default: "")*/
        public PointerLinkTest setPath(String path){
            linkPath = path;
            return this;
        }

        /** For {@link LinedSpanPointLink#getLookupText()} (default: "")*/
        public PointerLinkTest setLookup(String lookup){
            lookupText = lookup;
            return this;
        }

        @Override
        public void setup(){
            setLinedType(LinedType.LINK);
            setStyles(getLinedType(), getCatalogueStatus());
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanPointLink test = assertClass(span, LinedSpanPointLink.class);
            tests.add(() -> assertEquals(linkPath, test.getPath(), "getPath()"));
            tests.add(() -> assertEquals(lookupText, test.getLookupText(),
                "getLookupText()"));
            super.test(span, tests);
        }
    }

    public static class BreakLineTest extends SimpleStyleTest<BreakLineTest>{

        public BreakLineTest(){
            super(BreakLineTest.class, LinedType.BREAK, 0, 0);
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            super.test(assertClass(span, LinedSpanBreak.class));
        }
    }

    public static class AgendaLineTest extends SimpleStyleTest<AgendaLineTest>{
        private String agendaLine;

        public AgendaLineTest(){
            super(AgendaLineTest.class, LinedType.AGENDA, 0, 1);
        }

        /** For {@link LinedSpanAgenda#getAgenda()} (default: */
        public AgendaLineTest setAgenda(String agenda){
            agendaLine = agenda;
            return this;
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanAgenda test = assertClass(span, LinedSpanAgenda.class);
            tests.add(() -> assertEquals(agendaLine, test.getAgenda(),
                "getAgenda()"));
            super.test(span, tests);
        }
    }

    public static class ParagraphLineTest
            extends SimpleStyleTest<ParagraphLineTest>{

        private Optional<FormattedSpan> lineText;

        public ParagraphLineTest(){
            super(ParagraphLineTest.class, LinedType.PARAGRAPH, 1, 0);
            lineText = Optional.empty();
        }

        /** For {@link LinedSpanParagraph#getFormattedSpan()} (no default)*/
        public ParagraphLineTest setFormattedSpan(int ... idx){
            lineText = idx;
            return this;
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanParagraph test = assertClass(LinedSpanParagraph.class);
            tests.add(() -> FormattedSpan.class, lineText, () ->
                test.getFormattedSpan(), "getFormattedSpan()");
            super.test(span, tests);
        }
    }

    public static class NoteLineTest extends SimpleStyleTest<NoteLineTest>{

        private Optional<CatalogueIdentity> buildId;
        private int[] lineText;
        private String lookupText;

        public NoteLineTest(){
            super(NoteLineTest.class, LinedType.NOTE, 0, 1);
            buildId = Optional.empty();
            lineText = Optional.empty();
        lookupText = "";
        }

        /** For {@link LinedSpanNote#buildId()} (default: empty) */
        public NoteLineTest setBuildId(IDBuilder builder){
            buildId = Optional.ofNullable(builder).map(found -> found.build());
            return this;
        }

        /** For {@link LinedSpanNote#getFormattedSpan()} (no default) */
        public NoteLineTest setFormattedSpan(int ... indexes){
            lineText = indexes;
            return this;
        }

        /** For {@link LinedSpanNote#getLookupText()} (default: "") */
        public NoteLineTest setLookup(String text){
            lookupText = text;
            return this;
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanNote test = assertClass(LinedSpanNote.class);
            tests.add(() -> assertEquals(buildId, test.buildId(), "buildId()"));
            tests.add(() -> assertEquals(lookupText, test.getLookupText(),
                "getLookupText()"));
            tests.add(() -> FormattedSpan.class, linedText,
                () -> test.getFormattedText(), "getFormattedText()");
            super.test(span, tests);
        }
    }

    public static class PointerNoteTest extends SimpleStyleTest<PointerNoteTest>{
        private Optional<FormattedSpan> lineText;
        private String lookupText;

        public PointerNoteTest(){
            super(PointerNoteTest.class, null);
            lineText = Optional.empty();
            lookupText = "";
        }

        /** For {@link LinedSpanPointNote#getFormattedSpan()} (no default) */
        public PointerNoteTest setFormattedSpan(int ... indexes){
            lineText = indexes;
            return this;
        }

        /** For {@link LinedSpanPointNote#getLookupText()} (default: "")*/
        public PointerNoteTest setLookup(String lookup){
            lookupText = lookup;
            return this;
        }

        @Override
        public void setup(){
            addStyles(getCatalogueStatus());
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanPointNote test = assertClass(span, LinedSpanPointNote.class);

            tests.add(() -> FormattedSpan.class, linedText,
                () -> test.getFormattedText(), "getFormattedText()");
            tests.add(() -> assertEquals(lookupText, test.getLookupText(),
                "getLookupText()"));
            super.test(span);
        }
    }

    public static class QuoteLineTest extends SimpleStyleTest<QuoteLineTest>{
        private Optional<FormattedSpan> lineText;

        public QuoteLineTest(){
            super(QuoteLineTest.class, LinedType.QUOTE, 1, 0);
            lineText = Optional.empty();
        }

        /** For {@link LinedSpanQuote#getFormattedSpan()} (no default) */
        public QuoteLineTest setFormattedSpan(int ... indexes){
            lineText = indexes;
            return this;
        }


        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanQuote test = assertClass(span, LinedSpanQuote.class);
            tests.add(() -> FormattedSpan.class, linedText,
                () -> test.getFormattedText(), "getFormattedText()");
            super.test(span);
        }
    }
}

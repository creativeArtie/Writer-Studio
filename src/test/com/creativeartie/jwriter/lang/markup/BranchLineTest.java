package com.creativeartie.jwriter.lang.markup;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.*;

import static org.junit.Assert.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;

public class BranchLineTest {
    private static class LineTest<T extends LineTest<T>> extends
            SpanBranchAssert<T> {

        private int publishTotal;
        private int noteTotal;
        private LinedType linedType;
        private boolean isLast;

        public LineTest(Class<T> clazz){
            super(clazz);
            isLast = true;
        }

        public T setPublishTotal(int count){
            publishTotal = count;
            return cast();
        }

        public T setNoteTotal(int count){
            noteTotal = count;
            return cast();
        }

        public T setIsLast(boolean last){
            isLast = last;
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
        public void test(SpanBranch span){
            LinedSpan test = (LinedSpan) span;
            assertEquals(getError("type", span), linedType, test.getLinedType());
            assertEquals(getError("last", span), isLast, test.isLast());
            assertEquals(getError("publish", span), publishTotal, test.getPublishTotal());
            assertEquals(getError("note", span), noteTotal, test.getNoteTotal());
        }
    }

    public static class CiteLineTest extends LineTest<CiteLineTest>{

        private InfoFieldType infoType;
        private Optional<InfoDataSpan> dataSpan;

        public CiteLineTest(){
            super(CiteLineTest.class);
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
        public void test(SpanBranch span){
            LinedSpanCite test = assertClass(span, LinedSpanCite.class);
            assertEquals(getError("field", test), infoType, test.getFieldType());
            assertSpan("data", span, dataSpan, test.getData());
            super.test(span);
        }
    }

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
        private Optional<FormatSpanMain> lineText;

        public LevelLineTest(Class<T> clazz){
            super(clazz, null);
            lineText = Optional.empty();
        }

        public T setLevel(int level){
            lineLevel = level;
            return cast();
        }

        public T setFormattedSpan(DocumentAssert doc, int ... idx){
            lineText = Optional.of(doc.getChild(FormatSpanMain.class, idx));
            return cast();
        }

        protected abstract LinedSpanLevel testSubclass(SpanBranch span);

        @Override
        public void test(SpanBranch span){
            LinedSpanLevel test = testSubclass(span);
            assertEquals(getError("level", test), lineLevel, test.getLevel());
            assertSpan("text", span, lineText, test.getFormattedSpan());
            super.test(span);
        }
    }

    public static class HeadLevelLineTest extends
            LevelLineTest<HeadLevelLineTest>{

        private EditionType edition;

        public HeadLevelLineTest(){
            super(HeadLevelLineTest.class);
        }

        public HeadLevelLineTest setEdition(EditionType ed){
            edition = ed;
            return this;
        }

        protected LinedSpanLevel testSubclass(SpanBranch span){
            LinedSpanLevelSection test = assertClass(span, LinedSpanLevelSection.class);
            assertEquals(getError("edition", span), edition, test.getEdition());
            return test;
        }
    }

    public static class ListLevelLineTest extends
                LevelLineTest<ListLevelLineTest>{

        public ListLevelLineTest(){
            super(ListLevelLineTest.class);
        }

        protected LinedSpanLevel testSubclass(SpanBranch span){
            return assertClass(span, LinedSpanLevelList.class);
        }
    }

    public static class PointerLinkTest extends LineTest<PointerLinkTest>{
        private String path;

        public PointerLinkTest(){
            super(PointerLinkTest.class);
        }

        public PointerLinkTest setPath(String str){
            path = str;
            return this;
        }

        @Override
        public void setup(){
            setLinedType(LinedType.HYPERLINK);
            setStyles(getLinedType(), getCatalogueStatus());
        }

        @Override
        public void test(SpanBranch span){
            LinedSpanPointLink test = assertClass(span, LinedSpanPointLink.class);
            assertEquals(getError("path", test), path, test.getPath());
            super.test(span);
        }
    }

    public static class BreakLineTest extends SimpleStyleTest<BreakLineTest>{

        public BreakLineTest(){
            super(BreakLineTest.class, LinedType.BREAK);
        }

        @Override
        public void test(SpanBranch span){
            super.test(assertClass(span, LinedSpanBreak.class));
        }
    }

    public static class AgendaLineTest extends SimpleStyleTest<AgendaLineTest>{
        private String agendaLine;

        public AgendaLineTest(){
            super(AgendaLineTest.class, LinedType.AGENDA);
        }

        public AgendaLineTest setAgenda(String agenda){
            agendaLine = agenda;
            return this;
        }

        @Override
        public void test(SpanBranch span){
            LinedSpanAgenda test = assertClass(span, LinedSpanAgenda.class);
            assertEquals(getError("agenda", test), agendaLine, test.getAgenda());
            super.test(span);
        }
    }

    public static class ParagraphLineTest
            extends SimpleStyleTest<ParagraphLineTest>{

        private Optional<FormatSpanMain> lineText;

        public ParagraphLineTest(){
            super(ParagraphLineTest.class, LinedType.PARAGRAPH);
            lineText = Optional.empty();
        }

        public ParagraphLineTest setFormattedSpan(DocumentAssert doc, int ... idx){
            lineText = Optional.of(doc.getChild(FormatSpanMain.class, idx));
            return this;
        }

        @Override
        public void test(SpanBranch span){
            LinedSpanParagraph test = assertClass(span, LinedSpanParagraph.class);
            assertSpan("data", span, lineText, test.getFormattedSpan());
            super.test(span);
        }
    }

    public static class NoteLineTest extends SimpleStyleTest<NoteLineTest>{

        private Optional<CatalogueIdentity> buildId;
        private Optional<FormatSpanMain> lineText;
        private boolean isFirst;

        public NoteLineTest(){
            super(NoteLineTest.class, LinedType.NOTE);
            buildId = Optional.empty();
            lineText = Optional.empty();
            isFirst = true;
        }

        public NoteLineTest setBuildId(IDBuilder builder){
            buildId = Optional.ofNullable(builder).map(found -> found.build());
            return this;
        }

        public NoteLineTest setIsFirstLine(boolean bool){
            isFirst = bool;
            return this;
        }

        public NoteLineTest setFormattedSpan(DocumentAssert doc, int ... idx){
            lineText = Optional.of(doc.getChild(FormatSpanMain.class, idx));
            return this;
        }

        @Override
        public void test(SpanBranch span){
            LinedSpanNote test = assertClass(span, LinedSpanNote.class);
            assertSpan("data", span, lineText, test.getFormattedSpan());
            assertEquals(getError("id", span), buildId, test.buildId());
            assertEquals(getError("isFirst", span), isFirst, test.isFirstLine());
            super.test(span);
        }
    }

    public static class PointerNoteTest extends SimpleStyleTest<PointerNoteTest>{
        private Optional<FormatSpanMain> lineText;

        public PointerNoteTest(){
            super(PointerNoteTest.class, null);
            lineText = Optional.empty();
        }

        public PointerNoteTest setFormattedSpan(DocumentAssert doc, int ... idx){
            lineText = Optional.of(doc.getChild(FormatSpanMain.class, idx));
            return this;
        }

        @Override
        public void setup(){
            setStyles(getLinedType(), getCatalogueStatus());
        }

        @Override
        public void test(SpanBranch span){
            LinedSpanPointNote test = assertClass(span, LinedSpanPointNote.class);
            assertSpan("data", span, lineText, test.getFormattedSpan());
            super.test(span);
        }
    }

    public static class QuoteLineTest extends SimpleStyleTest<QuoteLineTest>{
        private Optional<FormatSpanMain> lineText;

        public QuoteLineTest(){
            super(QuoteLineTest.class, LinedType.QUOTE);
            lineText = Optional.empty();
        }

        public QuoteLineTest setFormattedSpan(DocumentAssert doc, int ... idx){
            lineText = Optional.of(doc.getChild(FormatSpanMain.class, idx));
            return this;
        }

        @Override
        public void test(SpanBranch span){
            LinedSpanQuote test = assertClass(span, LinedSpanQuote.class);
            assertSpan("formatted", span, lineText, test.getFormattedSpan());
            super.test(span);
        }
    }
}
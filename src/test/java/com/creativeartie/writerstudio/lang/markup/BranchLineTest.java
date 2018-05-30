package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.function.*;

import java.util.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

public class BranchLineTest {
    private static abstract class LineTest<T extends LineTest<T>> extends
            SpanBranchAssert<T> {

        private int publishTotal;
        private int noteTotal;
        private LinedType linedType;

        public LineTest(Class<T> clazz, DocumentAssert doc,
                int publish, int note){
            super(clazz, doc);
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

        protected abstract LinedSpan moreTest(SpanBranch span,
                ArrayList<Executable> tests);

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            LinedSpan test = moreTest(span, tests);
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
        private int[] dataSpan;

        public CiteLineTest(DocumentAssert doc){
            super(CiteLineTest.class, doc, 0, 1);
            setLinedType(LinedType.SOURCE);
            dataSpan = null;
        }

        public CiteLineTest setInfoType(InfoFieldType type){
            infoType = type;
            return this;
        }

        public CiteLineTest setDataSpan(int ... indexes){
            dataSpan = indexes;
            return this;
        }

        @Override
        public void setup(){
            setLinedType(LinedType.SOURCE);
            setStyles(LinedType.SOURCE, infoType);
            if (dataSpan != null){
                addStyles(AuxiliaryType.DATA_ERROR);
            }
        }

        @Override
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanCite test = assertClass(LinedSpanCite.class);

            tests.add(() -> assertEquals(infoType, test.getInfoFieldType(),
                "getInfoFieldType()"));
            tests.add(() -> assertChild(SpanBranch.class, dataSpan,
                () -> test.getData(), "getData()"));
            return test;
        }
    }

    /** For when the style is just LinedType.*/
    private static abstract class SimpleStyleTest<T extends SimpleStyleTest<T>>
            extends LineTest<T>{
        private LinedType linedType;

        public SimpleStyleTest(Class<T> clazz, DocumentAssert doc,
                LinedType type, int publish, int note){
            super(clazz, doc, publish, note);
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

        public LevelLineTest(Class<T> clazz, DocumentAssert doc, int publish,
                int note){
            super(clazz, doc, null, publish, note);
            lineLevel = 1;
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
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanLevel test = testSubclass(span, tests);
            tests.add(() -> assertEquals(lineLevel, test.getLevel(), "getLevel"));
            tests.add(() -> assertChild(FormattedSpan.class, lineText,
                () -> test.getFormattedSpan(), "getFormatedSpan()"));
            return test;
        }
    }

    public static class HeadLevelLineTest extends
            LevelLineTest<HeadLevelLineTest>{

        private EditionType editionType;
        private String editionDetail;
        private String lookupText;

        public HeadLevelLineTest(DocumentAssert doc){
            super(HeadLevelLineTest.class, doc, 1, 0);
            editionType = EditionType.NONE;
            editionDetail = "";
        }

        /** For {@link LinedSpanLevelSection#getEditionType()} (default: NONE)*/
        public HeadLevelLineTest setEdition(EditionType edition){
            editionType = edition;
            lookupText = "";
            return this;
        }

        /** For {@link LinedSpanLevelSection#getEditionSpan()} (default: "")*/
        public HeadLevelLineTest setEditionSpan(String detail){
            editionDetail = detail;
            return this;
        }

        /** For {@link LinedSpanLevelSection#getLookupText()} (default: "")*/
        public HeadLevelLineTest setLookupText(String text){
            lookupText = text;
            return this;
        }

        @Override
        protected LinedSpanLevel testSubclass(SpanBranch span,
                ArrayList<Executable> tests){
            LinedSpanLevelSection test = assertClass(LinedSpanLevelSection.class);
            tests.add(() -> assertEquals(editionType, test.getEditionType(),
                "getEdtionType()"));
            tests.add(() -> assertEquals(editionDetail, test.getEditionDetail(),
                "getEditionDetail()"));
            tests.add(() -> assertEquals(lookupText, test.getLookupText(),
                "getLookupText()"));
            return test;
        }
    }

    public static class ListLevelLineTest extends
                LevelLineTest<ListLevelLineTest>{

        public ListLevelLineTest(DocumentAssert doc){
            super(ListLevelLineTest.class, doc, 1, 0);
        }

        protected LinedSpanLevel testSubclass(SpanBranch span,
                ArrayList<Executable> tests){
            return assertClass(LinedSpanLevelList.class);
        }
    }

    public static class PointerLinkTest extends LineTest<PointerLinkTest>{
        private String linkPath;
        private String lookupText;

        public PointerLinkTest(DocumentAssert doc){
            super(PointerLinkTest.class, doc, 1, 0);
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
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanPointLink test = assertClass(LinedSpanPointLink.class);
            tests.add(() -> assertEquals(linkPath, test.getPath(), "getPath()"));
            tests.add(() -> assertEquals(lookupText, test.getLookupText(),
                "getLookupText()"));
            return test;
        }
    }

    public static class BreakLineTest extends SimpleStyleTest<BreakLineTest>{

        public BreakLineTest(DocumentAssert doc){
            super(BreakLineTest.class, doc, LinedType.BREAK, 0, 0);
        }

        @Override
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            return assertClass(LinedSpanBreak.class);
        }
    }

    public static class AgendaLineTest extends SimpleStyleTest<AgendaLineTest>{
        private String agendaLine;

        public AgendaLineTest(DocumentAssert doc){
            super(AgendaLineTest.class, doc, LinedType.AGENDA, 0, 1);
        }

        /** For {@link LinedSpanAgenda#getAgenda()} (default: */
        public AgendaLineTest setAgenda(String agenda){
            agendaLine = agenda;
            return this;
        }

        @Override
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanAgenda test = assertClass(LinedSpanAgenda.class);
            tests.add(() -> assertEquals(agendaLine, test.getAgenda(),
                "getAgenda()"));
            return test;
        }
    }

    public static class ParagraphLineTest
            extends SimpleStyleTest<ParagraphLineTest>{

        private int[] lineText;

        public ParagraphLineTest(DocumentAssert doc){
            super(ParagraphLineTest.class, doc, LinedType.PARAGRAPH, 1, 0);
            lineText = null;
        }

        /** For {@link LinedSpanParagraph#getFormattedSpan()} (no default)*/
        public ParagraphLineTest setFormattedSpan(int ... idx){
            lineText = idx;
            return this;
        }

        @Override
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanParagraph test = assertClass(LinedSpanParagraph.class);
            tests.add(() -> assertChild(FormattedSpan.class, lineText, () ->
                test.getFormattedSpan(), "getFormattedSpan()"));
            return test;
        }
    }

    public static class NoteLineTest extends SimpleStyleTest<NoteLineTest>{

        private Optional<CatalogueIdentity> buildId;
        private int[] lineText;
        private String lookupText;

        public NoteLineTest(DocumentAssert doc){
            super(NoteLineTest.class, doc, LinedType.NOTE, 0, 1);
            buildId = Optional.empty();
            lineText = null;
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
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanNote test = assertClass(LinedSpanNote.class);
            tests.add(() -> assertEquals(buildId, test.buildId(), "buildId()"));
            tests.add(() -> assertEquals(lookupText, test.getLookupText(),
                "getLookupText()"));
            tests.add(() -> assertChild(FormattedSpan.class, lineText,
                () -> test.getFormattedSpan(), "getFormattedText()"));
            return test;
        }
    }

    public static class PointerNoteTest extends SimpleStyleTest<PointerNoteTest>{
        private int[] lineText;
        private String lookupText;

        public PointerNoteTest(DocumentAssert doc){
            super(PointerNoteTest.class, doc, null, 0, 0);
            lineText = null;
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
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanPointNote test = assertClass(LinedSpanPointNote.class);

            tests.add(() -> assertChild(FormattedSpan.class, lineText,
                () -> test.getFormattedSpan(), "getFormattedSpan()"));
            tests.add(() -> assertEquals(lookupText, test.getLookupText(),
                "getLookupText()"));
            return test;
        }
    }

    public static class QuoteLineTest extends SimpleStyleTest<QuoteLineTest>{
        private int[] lineText;

        public QuoteLineTest(DocumentAssert doc){
            super(QuoteLineTest.class, doc, LinedType.QUOTE, 1, 0);
            lineText = null;
        }

        /** For {@link LinedSpanQuote#getFormattedSpan()} (no default) */
        public QuoteLineTest setFormattedSpan(int ... indexes){
            lineText = indexes;
            return this;
        }


        @Override
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanQuote test = assertClass(LinedSpanQuote.class);
            tests.add(() -> assertChild(FormattedSpan.class, lineText,
                () -> test.getFormattedSpan(), "getFormattedSpan()"));
            return test;
        }
    }
}

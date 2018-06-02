package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.function.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static org.junit.jupiter.api.Assertions.*;

public class BranchLineAsserts {
    private static abstract class LineAssert<T extends LineAssert<T>> extends
            SpanBranchAssert<T> {

        private int publishTotal;
        private int noteTotal;
        private LinedType linedType;

        public LineAssert(Class<T> clazz, DocumentAssert doc,
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

    /** For when the style is just LinedType.*/
    private static abstract class SimpleStyleAssert<T extends SimpleStyleAssert<T>>
            extends LineAssert<T>{
        private LinedType linedType;

        public SimpleStyleAssert(Class<T> clazz, DocumentAssert doc,
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

    public static class AgendaLineAssert
        extends SimpleStyleAssert<AgendaLineAssert>
    {
        private String agendaLine;

        public AgendaLineAssert(DocumentAssert doc){
            super(AgendaLineAssert.class, doc, LinedType.AGENDA, 0, 1);
        }

        /** For {@link LinedSpanAgenda#getAgenda()} (default: */
        public AgendaLineAssert setAgenda(String agenda){
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

    public static class BreakLineAssert extends SimpleStyleAssert<BreakLineAssert>{

        public BreakLineAssert(DocumentAssert doc){
            super(BreakLineAssert.class, doc, LinedType.BREAK, 0, 0);
        }

        @Override
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            return assertClass(LinedSpanBreak.class);
        }
    }

    public static class CiteLineAssert extends LineAssert<CiteLineAssert>{

        private InfoFieldType infoType;
        private int[] dataSpan;

        public CiteLineAssert(DocumentAssert doc){
            super(CiteLineAssert.class, doc, 0, 1);
            setLinedType(LinedType.SOURCE);
            dataSpan = null;
            infoType = InfoFieldType.ERROR;
        }

        public CiteLineAssert setInfoType(InfoFieldType type){
            infoType = type;
            return this;
        }

        public CiteLineAssert setDataSpan(int ... indexes){
            dataSpan = indexes;
            return this;
        }

        @Override
        public void setup(){
            setLinedType(LinedType.SOURCE);
            setStyles(LinedType.SOURCE, infoType);
            if (dataSpan == null){
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

    public abstract static class LevelLineAssert
        <T extends SimpleStyleAssert<T>> extends SimpleStyleAssert<T>
    {
        private int lineLevel;
        private int[] lineText;

        public LevelLineAssert(Class<T> clazz, DocumentAssert doc, int publish,
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

    public static class ListLevelLineAssert extends
        LevelLineAssert<ListLevelLineAssert>
    {

        public ListLevelLineAssert(DocumentAssert doc){
            super(ListLevelLineAssert.class, doc, 1, 0);
        }

        protected LinedSpanLevel testSubclass(SpanBranch span,
                ArrayList<Executable> tests){
            return assertClass(LinedSpanLevelList.class);
        }
    }

    public static class HeadLevelLineAssert extends
            LevelLineAssert<HeadLevelLineAssert>{

        private EditionType editionType;
        private String editionDetail;
        private String lookupText;

        public HeadLevelLineAssert(DocumentAssert doc){
            super(HeadLevelLineAssert.class, doc, 1, 0);
            editionType = EditionType.NONE;
            editionDetail = "";
        }

        /** For {@link LinedSpanLevelSection#getEditionType()} (default: NONE)*/
        public HeadLevelLineAssert setEdition(EditionType edition){
            editionType = edition;
            lookupText = "";
            return this;
        }

        /** For {@link LinedSpanLevelSection#getEditionSpan()} (default: "")*/
        public HeadLevelLineAssert setDetail(String detail){
            editionDetail = detail;
            return this;
        }

        /** For {@link LinedSpanLevelSection#getLookupText()} (default: "")*/
        public HeadLevelLineAssert setLookup(String text){
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

    public static class NoteLineAssert extends SimpleStyleAssert<NoteLineAssert>{

        private Optional<CatalogueIdentity> buildId;
        private int[] lineText;
        private String lookupText;

        public NoteLineAssert(DocumentAssert doc){
            super(NoteLineAssert.class, doc, LinedType.NOTE, 0, 1);
            buildId = Optional.empty();
            lineText = null;
            lookupText = "";
        }

        /** For {@link LinedSpanNote#buildId()} (default: empty) */
        public NoteLineAssert setBuildId(IDBuilder builder){
            buildId = Optional.ofNullable(builder).map(found -> found.build());
            return this;
        }

        /** For {@link LinedSpanNote#getFormattedSpan()} (no default) */
        public NoteLineAssert setFormattedSpan(int ... indexes){
            lineText = indexes;
            return this;
        }

        /** For {@link LinedSpanNote#getLookupText()} (default: "") */
        public NoteLineAssert setLookup(String text){
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
    public static class ParagraphLineAssert
            extends SimpleStyleAssert<ParagraphLineAssert>{

        private int[] lineText;

        public ParagraphLineAssert(DocumentAssert doc){
            super(ParagraphLineAssert.class, doc, LinedType.PARAGRAPH, 1, 0);
            lineText = null;
        }

        /** For {@link LinedSpanParagraph#getFormattedSpan()} (no default)*/
        public ParagraphLineAssert setFormattedSpan(int ... idx){
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


    public static class PointerLinkAssert extends LineAssert<PointerLinkAssert>{
        private String linkPath;
        private String lookupText;

        public PointerLinkAssert(DocumentAssert doc){
            super(PointerLinkAssert.class, doc, 0, 0);
            linkPath = "";
            lookupText = "";
        }

        /** For {@link LinedSpanPointLink#getPath()} (default: "")*/
        public PointerLinkAssert setPath(String path){
            linkPath = path;
            return this;
        }

        /** For {@link LinedSpanPointLink#getLookupText()} (default: "")*/
        public PointerLinkAssert setLookup(String lookup){
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

    public static class PointerNoteAssert extends LineAssert<PointerNoteAssert>{
        private int[] lineText;
        private String lookupText;

        public PointerNoteAssert(DocumentAssert doc){
            super(PointerNoteAssert.class, doc, 0, 0);
            lineText = null;
            lookupText = "";
        }

        /** For {@link LinedSpanPointNote#getFormattedSpan()} (no default) */
        public PointerNoteAssert setFormattedSpan(int ... indexes){
            lineText = indexes;
            return this;
        }

        /** For {@link LinedSpanPointNote#getLookupText()} (default: "")*/
        public PointerNoteAssert setLookup(String lookup){
            lookupText = lookup;
            return this;
        }

        @Override
        public void setup(){
            addStyles(getLinedType(), getCatalogueStatus());
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

    public static class QuoteLineAssert extends SimpleStyleAssert<QuoteLineAssert>{
        private int[] lineText;

        public QuoteLineAssert(DocumentAssert doc){
            super(QuoteLineAssert.class, doc, LinedType.QUOTE, 1, 0);
            lineText = null;
        }

        /** For {@link LinedSpanQuote#getFormattedSpan()} (no default) */
        public QuoteLineAssert setFormattedSpan(int ... indexes){
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

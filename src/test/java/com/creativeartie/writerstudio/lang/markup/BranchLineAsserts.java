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

        public LineAssert(Class<T> clazz, DocumentAssert doc,
                int publish, int note){
            super(clazz, doc);
            publishTotal = publish;
            noteTotal = note;
        }

        /** For {@link LinedSpan#getPublishTotal()} (default: depends). */
        public final T setPublish(int count){
            publishTotal = count;
            return cast();
        }

        /** For {@link LinedSpan#getNoteTotal()} (default: depends). */
        public final T setNote(int count){
            noteTotal = count;
            return cast();
        }

        @Override
        public final void setup(){
            moreSetup();
        }

        protected abstract void moreSetup();

        @Override
        public final void test(SpanBranch span, ArrayList<Executable> tests){
            LinedSpan test = moreTest(span, tests);
            tests.add(() -> assertEquals(publishTotal, test.getPublishTotal(),
                "getPublishTotal()"));
            tests.add(() -> assertEquals(noteTotal, test.getNoteTotal(),
                "getNoteTotal()"));
        }

        protected abstract LinedSpan moreTest(SpanBranch span,
                ArrayList<Executable> tests);
    }

    public static class AgendaLineAssert extends LineAssert<AgendaLineAssert> {
        private String agendaLine;

        public AgendaLineAssert(DocumentAssert doc){
            super(AgendaLineAssert.class, doc, 0, 1);
        }

        /** For {@link LinedSpanAgenda#getAgenda()} (default: */
        public AgendaLineAssert setAgenda(String agenda){
            agendaLine = agenda;
            return this;
        }

        @Override
        public void moreSetup(){
            setCatalogued();
            setId(true);
        }

        @Override
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanAgenda test = assertClass(LinedSpanAgenda.class);
            tests.add(() -> assertEquals(agendaLine, test.getAgenda(),
                "getAgenda()"));
            return test;
        }
    }

    public static final class BreakLineAssert
            extends LineAssert<BreakLineAssert>{

        public BreakLineAssert(DocumentAssert doc){
            super(BreakLineAssert.class, doc, 0, 0);
        }

        @Override
        public void moreSetup(){}

        @Override
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            return assertClass(LinedSpanBreak.class);
        }
    }

    public static final class CiteLineAssert extends LineAssert<CiteLineAssert>{

        private InfoFieldType infoType;
        private int[] dataSpan;
        private Class<? extends SpanBranch> dataClass;

        public CiteLineAssert(DocumentAssert doc){
            super(CiteLineAssert.class, doc, 0, 1);
            dataSpan = null;
            infoType = InfoFieldType.ERROR;
            dataClass = null;

        }

        public CiteLineAssert setInfoType(InfoFieldType type){
            infoType = type;
            return this;
        }

        public CiteLineAssert setDataSpan(int ... indexes){
            dataSpan = indexes;
            return this;
        }

        public CiteLineAssert setDataClass(Class<? extends SpanBranch> clazz){
            dataClass = clazz;
            return this;
        }

        @Override
        public void moreSetup(){
            assert (dataClass == null && dataSpan == null) ||
                (dataClass != null && dataSpan != null);
            setCatalogued();
            setId(false);
        }

        @Override
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanCite test = assertClass(LinedSpanCite.class);

            tests.add(() -> assertEquals(infoType, test.getInfoFieldType(),
                "getInfoFieldType()"));

            Class<? extends SpanBranch> clazz = dataClass == null?
                SpanBranch.class : dataClass;
            tests.add(() -> assertChild(clazz, dataSpan,
                () -> test.getData(), "getData()"));
            return test;
        }
    }

    public abstract static class LevelLineAssert<T extends LineAssert<T>>
        extends LineAssert<T>{

        private int lineLevel;
        private int[] lineText;

        public LevelLineAssert(Class<T> clazz, DocumentAssert doc, int publish,
                int note){
            super(clazz, doc, publish, note);
            lineLevel = 1;
            lineText = null;
        }

        /** For {@link LinedSpanLevel#getLevel()} (default: 1)*/
        public final T setLevel(int level){
            lineLevel = level;
            return cast();
        }

        /** For {@link LinedSpanLevel#getForamtedSpan()} (no default) */
        public final T setFormattedSpan(int... indexes){
            lineText = indexes;
            return cast();
        }

        @Override
        public void moreSetup(){
            setupSubclass();
        }

        protected abstract void setupSubclass();

        protected abstract LinedSpanLevel testSubclass(SpanBranch span,
            ArrayList<Executable> tests);

        @Override
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanLevel test = testSubclass(span, tests);
            tests.add(() -> assertEquals(lineLevel, test.getLevel(), "getLevel()"));
            tests.add(() -> assertChild(FormattedSpan.class, lineText,
                () -> test.getFormattedSpan(), "getFormatedSpan()"));
            return test;
        }
    }

    public static final class ListLevelLineAssert extends
            LevelLineAssert<ListLevelLineAssert>{
        private boolean isNumbered;

        public ListLevelLineAssert(DocumentAssert doc){
            super(ListLevelLineAssert.class, doc, 1, 0);
            isNumbered = true;
        }

        /** For {@link LinedSpanLevelList#isNumbererd()} (default {@code true}) */
        public ListLevelLineAssert setNumbered(boolean numbered){
            isNumbered = numbered;
            return this;
        }

        @Override
        protected void setupSubclass(){}

        @Override
        protected LinedSpanLevel testSubclass(SpanBranch span,
                ArrayList<Executable> tests){
            LinedSpanLevelList test = assertClass(LinedSpanLevelList.class);
            tests.add(() -> assertEquals(isNumbered, test.isNumbered(),
                "isNumbered()"));
            return test;
        }
    }

    public static final class HeadLevelLineAssert extends
            LevelLineAssert<HeadLevelLineAssert>{

        private EditionType editionType;
        private String editionDetail;
        private String lookupText;
        private boolean isHeading;

        public HeadLevelLineAssert(DocumentAssert doc){
            super(HeadLevelLineAssert.class, doc, 1, 0);
            editionType = EditionType.NONE;
            editionDetail = "";
            lookupText = "";
            isHeading = true;
        }

        /** For {@link LinedSpanLevelSection#getEditionType()} (default: NONE)*/
        public HeadLevelLineAssert setEdition(EditionType edition){
            editionType = edition;
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

        /** For {@link LinedSpanLevelSection#isHeading()} (default: {@code true)*/
        public HeadLevelLineAssert setHeading(boolean heading){
            isHeading = heading;
            return this;
        }

        @Override
        protected void setupSubclass(){
            setCatalogued();
            setId(true);
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
            tests.add(() -> assertEquals(isHeading, test.isHeading(),
                "isHeading()"));
            return test;
        }
    }

    public static final class NoteLineAssert extends LineAssert<NoteLineAssert>{

        private Optional<CatalogueIdentity> buildId;
        private int[] lineText;
        private String lookupText;

        public NoteLineAssert(DocumentAssert doc){
            super(NoteLineAssert.class, doc, 0, 1);
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
        public void moreSetup(){}

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
    public static final class ParagraphLineAssert
            extends LineAssert<ParagraphLineAssert>{

        private int[] lineText;

        public ParagraphLineAssert(DocumentAssert doc){
            super(ParagraphLineAssert.class, doc, 1, 0);
            lineText = null;
        }

        /** For {@link LinedSpanParagraph#getFormattedSpan()} (no default)*/
        public ParagraphLineAssert setFormattedSpan(int ... idx){
            lineText = idx;
            return this;
        }

        @Override
        public void moreSetup(){}

        @Override
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanParagraph test = assertClass(LinedSpanParagraph.class);
            tests.add(() -> assertChild(FormattedSpan.class, lineText, () ->
                test.getFormattedSpan(), "getFormattedSpan()"));
            return test;
        }
    }


    public static final class PointerLinkAssert
            extends LineAssert<PointerLinkAssert>{
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
        public void moreSetup(){
            setCatalogued();
            setId(true);
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

    public static final class PointerNoteAssert
            extends LineAssert<PointerNoteAssert>{
        private int[] lineText;
        private String lookupText;
        private DirectoryType idType;

        public PointerNoteAssert(DocumentAssert doc){
            super(PointerNoteAssert.class, doc, 0, 0);
            lineText = null;
            lookupText = "";
            idType = DirectoryType.ENDNOTE;
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

        public PointerNoteAssert setType(DirectoryType type){
            idType = type;
            return this;
        }

        @Override
        public void moreSetup(){
            setCatalogued();
            setId(true);
        }

        @Override
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanPointNote test = assertClass(LinedSpanPointNote.class);

            tests.add(() -> assertChild(FormattedSpan.class, lineText,
                () -> test.getFormattedSpan(), "getFormattedSpan()"));
            tests.add(() -> assertEquals(lookupText, test.getLookupText(),
                "getLookupText()"));
            tests.add(() -> assertEquals(idType, test.getDirectoryType(),
                "getDirectoryType()"));
            return test;
        }
    }

    public static class QuoteLineAssert extends LineAssert<QuoteLineAssert>{
        private int[] lineText;

        public QuoteLineAssert(DocumentAssert doc){
            super(QuoteLineAssert.class, doc, 1, 0);
            lineText = null;
        }

        /** For {@link LinedSpanQuote#getFormattedSpan()} (no default) */
        public QuoteLineAssert setFormattedSpan(int ... indexes){
            lineText = indexes;
            return this;
        }

        @Override
        public void moreSetup(){}

        @Override
        public LinedSpan moreTest(SpanBranch span, ArrayList<Executable> tests){
            LinedSpanQuote test = assertClass(LinedSpanQuote.class);
            tests.add(() -> assertChild(FormattedSpan.class, lineText,
                () -> test.getFormattedSpan(), "getFormattedSpan()"));
            return test;
        }
    }
}

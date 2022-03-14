package com.creativeartie.writer.lang.markup;

import org.junit.jupiter.api.function.*;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static org.junit.jupiter.api.Assertions.*;

/** Branch asserts for other {@link SpanBranch}. */
public class BranchBasicAsserts {

    static abstract class ContentBasicAssert<T extends ContentBasicAssert<T>>
            extends SpanBranchAssert<T> {
        private String trimText;
        private String renderText;
        private boolean isBegin;
        private boolean isEnd;

        protected ContentBasicAssert(Class<T> clazz, DocumentAssert doc){
            super(clazz, doc);
            isBegin = false;
            isEnd = false;
            trimText = "";
            renderText = "";
        }

        /** For {@link BasicText#getTrimmed()} and
         * {@link BasicText#getRendered()} (default: {@code ""}) */
        public final T setBoth(String text){
            renderText = text;
            trimText = text.trim();
            return cast();
        }

        /** For {@link BasicText#isSpaceBegin()} (default: {@code false}) */
        public final T setBegin(boolean b){
            isBegin = b;
            return cast();
        }

        /** For {@link BasicText#isSpaceEnd()}  (default: {@code false}) */
        public final T setEnd(boolean b){
            isEnd = b;
            return cast();
        }

        @Override
        public final void setup(){
            moreSetup();
        }

        protected abstract void moreSetup();

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            BasicText test = moreTest(span, tests);
            tests.add(() -> assertEquals(renderText, test.getRendered(),  "getRendered()"));
            tests.add(() -> assertEquals(trimText,   test.getTrimmed(),   "getTrimmed()"));
            tests.add(() -> assertEquals(isBegin,    test.isSpaceBegin(), "isSpaceBegin()"));
            tests.add(() -> assertEquals(isEnd,      test.isSpaceEnd(),   "isSpaceEnd()"));
        }

        protected abstract BasicText moreTest(SpanBranch span,
            ArrayList<Executable> tests);
    }

    public static final class EscapeAssert extends SpanBranchAssert<EscapeAssert>{
        private String textEscape;

        public EscapeAssert(DocumentAssert doc){
            super(EscapeAssert.class, doc);
            textEscape = "";
        }

        public void setup(){}

        /** For {@link BasicTextEscape#getEscape()}  (default: {@code ""}) */
        public EscapeAssert setEscape(String escape){
            textEscape = escape;
            return this;
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            BasicTextEscape test = assertClass(BasicTextEscape.class);

            tests.add(() -> assertEquals(textEscape, test.getEscape(),
                "getEscape()"));
        }
    }

    public static final class ContentAssert extends
            ContentBasicAssert<ContentAssert>{
        private int wordCount;

        public ContentAssert(DocumentAssert doc){
            super(ContentAssert.class, doc);
            wordCount = 1;
        }

        /** For {@link ContentSpan#getWordCount()}  (default: {@code 1}) */
        public ContentAssert setCount(int count){
            wordCount = count;
            return this;
        }

        @Override public void moreSetup(){}

        @Override
        public BasicText moreTest(SpanBranch span, ArrayList<Executable> tests){
            ContentSpan test = assertClass(ContentSpan.class);
            tests.add( () -> assertEquals(wordCount, test.getWordCount(),
                "getWordCount()"));
            return test;
        }
    }

    public static final class DirectoryAssert
            extends SpanBranchAssert<DirectoryAssert>{

        private DirectoryType idPurpose;
        private CatalogueIdentity produceId;
        private String lookupText;

        public DirectoryAssert(DocumentAssert doc){
            super(DirectoryAssert.class, doc);
            idPurpose = DirectoryType.LINK;
            produceId = null;
            lookupText = "";
        }

        /** For {@link DirectorySpan#getPurposeType()}
         * (default: {@link DirectoyType.LINK})
         */
        public DirectoryAssert setPurpose(DirectoryType t){
            idPurpose = t;
            return this;
        }

        /** For {@link DirectorySpan#buildId()} (default: {@code none}). */
        public DirectoryAssert setIdentity(IDBuilder builder){
            produceId = builder.build();
            return this;
        }

        /** For {@link DirectorySpan#buildId()} (default: {@code ""}). */
        public DirectoryAssert setLookup(String text){
            lookupText = text;
            return this;
        }

        @Override public void setup(){}

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            DirectorySpan test = assertClass(DirectorySpan.class);

            tests.add(() -> assertEquals(idPurpose, test.getPurposeType(),
                "getPurposeType()"));
            tests.add(() -> assertEquals(produceId, test.buildId(),
                "buildId()"));
            tests.add(() -> assertEquals(lookupText, test.getLookupText(),
                "getLookupText()"));
        }
    }

    public static final class EditionAssert
            extends SpanBranchAssert<EditionAssert>{

        private EditionType editionType;
        private String detailText;

        public EditionAssert(DocumentAssert doc){
            super(EditionAssert.class, doc);
            editionType = EditionType.OTHER;
            detailText = "";
        }

        /** For {@link EditionSpan#getEditionType()} (default: {@code OTHER}). */
        public EditionAssert setEdition(EditionType edition){
            editionType = edition;
            return this;
        }

        /** For {@link EditionSpan#getDetail()} (default: {@code ""}). */
        public EditionAssert setDetail(String text){
            detailText = text;
            return this;
        }

        @Override
        public void setup(){}

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            EditionSpan test = assertClass(EditionSpan.class);

            tests.add(() -> assertEquals(editionType, test.getEditionType(),
                "getEditionType()"));
            tests.add(() -> assertEquals(detailText, test.getDetail(),
                "getDetail()"));
        }
    }

    public static final class FormatAgendaAssert extends
            SpanBranchAssert<FormatAgendaAssert>{
        private String agendaText;

        public FormatAgendaAssert(DocumentAssert doc){
            super(FormatAgendaAssert.class, doc);
            agendaText = "";
        }

        /** For {@link EditionSpan#getDetail()} (default: {@code ""}). */
        public FormatAgendaAssert setAgenda(String agenda){
            agendaText = agenda;
            return this;
        }

        @Override
        public void setup(){
            setCatalogued();
            setId(true);
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            FormatSpanAgenda test = assertClass(FormatSpanAgenda.class);

            assertEquals(agendaText, test.getAgenda(), "getAgenda()");
        }
    }

    private BranchBasicAsserts(){}
}

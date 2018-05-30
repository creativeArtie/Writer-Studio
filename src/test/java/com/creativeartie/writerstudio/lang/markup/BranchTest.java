package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.function.*;

import java.util.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

/** Branch asserts for other {@link SpanBranch}. */
public class BranchTest {

    static abstract class ContentBasicTest<T extends ContentBasicTest<T>> extends
            SpanBranchAssert<T> {
        private String trimText;
        private String renderText;
        private boolean isBegin;
        private boolean isEnd;

        protected ContentBasicTest(Class<T> clazz, DocumentAssert doc){
            super(clazz, doc);
            isBegin = false;
            isEnd = false;
            trimText = "";
            renderText = "";
        }

        /** For {@link BasicText#getTrimmed()} and
         * {@link BasicText#getRendered()} (default: {@code ""}) */
        public T setBoth(String text){
            renderText = text;
            trimText = text.trim();
            return cast();
        }

        /** For {@link BasicText#isSpaceBegin()} (default: {@code false}) */
        public T setBegin(boolean b){
            isBegin = b;
            return cast();
        }

        /** For {@link BasicText#isSpaceEnd()}  (default: {@code false}) */
        public T setEnd(boolean b){
            isEnd = b;
            return cast();
        }

        protected abstract BasicText moreTest(SpanBranch span,
            ArrayList<Executable> tests);

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            BasicText test = moreTest(span, tests);
            tests.add(() -> assertEquals(trimText, test.getRendered(),  "getRendered()"));
            tests.add(() -> assertEquals(trimText, test.getTrimmed(),   "getTrimmed()"));
            tests.add(() -> assertEquals(isBegin,  test.isSpaceBegin(), "isSpaceBegin()"));
            tests.add(() -> assertEquals(isEnd,    test.isSpaceEnd(),   "isSpaceEnd()"));
        }
    }

    public static class ContentTest extends ContentBasicTest<ContentTest>{
        private int wordCount;

        public ContentTest(DocumentAssert doc){
            super(ContentTest.class, doc);
            wordCount = 1;
        }

        /** For {@link ContentSpan#getWordCount()}  (default: {@code 1}) */
        public ContentTest setCount(int count){
            wordCount = count;
            return this;
        }

        @Override
        public BasicText moreTest(SpanBranch span, ArrayList<Executable> tests){
            ContentSpan test = assertClass(ContentSpan.class);
            tests.add( () -> assertEquals(wordCount, test.getWordCount(),
                "getWordCount()"));
            return test;
        }
    }

    public static class EscapeTest extends SpanBranchAssert<EscapeTest>{
        private String textEscape;

        public EscapeTest(DocumentAssert doc){
            super(EscapeTest.class, doc);
            textEscape = "";
        }

        public void setup(){
            setStyles(AuxiliaryType.ESCAPE);
        }

        /** For {@link BasicTextEscape#getEscape()}  (default: {@code ""}) */
        public EscapeTest setEscape(String escape){
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

    public static class DirectoryTest extends SpanBranchAssert<DirectoryTest>{

        private DirectoryType idPurpose;
        private CatalogueIdentity produceId;
        private String lookupText;

        public DirectoryTest(DocumentAssert doc){
            super(DirectoryTest.class, doc);
            idPurpose = DirectoryType.LINK;
            produceId = null;
            lookupText = "";
        }

        /** For {@link DirectorySpan#getPurposeType()}
         * (default: {@link DirectoyType.LINK})
         */
        public DirectoryTest setPurpose(DirectoryType t){
            idPurpose = t;
            return this;
        }

        /** For {@link DirectorySpan#buildId()} (default: {@code none}). */
        public DirectoryTest setIdentity(IDBuilder builder){
            produceId = builder.build();
            return this;
        }

        /** For {@link DirectorySpan#buildId()} (default: {@code ""}). */
        public DirectoryTest setLookup(String text){
            lookupText = text;
            return this;
        }


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

    public static class EditionTest extends SpanBranchAssert<EditionTest>{

        private EditionType editionType;
        private String detailText;

        public EditionTest(DocumentAssert doc){
            super(EditionTest.class, doc);
            editionType = EditionType.OTHER;
            detailText = "";
        }

        /** For {@link EditionSpan#getEditionType()} (default: {@code OTHER}). */
        public EditionTest setEdition(EditionType edition){
            editionType = edition;
            return this;
        }

        /** For {@link EditionSpan#getDetail()} (default: {@code ""}). */
        public EditionTest setDetail(String text){
            detailText = text;
            return this;
        }

        @Override
        public void setup(){
            addStyles(editionType);
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            EditionSpan test = assertClass(EditionSpan.class);

            tests.add(() -> assertEquals(editionType, test.getEditionType(),
                "getEditionType()"));
            tests.add(() -> assertEquals(detailText, test.getDetail(),
                "getDetail()"));
        }
    }

    public static class FormatAgendaTest extends
            SpanBranchAssert<FormatAgendaTest>{
        private String agendaText;

        public FormatAgendaTest(DocumentAssert doc){
            super(FormatAgendaTest.class, doc);
            agendaText = "";
        }

        /** For {@link EditionSpan#getDetail()} (default: {@code ""}). */
        public FormatAgendaTest setAgenda(String agenda){
            agendaText = agenda;
            return this;
        }

        @Override
        public void setup(){
            setStyles(AuxiliaryType.AGENDA);
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            FormatSpanAgenda test = assertClass(FormatSpanAgenda.class);

            assertEquals(agendaText, test.getAgenda(), "getAgenda()");
        }
    }

    public static class FieldTest extends SpanBranchAssert<FieldTest>{
        private InfoFieldType fieldType;

        public FieldTest(DocumentAssert doc){
            super(FieldTest.class, doc);
            fieldType = InfoFieldType.ERROR;
        }

        /** For {@link EditionSpan#getDetail()} (default: {@code ERROR}). */
        public FieldTest setType(InfoFieldType type){
            fieldType = type;
            return this;
        }

        @Override
        public void setup(){
            addStyles(fieldType);
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            InfoFieldSpan test = assertClass(InfoFieldSpan.class);

            tests.add(() -> assertEquals(fieldType, test.getInfoFieldType(),
                "getInfoFieldType()"));
        }
    }

    private BranchTest(){}
}

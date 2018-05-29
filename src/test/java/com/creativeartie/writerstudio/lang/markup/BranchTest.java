package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.function.*;

import java.util.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

/** Branch asserts for other {@link SpanBranch}. */
public class BranchTest {

    static class ContentBasicTest<T extends ContentBasicTest<T>> extends
        SpanBranchAssert<T>
    {
        private String trimText;
        private boolean isBegin;
        private boolean isEnd;

        protected ContentBasicTest(Class<T> clazz){
            super(clazz);
            isBegin = false;
            isEnd = false;
            trimText = "";
        }

        /** For {@link BasicText#getTrimmed()} (default: {@code ""}) */
        public T setTrimmed(String str){
            text = str;
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

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            BasicText test = (BasicText) span;
            tests.add(() -> assertEquals(trimText, test.getTrimmed(),   "getTrimmed()"));
            tests.add(() -> assertEquals(isBegin,  test.isSpaceBegin(), "isSpaceBegin()"));
            tests.add(() -> assertEquals(isEnd,    test.isSpaceEnd(),   "isSpaceEnd()"));
        }
    }

    public static class ContentTest extends ContentBasicTest<ContentTest>{
        private int wordCount;

        public ContentTest(){
            super(ContentTest.class);
            wordCount = 1;
        }

        /** For {@link ContentSpan#wordCount()}  (default: {@code 1}) */
        public ContentTest setCount(int count){
            size = count;
            return this;
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            ContentSpan test = assertClass(ContentSpan.class);
            tests.addAll( () -> assertEquals(size, test.wordCount(), "wordCount()"));
            super.test(span, tests);
        }
    }

    public static class EscapeTest extends SpanBranchAssert<EscapeTest>{
        private String textEscape;

        public EscapeTest(){
            super(EscapeTest.class);
            textEscape = "";
        }

        public void setup(){
            setStyles(AuxiliaryType.ESCAPE);
        }

        /** For {@link BasicTextEscape#getEscape()}  (default: {@code ""}) */
        public EscapeTest setEscape(String str){
            escape = str;
            return this;
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            BasicTextEscape test = assertClass(BasicTextEscape.class);

            tests.add(() -> assertEquals(escape, test.getEscape(), "getEscape()"));
        }
    }

    public static class DirectoryTest extends SpanBranchAssert<DirectoryTest>{

        private DirectoryType idPurpose;
        private CatalogueIdentity produceId;

        public DirectoryTest(){
            super(DirectoryTest.class);
            idPurpose = DirectoryType.LINK;
            produceId = null;
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

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            DirectorySpan test = assertClass(DirectorySpan.class);

            tests.add(() -> assertEquals(idPurpose,  test.getPurposeType(), "idPurpose"));
            tests.add(() -> assertEquals(produceId, test.buildId(),        "id"));
        }
    }

    public static class EditionTest extends SpanBranchAssert<EditionTest>{

        private EditionType editionType;
        private String detailText;

        public EditionTest(){
            super(EditionTest.class);
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
            setStyles(edition);
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            EditionSpan test = assertClass(EditionSpan.class);

            tests.add(() -> assertEquals(editionType, test.getEditionType(), "getEditionType()"));
            tests.add(() -> assertEquals(detailText,  test.getDetail(),      "getDetail()"));
        }
    }

    public static class FormatAgendaTest extends
            SpanBranchAssert<FormatAgendaTest>{
        private String agendaText;

        public FormatAgendaTest(){
            super(FormatAgendaTest.class);
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

        public FieldTest(){
            super(FieldTest.class);
            fieldType = InfoFieldType.ERROR;
        }

        /** For {@link EditionSpan#getDetail()} (default: {@code ERROR}). */
        public FieldTest setType(InfoFieldType type){
            fieldType = type;
            return this;
        }

        @Override
        public void setup(){
            setStyles(FormatTypeField);
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            InfoFieldSpan test = assertClass(InfoFieldSpan.class);

            tests.add(() -> assertEquals(fieldType, test.getFormatTypeField(),
                "getFormatTypeField()"));
        }
    }

    private BranchTest(){}
}

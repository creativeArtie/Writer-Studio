package com.creativeartie.writerstudio.lang.markup;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.*;

import static org.junit.Assert.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

public class BranchTest {

    static class ContentBasicTest<T extends ContentBasicTest<T>> extends
        SpanBranchAssert<T>
    {
        private String text;
        private boolean isBegin;
        private boolean isEnd;

        protected ContentBasicTest(Class<T> clazz){
            super(clazz);
        }

        public T setText(String str){
            text = str;
            return cast();
        }

        public T setBegin(boolean b){
            isBegin = b;
            return cast();
        }

        public T setEnd(boolean b){
            isEnd = b;
            return cast();
        }

        @Override
        public void test(SpanBranch span){
            BasicText test = (BasicText) span;
            assertEquals(getError("parsed", test),  text,  test.getTrimmed());
            assertEquals(getError("begin", test), isBegin, test.isSpaceBegin());
            assertEquals(getError("end", test),   isEnd,   test.isSpaceEnd());
        }
    }

    public static class ContentTest extends ContentBasicTest<ContentTest>{
        private int size;

        public ContentTest(){
            super(ContentTest.class);
        }

        public ContentTest setCount(int i){
            size = i;
            return this;
        }

        @Override
        public void test(SpanBranch span){
            ContentSpan test = assertClass(span, ContentSpan.class);
            assertEquals(getError("count", span), size, test.wordCount());
            super.test(span);
        }
    }

    public static class EscapeTest extends SpanBranchAssert<EscapeTest>{
        private String escape;

        public EscapeTest(){
            super(EscapeTest.class);
        }

        public void setup(){
            setStyles(AuxiliaryType.ESCAPE);
        }

        public EscapeTest setEscape(String str){
            escape = str;
            return this;
        }

        @Override
        public void test(SpanBranch span){
            BasicTextEscape test = assertClass(span, BasicTextEscape.class);

            assertEquals("Wrong escape.", escape, test.getEscape());
        }
    }

    public static class DirectoryTest extends SpanBranchAssert<DirectoryTest>{

        private DirectoryType purpose;
        private CatalogueIdentity produces;

        public DirectoryTest(){
            super(DirectoryTest.class);
        }

        public DirectoryTest setPurpose(DirectoryType t){
            purpose = t;
            return this;
        }

        public DirectoryTest setIdentity(IDBuilder builder){
            produces = builder.build();
            return this;
        }

        @Override
        public void test(SpanBranch span){
            DirectorySpan test = assertClass(span, DirectorySpan.class);

            assertEquals(getError("purpose", test), purpose, test.getPurpose());
            assertEquals(getError("id",      test), produces,   test.buildId());
        }
    }

    public static class EditionTest extends SpanBranchAssert<EditionTest>{

        private EditionType edition;
        private String text;

        public EditionTest(){
            super(EditionTest.class);
        }

        public EditionTest setEdition(EditionType t){
            edition = t;
            return this;
        }

        public EditionTest setText(String str){
            text = str;
            return this;
        }

        @Override
        public void setup(){
            setStyles(edition);
        }

        @Override
        public void test(SpanBranch span){
            EditionSpan test = assertClass(span, EditionSpan.class);

            assertEquals(getError("edition", test), edition, test.getEdition());
            assertEquals(getError("detail", test),  text, test.getDetail());
        }
    }

    public static class FormatAgendaTest extends
            SpanBranchAssert<FormatAgendaTest>{
        private String text;

        public FormatAgendaTest(){
            super(FormatAgendaTest.class);
        }

        public FormatAgendaTest setText(String str){
            text = str;
            return this;
        }

        @Override
        public void setup(){
            setStyles(AuxiliaryType.AGENDA);
        }

        @Override
        public void test(SpanBranch span){
            FormatSpanAgenda test = assertClass(span, FormatSpanAgenda.class);

            assertEquals(getError("agenda", test), text, test.getAgenda());
        }
    }

    public static class FieldTest extends SpanBranchAssert<FieldTest>{
        private InfoFieldType fieldType;

        public FieldTest(){
            super(FieldTest.class);
        }

        public FieldTest setType(InfoFieldType type){
            fieldType = type;
            return this;
        }

        @Override
        public void setup(){
            setStyles(fieldType);
        }

        @Override
        public void test(SpanBranch span){
            InfoFieldSpan test = assertClass(span, InfoFieldSpan.class);

            assertEquals(getError("type", test), fieldType, test.getFieldType());
        }
    }

    private BranchTest(){}
}
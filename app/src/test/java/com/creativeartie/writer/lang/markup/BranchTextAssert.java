package com.creativeartie.writer.lang.markup;

import org.junit.jupiter.api.function.*;

import java.util.*;

import com.creativeartie.writer.lang.*;

import static org.junit.jupiter.api.Assertions.*;

public class BranchTextAssert {

    static abstract class TextLineAssert<T extends TextLineAssert<?>>
            extends SpanBranchAssert<T>{

        private Class<? extends SpanBranch> dataClass;
        private int[] dataValue;
        private TextType rowType;
        private TextDataType dataType;

        private TextLineAssert(Class<T> cast, DocumentAssert doc,
                Class<? extends SpanBranch> clazz){
            super(cast, doc);
            dataClass = clazz;
            dataValue = null;
            dataType = TextDataType.LEFT;
        }

        public final T setRow(TextType type){
            rowType = type;
            return cast();
        }

        public final T setData(int ... indexes){
            dataValue = indexes;
            return cast();
        }

        public final T setType(TextDataType type){
            dataType = type;
            return cast();
        }

        @Override
        public void setup(){
            moreSetup();
        }

        protected abstract void moreSetup();

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            TextSpan<? extends SpanBranch> test = moreTest(span, tests);
            tests.add(() -> assertEquals(rowType, test.getRowType()));
            tests.add(() -> assertEquals(dataType, test.getDataType()));
            tests.add(() -> assertChild(dataClass, dataValue,
                () -> test.getData(), "getData()"));
        }

        protected abstract TextSpan<? extends SpanBranch> moreTest(
            SpanBranch span, ArrayList<Executable> tests);
    }

    public static final class UnknownAssert
        extends TextLineAssert<UnknownAssert>
    {

        public UnknownAssert(TextFileTest.TextFileAssert doc){
            this(doc.getAsserter());
        }

        public UnknownAssert(DocumentAssert doc){
            super(UnknownAssert.class, doc, ContentSpan.class);
            setRow(TextSpanUnkown.TYPE);
        }

        @Override
        protected void moreSetup(){}

        @Override
        protected TextSpanUnkown moreTest(SpanBranch span,
                ArrayList<Executable> tests){
            return assertClass(TextSpanUnkown.class);
        }
    }

    public static final class InfoLineAssert
        extends TextLineAssert<InfoLineAssert>
    {

        public InfoLineAssert(TextFileTest.TextFileAssert doc){
            this(doc.getAsserter());
        }

        public InfoLineAssert(DocumentAssert doc){
            super(InfoLineAssert.class, doc, ContentSpan.class);
            setType(TextDataType.TEXT);
        }

        @Override
        protected void moreSetup(){}

        @Override
        protected TextSpanInfo moreTest(SpanBranch span,
                ArrayList<Executable> tests){
            return assertClass(TextSpanInfo.class);
        }
    }

    public static final class MatterLineAssert
            extends TextLineAssert<MatterLineAssert>{

        private int lineIndex;

        public MatterLineAssert(TextFileTest.TextFileAssert doc){
            this(doc.getAsserter());
        }

        public MatterLineAssert(DocumentAssert doc){
            super(MatterLineAssert.class, doc, FormattedSpan.class);
            lineIndex = 0;
        }

        public MatterLineAssert setIndex(int index){
            lineIndex = index;
            return this;
        }

        @Override
        protected void moreSetup(){}

        @Override
        protected TextSpanMatter moreTest(SpanBranch span,
                ArrayList<Executable> tests){
            TextSpanMatter test =  assertClass(TextSpanMatter.class);
            tests.add(() -> assertEquals(lineIndex,
                test.getIndex(), "getIndex()"));
            return test;
        }
    }
}

package com.creativeartie.jwriter.lang.markup;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.*;

import static org.junit.Assert.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;

public class BranchDataTest {
    private static class DataTest<T extends DataTest<T>> extends
            SpanBranchAssert<T> {
        private InfoDataType dataType;

        public DataTest(Class<T> clazz){
            super(clazz);
        }

        T setType(InfoDataType type){
            dataType = type;
            return cast();
        }

        @Override
        public void setup(){
            setStyles(dataType);
        }

        @Override
        public void test(SpanBranch span){
            InfoDataSpan test = (InfoDataSpan) span;

            assertEquals(getError("type", test), dataType, test.getDataType());
        }
    }

    public static class FormatDataTest extends DataTest<FormatDataTest>{
        private FormatSpanMain spanData;

        public FormatDataTest(){
            super(FormatDataTest.class);
            setType(InfoDataType.FORMATTED);
        }

        public FormatDataTest setData(DocumentAssert doc, int ... idx){
            SpanBranch span = doc.getChild(idx);
            if (span instanceof FormatSpanMain){
                spanData = (FormatSpanMain) span;
            }
            return this;
        }

        @Override
        public void test(SpanBranch span){
            InfoDataSpanFormatted test = assertClass(span,
                InfoDataSpanFormatted.class);
            assertSame(getError("data", test), spanData, test.getData());
            super.test(span);
        }
    }
    public static class ContentDataTest extends DataTest<ContentDataTest>{
        private Span spanData;

        public ContentDataTest(){
            super(ContentDataTest.class);
            setType(InfoDataType.TEXT);
        }

        public ContentDataTest setData(DocumentAssert doc, int ... idx){
            Span span = doc.getChild(idx);
            spanData = span;
            return this;
        }

        @Override
        public void test(SpanBranch span){
            InfoDataSpanText test = assertClass(span, InfoDataSpanText.class);
            assertSame(getError("data", test), spanData, test.getData());
            super.test(span);
        }
    }
}
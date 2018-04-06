package com.creativeartie.writerstudio.lang.markup;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.*;

import static org.junit.Assert.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

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
        private FormattedSpan spanData;

        public FormatDataTest(){
            super(FormatDataTest.class);
            setType(InfoDataType.FORMATTED);
        }

        public FormatDataTest setData(DocumentAssert doc, int ... idx){
            spanData = doc.getChild(FormattedSpan.class, idx);
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
        private ContentSpan spanData;

        public ContentDataTest(){
            super(ContentDataTest.class);
            setType(InfoDataType.TEXT);
        }

        public ContentDataTest setData(DocumentAssert doc, int ... idx){
            spanData = doc.getChild(ContentSpan.class, idx);
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
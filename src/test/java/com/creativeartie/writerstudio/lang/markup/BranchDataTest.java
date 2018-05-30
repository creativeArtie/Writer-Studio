package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.function.*;

import java.util.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

public class BranchDataTest {
    private static class DataTest<T extends DataTest<T>>
            extends SpanBranchAssert<T> {
        /** For {@link InfoDataType#getDataType()}  (overrided)*/
        private InfoDataType dataType;

        public DataTest(Class<T> clazz, DocumentAssert doc, InfoDataType type){
            super(clazz, doc);
            dataType = type;
        }

        @Override
        public void setup(){
            setStyles(dataType);
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            InfoDataSpan test = (InfoDataSpan) span;

            tests.add(() -> assertEquals(dataType, test.getDataType(),
                "getDataType()"));
        }
    }

    public static class FormatDataTest extends DataTest<FormatDataTest>{
        private int[] spanData;

        public FormatDataTest(DocumentAssert doc){
            super(FormatDataTest.class, doc, InfoDataType.FORMATTED);
        }

        /** For {@link InfoDataSpanFormatted#getData()}  (no default). */
        public FormatDataTest setData(int ... idx){
            spanData = idx;
            return this;
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            InfoDataSpanFormatted test = assertClass(InfoDataSpanFormatted.class);

            tests.add(() -> assertSpan(FormattedSpan.class, spanData,
                () -> test.getData(), "getData()"));
            super.test(span, tests);
        }
    }

    public static class ContentDataTest extends DataTest<ContentDataTest>{
        private int[] spanData;

        public ContentDataTest(DocumentAssert doc){
            super(ContentDataTest.class, doc, InfoDataType.TEXT);
            spanData = null;
        }

        /** For {@link InfoDataSpanText#getData()}  (no default). */
        public ContentDataTest setData(int ... idx){
            spanData = idx;
            return this;
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            InfoDataSpanText test = assertClass(InfoDataSpanText.class);

            tests.add(() -> assertSpan(FormattedSpan.class, spanData,
                () -> test.getData(), "getData()"));
            super.test(span, tests);
        }
    }

    public static class RefDataTest extends DataTest<RefDataTest>{
        private int[] spanData;

        public RefDataTest(DocumentAssert doc){
            super(RefDataTest.class, doc, InfoDataType.TEXT);
            spanData = null;
        }

        /** For {@link InfoDataSpanText#getData()}  (no default). */
        public RefDataTest setData(int ... idx){
            spanData = idx;
            return this;
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            InfoDataSpanRef test = assertClass(InfoDataSpanRef.class);

            tests.add(() -> assertSpan(DirectorySpan.class, spanData,
                () -> test.getData(), "getData()"));
            super.test(span, tests);
        }
    }
}

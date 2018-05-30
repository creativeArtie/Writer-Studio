package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.function.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static org.junit.jupiter.api.Assertions.*;

public class BranchInfoDataAsserts {
    private static class DataAssert<T extends DataAssert<T>>
            extends SpanBranchAssert<T> {
        /** For {@link InfoDataType#getDataType()}  (overrided)*/
        private InfoDataType dataType;

        public DataAssert(Class<T> clazz, DocumentAssert doc, InfoDataType type){
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

    public static class FormatDataAssert extends DataAssert<FormatDataAssert>{
        private int[] spanData;

        public FormatDataAssert(DocumentAssert doc){
            super(FormatDataAssert.class, doc, InfoDataType.FORMATTED);
        }

        /** For {@link InfoDataSpanFormatted#getData()}  (no default). */
        public FormatDataAssert setData(int ... idx){
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

    public static class RefDataAssert extends DataAssert<RefDataAssert>{
        private int[] spanData;

        public RefDataAssert(DocumentAssert doc){
            super(RefDataAssert.class, doc, InfoDataType.TEXT);
            spanData = null;
        }

        /** For {@link InfoDataSpanText#getData()}  (no default). */
        public RefDataAssert setData(int ... idx){
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

    public static class ContentTextAssert extends DataAssert<ContentTextAssert>{
        private int[] spanData;

        public ContentTextAssert(DocumentAssert doc){
            super(ContentTextAssert.class, doc, InfoDataType.TEXT);
            spanData = null;
        }

        /** For {@link InfoDataSpanText#getData()}  (no default). */
        public ContentTextAssert setData(int ... idx){
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

}

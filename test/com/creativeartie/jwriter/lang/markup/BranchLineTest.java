package com.creativeartie.jwriter.lang.markup;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.*;

import static org.junit.Assert.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;

public class BranchLineTest {
    private static class LineTest<T extends LineTest<T>> extends
            SpanBranchAssert<T> {

        private int publishCount;
        private int noteCount;
        private LinedType linedType;

        public LineTest(Class<T> clazz){
            super(clazz);
        }

        public T setPublishCount(int count){
            publishCount = count;
            return cast();
        }

        public T setNoteCount(int count){
            noteCount = count;
            return cast();
        }

        public T setLinedType(LinedType type){
            linedType = type;
            return cast();
        }

        @Override
        public void test(SpanBranch span){
            LinedSpan test = (LinedSpan) span;
            assertEquals(getError("type", span), linedType, test.getLinedType());
            assertEquals(getError("publish", span), publishCount, test.getPublishCount());
            assertEquals(getError("note", span), noteCount, test.getNoteCount());
        }
    }

    private static class SimpleStyleTest<T extends SimpleStyleTest<T>>
            extends LineTest<T>{
        private LinedType linedType;

        public SimpleStyleTest(Class<T> clazz, LinedType type){
            super(clazz);
            linedType = type;
        }

        @Override
        public void setup(){
            setLinedType(linedType);
            setStyles(LinedType.BREAK);
        }
    }

    public static class CiteLineTest extends LineTest<CiteLineTest>{

        private InfoFieldType infoType;
        private Optional<InfoDataSpan<?>> dataSpan;

        public CiteLineTest(){
            super(CiteLineTest.class);
            setLinedType(LinedType.SOURCE);
            dataSpan = Optional.empty();
        }

        public CiteLineTest setInfoType(InfoFieldType type){
            infoType = type;
            return this;
        }

        public CiteLineTest setDataSpan(DocumentAssert doc, int ... idx){
            SpanBranch span = doc.getChild(idx);
            if (span instanceof InfoDataSpan){
                dataSpan = Optional.of((InfoDataSpan<?>) span);
            }
            return this;
        }

        @Override
        public void setup(){
            setLinedType(LinedType.SOURCE);
            setStyles(LinedType.SOURCE);
            if ( ! dataSpan.isPresent()){
                addStyles(AuxiliaryStyle.DATA_ERROR);
            }
        }

        @Override
        public void test(SpanBranch span){
            LinedSpanCite test = assertClass(span, LinedSpanCite.class);
            assertEquals(getError("field", test), infoType, test.getFieldType());
            assertSpan("data", span, dataSpan, test.getData());
            super.test(span);
        }
    }

    public static class BreakLineTest extends SimpleStyleTest<BreakLineTest>{

        public BreakLineTest(){
            super(BreakLineTest.class, LinedType.BREAK);
        }

        @Override
        public void test(SpanBranch span){
            super.test(assertClass(span, LinedSpanBreak.class));
        }
    }
}
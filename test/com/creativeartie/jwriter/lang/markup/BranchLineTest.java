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
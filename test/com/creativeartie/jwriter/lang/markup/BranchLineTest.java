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
            } else {
                throw new IllegalArgumentException(span +
                    " is not of type InfoDataSpan. Gotten: " + span.getClass());

            }
            return this;
        }

        @Override
        public void setup(){
            setLinedType(LinedType.SOURCE);
            setStyles(LinedType.SOURCE, infoType);
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

    private static class SimpleStyleTest<T extends SimpleStyleTest<T>>
            extends LineTest<T>{
        private LinedType linedType;

        public SimpleStyleTest(Class<T> clazz, LinedType type){
            super(clazz);
            linedType = type;
        }

        public T setLinedType(LinedType type){
            linedType = type;
            return super.setLinedType(type);
        }

        @Override
        public void setup(){
            setLinedType(linedType);
            setStyles(linedType);
        }
    }


    public abstract static class LevelLineTest<T extends SimpleStyleTest<T>>
            extends SimpleStyleTest<T>{
        private int lineLevel;
        private Optional<FormatSpanMain> lineText;

        public LevelLineTest(Class<T> clazz){
            super(clazz, null);
            lineText = Optional.empty();
        }

        public T setLevel(int level){
            lineLevel = level;
            return cast();
        }

        public T setFormattedSpan(DocumentAssert doc, int ... idx){
            SpanBranch span = doc.getChild(idx);
            if (span instanceof FormatSpanMain){
                lineText = Optional.of((FormatSpanMain) span);
            } else {
                throw new IllegalArgumentException(span +
                    " is not of type InfoDataSpan. Gotten: " + span.getClass());

            }
            return cast();
        }

        protected abstract LinedSpanLevel testSubclass(SpanBranch span);

        @Override
        public void test(SpanBranch span){
            LinedSpanLevel test = testSubclass(span);
            assertEquals(getError("level", test), lineLevel, test.getLevel());
            assertSpan("text", span, lineText, test.getFormattedSpan());
            super.test(span);
        }
    }

    public static class BasicLevelLineTest extends
        LevelLineTest<BasicLevelLineTest>{

        public BasicLevelLineTest(){
            super(BasicLevelLineTest.class);
        }

        protected LinedSpanLevel testSubclass(SpanBranch span){
            return assertClass(span, LinedSpanLevel.class);
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

    public static class CommonLineTest<T extends CommonLineTest<T>>
            extends SimpleStyleTest<T>{

        private Optional<FormatSpanMain> lineText;

        public CommonLineTest(Class<T> clazz, LinedType type){
            super(clazz, type);
        }

        public T setFormattedSpan(DocumentAssert doc, int ... idx){
            SpanBranch span = doc.getChild(idx);
            if (span instanceof FormatSpanMain){
                lineText = Optional.of((FormatSpanMain) span);
            } else {
                throw new IllegalArgumentException(span +
                    " is not of type FormatSpanMain. Gotten: " + span.getClass());

            }
            return cast();
        }

        protected Optional<FormatSpanMain> getFormatSpan(){
            return lineText;
        }
    }

    public static class ParagraphLineTest extends CommonLineTest<ParagraphLineTest>{

        private Optional<FormatSpanMain> lineText;

        public ParagraphLineTest(){
            super(ParagraphLineTest.class, LinedType.PARAGRAPH);
            lineText = Optional.empty();
        }

        @Override
        public void test(SpanBranch span){
            LinedSpanParagraph test = assertClass(span, LinedSpanParagraph.class);
            assertSpan("data", span, getFormatSpan(), test.getFormattedSpan());
            super.test(span);
        }
    }
}
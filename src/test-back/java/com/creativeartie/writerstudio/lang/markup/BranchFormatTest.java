package com.creativeartie.writerstudio.lang.markup;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.*;

import static org.junit.Assert.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
public class BranchFormatTest {

    private static void testFormats(SpanBranch span, FormatTypeStyle[] formats){
        FormatSpan test = (FormatSpan) span;

        assertFormat(test, test.isBold(),      FormatTypeStyle.BOLD, formats);
        assertFormat(test, test.isItalics(),   FormatTypeStyle.ITALICS, formats);
        assertFormat(test, test.isUnderline(), FormatTypeStyle.UNDERLINE, formats);
        assertFormat(test, test.isCoded(),     FormatTypeStyle.CODED, formats);
    }

    private static void assertFormat(FormatSpan span, boolean format,
            FormatTypeStyle type,  FormatTypeStyle[] formats)
    {
        boolean isTrue = false;
        for (FormatTypeStyle expected: formats){
            if (type == expected){
                isTrue = true;
            }
        }
        assertEquals(getError(type.name().toLowerCase() + " format", span),
            isTrue, format);
    }

    public static class FormatNoteTest extends SpanBranchAssert<FormatNoteTest>{
        private DirectoryType directory;
        private FormatTypeStyle[] formats;
        private Optional<SpanBranch> target;

        public FormatNoteTest(){
            super(FormatNoteTest.class);
            formats = new FormatTypeStyle[0];
            target = Optional.empty();
        }

        public FormatNoteTest setDirectoryType(DirectoryType type){
            directory = type;
            return this;
        }

        public FormatNoteTest setFormats(FormatTypeStyle ... types){
            formats = types;
            return this;
        }

        public FormatNoteTest setTarget(DocumentAssert doc, int ... idx){
            target = Optional.of(doc.getChild(SpanBranch.class, idx));
            return this;
        }

        public FormatTypeStyle[] getFormats(){
            return formats;
        }

        @Override
        public void setup(){
            setStyles(directory, getCatalogueStatus());
            addStyles(getFormats());
            setCatalogued();
        }

        @Override
        public void test(SpanBranch span){
            FormatSpanPointId test = assertClass(span, FormatSpanPointId
                .class);

            assertEquals(getError("type", test), directory, test.getIdType());
            testFormats(test, formats);
        }
    }

    public static class FormatKeyTest extends SpanBranchAssert<FormatKeyTest>{

        private FormatTypeStyle[] formats;
        private String field;

        public FormatKeyTest(){
            super(FormatKeyTest.class);
            formats = new FormatTypeStyle[0];
        }

        public FormatKeyTest setField(String str){
            field = str;
            return this;
        }

        public FormatKeyTest setFormats(FormatTypeStyle ... types){
            formats = types;
            return this;
        }

        public FormatTypeStyle[] getFormats(){
            return formats;
        }


        @Override
        public void setup(){
            setStyles(AuxiliaryType.REF_KEY);
            addStyles(formats);
        }

        @Override
        public void test(SpanBranch span){
            FormatSpanPointKey test = assertClass(span,
                FormatSpanPointKey.class);

            assertEquals(getError("field", test), field, test.getField());
            testFormats(test, formats);
        }
    }

    public static class FormatLinkTest extends SpanBranchAssert<FormatLinkTest>{

        private FormatTypeStyle[] formats;
        private Optional<SpanBranch> path;
        private String text;

        public FormatLinkTest(){
            super(FormatLinkTest.class);
            formats = new FormatTypeStyle[0];
            path = Optional.empty();
        }

        public FormatLinkTest setPath(DocumentAssert doc, int ... idx){
            path = Optional.of(doc.getChild(SpanBranch.class, idx));
            return cast();
        }

        public FormatLinkTest setText(String str){
            text = str;
            return cast();
        }

        public FormatLinkTest setFormats(FormatTypeStyle ... types){
            formats = types;
            return this;
        }

        public FormatTypeStyle[] getFormats(){
            return formats;
        }


        @Override
        public void setup(){
            if (isCatalogued()){
                setStyles(AuxiliaryType.REF_LINK, getCatalogueStatus());
            } else {
                setStyles(AuxiliaryType.DIRECT_LINK);
            }
            addStyles(formats);
        }

        @Override
        public void test(SpanBranch span){
            if (isCatalogued()){
                assertClass(span, FormatSpanLinkRef.class);
            } else {
                assertClass(span, FormatSpanLinkDirect.class);
            }

            FormatSpanLink test = (FormatSpanLink) span;
            assertEquals(getError("link text", test), text, test.getText());
            testFormats(test, formats);
        }
    }

    public static class FormatContentTest extends BranchTest.ContentBasicTest<FormatContentTest> {

        private FormatTypeStyle[] formats;

        public FormatContentTest(){
            super(FormatContentTest.class);
            formats = new FormatTypeStyle[0];
        }

        public FormatContentTest setFormats(FormatTypeStyle... types){
            formats = types;
            return this;
        }

        public FormatTypeStyle[] getFormats(){
            return formats;
        }

        @Override
        public void setup(){
            addStyles(formats);
        }

        @Override
        public void test(SpanBranch span){
            FormatSpanContent test = assertClass(span, FormatSpanContent.class);
            testFormats(test, formats);

        }
    }

    public static class FormattedSpanTest extends SpanBranchAssert<FormattedSpanTest>
    {
        private int publishTotal;
        private int noteTotal;
        public FormattedSpanTest(){
            super(FormattedSpanTest.class);
        }

        public FormattedSpanTest setPublishTotal(int count){
            publishTotal = count;
            return this;
        }

        public FormattedSpanTest setNoteTotal(int count){
            noteTotal = count;
            return this;
        }

        @Override
        protected void test(SpanBranch span) {
            FormattedSpan test = assertClass(span, FormattedSpan.class);

            assertEquals(getError("publish", test), publishTotal,
                test.getPublishTotal());
            assertEquals(getError("note", test), noteTotal,
                test.getNoteTotal());
        }

    }
}

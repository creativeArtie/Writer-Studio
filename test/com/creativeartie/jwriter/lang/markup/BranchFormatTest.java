package com.creativeartie.jwriter.lang.markup;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.*;

import static org.junit.Assert.*;

import com.creativeartie.jwriter.lang.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;
public class BranchFormatTest {

    private static void testFormats(SpanBranch span, FormatType[] formats){
        FormatSpan test = (FormatSpan) span;

        assertFormat(test, test.isBold(),      FormatType.BOLD, formats);
        assertFormat(test, test.isItalics(),   FormatType.ITALICS, formats);
        assertFormat(test, test.isUnderline(), FormatType.UNDERLINE, formats);
        assertFormat(test, test.isCoded(),     FormatType.CODED, formats);
    }

    private static void assertFormat(FormatSpan span, boolean format,
            FormatType type,  FormatType[] formats)
    {
        boolean isTrue = false;
        for (FormatType expected: formats){
            if (type == expected){
                isTrue = true;
            }
        }
        assertEquals(getError(type.name().toLowerCase() + " format", span),
            isTrue, format);
    }

    public static class FormatNoteTest extends SpanBranchAssert<FormatNoteTest>{
        private DirectoryType directory;
        private FormatType[] formats;

        public FormatNoteTest(){
            super(FormatNoteTest.class);
            formats = new FormatType[0];
        }

        public FormatNoteTest setDirectoryType(DirectoryType type){
            directory = type;
            return this;
        }
        
        public FormatNoteTest setFormats(FormatType ... types){
            formats = types;
            return this;
        }
        
        public FormatType[] getFormats(){
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
            FormatSpanDirectory test = assertClass(span, FormatSpanDirectory
                .class);

            assertEquals(getError("type", test), directory, test.getIdType());
            testFormats(test, formats);
        }
    }

    public static class FormatLinkTest extends SpanBranchAssert<FormatLinkTest> {

        private FormatType[] formats;
        private String path;
        private String text;

        public FormatLinkTest(){
            super(FormatLinkTest.class);
            formats = new FormatType[0];
        }

        public FormatLinkTest setPath(String str){
            path = str;
            return cast();
        }

        public FormatLinkTest setText(String str){
            text = str;
            return cast();
        }
        
        public FormatLinkTest setFormats(FormatType ... types){
            formats = types;
            return this;
        }
        
        public FormatType[] getFormats(){
            return formats;
        }


        @Override
        public void setup(){
            if (isCatalogued()){
                setStyles(AuxiliaryStyle.REF_LINK, getCatalogueStatus());
            } else {
                setStyles(AuxiliaryStyle.DIRECT_LINK);
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
            assertEquals(getError("link path", test), path, test.getPath());
            assertEquals(getError("link text", test), text, test.getText());
            testFormats(test, formats);
        }
    }
    
    public static class FormatContentTest extends BranchTest.ContentBasicTest<FormatContentTest> {
        
        private FormatType[] formats;

        public FormatContentTest(){
            super(FormatContentTest.class);
            formats = new FormatType[0];
        }
        
        public FormatContentTest setFormats(FormatType... types){
            formats = types;
            return this;
        }
        
        public FormatType[] getFormats(){
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
    
    public static class FormatMainTest extends SpanBranchAssert<FormatMainTest>
    {
        private int publishCount;
        private int noteCount;
        public FormatMainTest(){
            super(FormatMainTest.class);
        }
        
        public FormatMainTest setPublishCount(int count){
            publishCount = count;
            return this;
        }
        
        public FormatMainTest setNoteCount(int count){
            noteCount = count;
            return this;
        }

        @Override
        protected void test(SpanBranch span) {
            FormatSpanMain test = assertClass(span, FormatSpanMain.class);

            assertEquals(getError("publish", test), publishCount, 
                test.getPublishCount());
            assertEquals(getError("note", test), noteCount, 
                test.getNoteCount());
        }
        
    }
}

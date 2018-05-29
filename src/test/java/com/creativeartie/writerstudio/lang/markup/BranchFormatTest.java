package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.function.*;

import java.util.*;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

public class BranchFormatTest {

    private static ArrayList<Executable>
            testFormats(SpanBranch span, FormatTypeStyle[] formats){
        FormatSpan test = (FormatSpan) span;

        ArrayList<Executable> tests = new ArrayList<>();
        test.add(() -> Arrays.asList(formats).contains(FormatTypeStyle.BOLD),
            test.isBold(), "isBold()");
        test.add(() -> Arrays.asList(formats).contains(FormatTypeStyle.ITALICS),
            test.isItalics(), "isItalics()");
        test.add(() -> Arrays.asList(formats).contains(FormatTypeStyle.UNDERLINE),
            test.isUnderline(), "isUnderline()");
        test.add(() -> Arrays.asList(formats).contains(FormatTypeStyle.CODED),
            test.isCoded(), "isCoded()");
        return tests;
    }

    public static class FormatNoteTest extends SpanBranchAssert<FormatNoteTest>{
        private DirectoryType idType;
        private FormatTypeStyle[] spanFormats;
        private Optional<SpanBranch> spanTarget;

        public FormatNoteTest(){
            super(FormatNoteTest.class);
            spanFormats = new FormatTypeStyle[0];
            spanTarget = Optional.empty();
            idType = DirectoryType.FOOTNOTE;
        }

        /** For {@link FormatSpanPointId#getIdType()}
         * (default: {@link DirectoryType#FOOTNOTE}).
         */
        public FormatNoteTest setDirectoryType(DirectoryType type){
            idType = type;
            return this;
        }

        /** For {@link FormatSpan formats}  (default: none). */
        public FormatNoteTest setFormats(FormatTypeStyle ... types){
            spanFormats = types;
            return this;
        }

        public FormatTypeStyle[] getFormats(){
            return spanFormats;
        }

        public FormatNoteTest setTarget(DocumentAssert doc, int ... idx){
            spanTarget = Optional.of(doc.getChild(SpanBranch.class, idx));
            return this;
        }

        @Override
        public void setup(){
            setStyles(idType, getCatalogueStatus());
            addStyles(getFormats());
            setCatalogued();
        }

        @Override
        public void test(SpanBranch span){
            FormatSpanPointId test = assertClass(FormatSpanPointId.class);

            tests.add(() -> assertEquals(idType, test.getIdType(), "getIdType()"));
            tests.addAll(testFormats(test, spanFormats));
        }
    }

    public static class FormatKeyTest extends SpanBranchAssert<FormatKeyTest>{

        private FormatTypeStyle[] spanFormats;
        private String keyField;

        public FormatKeyTest(){
            super(FormatKeyTest.class);
            spanFormats = new FormatTypeStyle[0];
            keyField = "";
        }

        /** For {@link FormatSpanPointKey#getField()} (default: {@code ""}). */
        public FormatKeyTest setField(String str){
            keyField = str;
            return this;
        }

        /** For {@link FormatSpan spanFormats} (default: none). */
        public FormatKeyTest setFormats(FormatTypeStyle ... types){
            spanFormats = types;
            return this;
        }

        public FormatTypeStyle[] getFormats(){
            return spanFormats;
        }


        @Override
        public void setup(){
            setStyles(AuxiliaryType.REF_KEY);
            addStyles(spanFormats);
        }

        @Override
        public void test(SpanBranch span){
            FormatSpanPointKey test = assertClass(FormatSpanPointKey.class);

            assertEquals(keyField, test.getField(), "getField()");
            testFormats(test, spanFormats);
        }
    }

    public static class FormatLinkTest extends SpanBranchAssert<FormatLinkTest>{

        private FormatTypeStyle[] spanFormats;
        private Optional<SpanBranch> linkPath;
        private String linkText;

        public FormatLinkTest(){
            super(FormatLinkTest.class);
            spanFormats = new FormatTypeStyle[0];
            linkPath = Optional.empty();
        }

        /** For {@link FormatSpanLink#getPath()} (default: {@code ""}). */
        public FormatLinkTest setPath(DocumentAssert doc, int ... idx){
            linkPath = Optional.of(doc.getChild(SpanBranch.class, idx));
            return cast();
        }

        /** For {@link FormatSpanLink#getText()} (default: {@code ""}). */
        public FormatLinkTest setText(String str){
            linkText = str;
            return cast();
        }

        /** For {@link FormatSpan spanFormats} (default: none). */
        public FormatLinkTest setFormats(FormatTypeStyle ... types){
            spanFormats = types;
            return this;
        }

        public FormatTypeStyle[] getFormats(){
            return spanFormats;
        }


        @Override
        public void setup(){
            if (isCatalogued()){
                addStyles(AuxiliaryType.REF_LINK, getCatalogueStatus());
            } else {
                addStyles(AuxiliaryType.DIRECT_LINK);
            }
            addStyles(spanFormats);
        }

        @Override
        public void test(SpanBranch span){
            if (isCatalogued()){
                assertClass(span, FormatSpanLinkRef.class);
            } else {
                assertClass(span, FormatSpanLinkDirect.class);
            }

            FormatSpanLink test = (FormatSpanLink) span;
            assertEquals(linkText, test.getText(), "getText()");
            testFormats(test, spanFormats);
        }
    }

    public static class FormatContentTest extends BranchTest.ContentBasicTest<FormatContentTest> {

        private FormatTypeStyle[] spanFormats;

        public FormatContentTest(){
            super(FormatContentTest.class);
            spanFormats = new FormatTypeStyle[0];
        }

        public FormatContentTest setFormats(FormatTypeStyle... types){
            spanFormats = types;
            return this;
        }

        public FormatTypeStyle[] getFormats(){
            return spanFormats;
        }

        @Override
        public void setup(){
            addStyles(spanFormats);
        }

        @Override
        public void test(SpanBranch span){
            FormatSpanContent test = assertClass(span, FormatSpanContent.class);
            testFormats(test, spanFormats);

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

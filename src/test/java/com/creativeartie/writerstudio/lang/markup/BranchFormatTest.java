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
        test.add(() -> assertEquals(Arrays.asList(formats)
            .contains(FormatTypeStyle.BOLD), test.isBold(), "isBold()"));
        test.add(() -> assertEquals(Arrays.asList(formats)
            .contains(FormatTypeStyle.ITALICS), test.isItalics(), "isItalics()"));
        test.add(() -> assertEquals(Arrays.asList(formats)
            .contains(FormatTypeStyle.UNDERLINE), test.isUnderline(), "isUnderline()");
        test.add(() -> assertEquals(Arrays.asList(formats)
            .contains(FormatTypeStyle.CODED), test.isCoded(), "isCoded()");
        return tests;
    }

    public static class FormatNoteTest extends SpanBranchAssert<FormatNoteTest>{
        private DirectoryType idType;
        private FormatTypeStyle[] spanFormats;
        private int[] spanTarget;

        public FormatNoteTest(DocumentAssert doc){
            super(FormatNoteTest.class, doc);
            spanFormats = new FormatTypeStyle[0];
            spanTarget = null;
            idType = DirectoryType.FOOTNOTE;
        }

        /** For {@link FormatSpanPointId#getIdType()}
         * (default: {@link DirectoryType#FOOTNOTE}).
         */
        public FormatNoteTest setDirectoryType(DirectoryType type){
            assert type == DirectoryType.RESEARCH ||
                type == DirectoryType.FOOTNOTE ||
                type == DirectoryType.ENDNOTE : "Wrong type: " + type;
            idType = type;
            return this;
        }

        /** For {@link FormatSpan formats}  (default: no formats). */
        public FormatNoteTest setFormats(FormatTypeStyle ... types){
            spanFormats = types;
            return this;
        }

        /** For {@link FormatSpanPointId#getTarget()} (no default).*/
        public FormatNoteTest setTarget(int ... indexes){
            spanTarget = indexes;
            return this;
        }

        @Override
        public void setup(){
            setStyles(idType, getCatalogueStatus());
            addStyles(spanFormats);
            setCatalogued();
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            FormatSpanPointId test = assertClass(FormatSpanPointId.class);

            tests.add(() -> assertEquals(idType, test.getIdType(), "getIdType()"));
            Class<?> clazz;
            switch (idType){
                case DirectoryType.RESEARCH:
                    clazz = NoteCardSpan.class;
                    break;
                case DirectoryType.FOOTNOTE:
                case DirectoryType.ENDNOTE:
                    clazz = LinedSpanPointNote.class;
                    break;
                default:
                    clazz = SpanBranch.class;
            }
            tests.add(() -> assertSpan(clazz, spanTarget, () -> test.getTarget(),
                "getTarget()"));
            tests.addAll(testFormats(test, spanFormats));
        }
    }

    public static class FormatKeyTest extends SpanBranchAssert<FormatKeyTest>{

        private FormatTypeStyle[] spanFormats;
        private FormatTypeField keyField;

        public FormatKeyTest(){
            super(FormatKeyTest.class);
            spanFormats = new FormatTypeStyle[0];
            keyField = "";
        }

        /** For {@link FormatSpanPointKey#getField()} (default: {@code ""}). */
        public FormatKeyTest setField(FormatTypeField key){
            keyField = key;
            return this;
        }

        /** For {@link FormatSpan spanFormats} (default: none). */
        public FormatKeyTest setFormats(FormatTypeStyle ... types){
            spanFormats = types;
            return this;
        }

        @Override
        public void setup(){
            setStyles(AuxiliaryType.REF_KEY);
            addStyles(spanFormats);
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            FormatSpanPointKey test = assertClass(FormatSpanPointKey.class);

            tests.add(() -> assertEquals(keyField, test.getField(), "getField()"));
            tests.add(() -> testFormats(test, spanFormats));
        }
    }

    private static abstract class FormatLinkTest<T extends SpanBranchAssert>
            extends SpanBranchAssert<T>{

        private FormatTypeStyle[] spanFormats;
        private String linkText;

        public FormatLinkTest(Class<T> clazz){
            super(clazz);
            spanFormats = new FormatTypeStyle[0];
            likeText = "";
        }

        /** For {@link FormatSpanLink#getText()} (default: {@code ""}). */
        public T setText(String str){
            linkText = str;
            return cast();
        }

        /** For {@link FormatSpan spanFormats} (default: none). */
        public T setFormats(FormatTypeStyle ... types){
            spanFormats = types;
            return cast();
        }

        @Override
        public void setup(){
            moreSetup();
            if (isCatalogued()){
                addStyles(AuxiliaryType.REF_LINK, getCatalogueStatus());
            } else {
                addStyles(AuxiliaryType.DIRECT_LINK);
            }
            addStyles(spanFormats);
        }

        protected abstract void moreSetup();

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            FormatSpanLink test = (FormatSpanLink) span;
            assertEquals(linkText, test.getText(), "getText()");
            testFormats(test, spanFormats);
        }
    }

    public class FormatDirectLinkTest
            extends FormatLinkTest<FormatDirectLinkTest>{

        private String linkPath;

        public FormatDirectLinkTest(){
            super(FormatDirectLinkTest.class);
            linkPath = "";
        }

        /** For {@link FormatDirectSpanLink#getPath()} (default: {@code ""}). */
        public FormatDirectLinkTest setText(String path){
            linkPath = path;
            return this;
        }

        public void moreSetup(){}

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            FormatSpanLinkDirect test = assertClass(FormatSpanLinkDirect.class);

            tests.add(() -> assertEquals(linkPath, test.getPath(), "getPath()"));
            super(span, tests);
        }
    }

    public class FormatRefLinkTest extends FormatLinkTest<FormatRefLinkTest>{

        private int[] linkPath;

        public FormatRefLinkTest(){
            super(FormatRefLinkTest.class);
            linkPath = null;
        }

        /** For {@link FormatDirectSpanLink#getPath()} (default: {@code ""}). */
        public FormatRefLinkTest setText(String path){
            linkPath = path;
            return this;
        }

        public void moreSetup(){
            setCatalogued();
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            FormatSpanLinkRef test = assertClass(FormatSpanLinkRef.class);

            tests.add(() -> assertSpan(SpanBranch.class, spanTarget,
                () -> span.getTarget(), "getTarget()"));
            super(span, tests);
        }
    }

    public static class FormatContentTest extends BranchTest.ContentBasicTest<FormatContentTest> {

        private FormatTypeStyle[] spanFormats;

        public FormatContentTest(){
            super(FormatContentTest.class);
            spanFormats = new FormatTypeStyle[0];
        }

        /** For {@link FormatSpan spanFormats} (default: none). */
        public FormatContentTest setFormats(FormatTypeStyle... types){
            spanFormats = types;
            return this;
        }

        @Override
        public void setup(){
            addStyles(spanFormats);
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            FormatSpanContent test = assertClass(span, FormatSpanContent.class);
            tests.add(() -> testFormats(test, spanFormats));
            super.test(span, tests);
        }
    }

    public static class FormattedSpanTest extends SpanBranchAssert<FormattedSpanTest>
    {
        private int publishTotal;
        private int noteTotal;
        private String parsedText;

        public FormattedSpanTest(){
            super(FormattedSpanTest.class);
            parsedText = "";
            publishTotal = 1;
            noteTotal = 0;
        }

        /** For {@link FormattedSpan#getPublishTotal()} (default: {@code 1}). */
        public FormattedSpanTest setPublish(int count){
            publishTotal = count;
            return this;
        }

        /** For {@link FormattedSpan#getNoteTotal()} (default: {@code 0}). */
        public FormattedSpanTest setNote(int count){
            noteTotal = count;
            return this;
        }

        /** For {@link FormattedSpan#getParsedText()} (default: {@code ""}). */
        public FormattedSpanTest setParsed(String text){
            parsedText = text;
        }

        @Override
        protected void test(SpanBranch span, ArrayList<Executable> tests) {
            FormattedSpan test = assertClass(FormattedSpan.class);
            tests.add(() -> assertEquals(publishTotal, test.getPublishTotal(),
                "getPublishTotal()"));
            tests.add(() -> assertEquals(noteTotal, test.getNoteTotal(),
                "getNoteTotal()"));
            tests.add(() -> assertEquals(parsedText, test.getParsedText(),
                "getParsedText()"));
        }

    }
}

package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.function.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static org.junit.jupiter.api.Assertions.*;

public class BranchFormatAsserts {

    private static ArrayList<Executable>
            testFormats(SpanBranch span, FormatTypeStyle[] formats){
        FormatSpan test = (FormatSpan) span;

        ArrayList<Executable> tests = new ArrayList<>();
        tests.add(() -> assertEquals(Arrays.asList(formats)
            .contains(FormatTypeStyle.BOLD), test.isBold(), "isBold()"));
        tests.add(() -> assertEquals(Arrays.asList(formats)
            .contains(FormatTypeStyle.ITALICS), test.isItalics(), "isItalics()"));
        tests.add(() -> assertEquals(Arrays.asList(formats)
            .contains(FormatTypeStyle.UNDERLINE), test.isUnderline(),
            "isUnderline()"));
        tests.add(() -> assertEquals(Arrays.asList(formats)
            .contains(FormatTypeStyle.CODED), test.isCoded(), "isCoded()"));
        return tests;
    }
    public static class FormatContentAssert extends BranchBasicAsserts.ContentBasicAssert<FormatContentAssert> {

        private FormatTypeStyle[] spanFormats;

        public FormatContentAssert(DocumentAssert doc){
            super(FormatContentAssert.class, doc);
            spanFormats = new FormatTypeStyle[0];
        }

        /** For {@link FormatSpan spanFormats} (default: none). */
        public FormatContentAssert setFormats(FormatTypeStyle... types){
            spanFormats = types;
            return this;
        }

        @Override
        public void setup(){
            addStyles(spanFormats);
        }

        @Override
        public BasicText moreTest(SpanBranch span, ArrayList<Executable> tests){
            FormatSpanContent test = assertClass(FormatSpanContent.class);
            tests.add(() -> testFormats(test, spanFormats));
            return test;
        }
    }

    private static abstract class FormatLinkAssert<T extends SpanBranchAssert>
            extends SpanBranchAssert<T>{

        private FormatTypeStyle[] spanFormats;
        private String linkText;

        public FormatLinkAssert(Class<T> clazz, DocumentAssert doc){
            super(clazz, doc);
            spanFormats = new FormatTypeStyle[0];
            linkText = "";
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

        protected abstract FormatSpanLink moreTest(SpanBranch span,
            ArrayList<Executable> tests);

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            FormatSpanLink test = (FormatSpanLink) span;
            assertEquals(linkText, test.getText(), "getText()");
            testFormats(test, spanFormats);
        }
    }

    public static class FormatDirectLinkAssert
        extends FormatLinkAssert<FormatDirectLinkAssert>
    {

        private String linkPath;

        public FormatDirectLinkAssert(DocumentAssert doc){
            super(FormatDirectLinkAssert.class, doc);
            linkPath = "";
        }

        /** For {@link FormatDirectSpanLink#getPath()} (default: {@code ""}). */
        public FormatDirectLinkAssert setPath(String path){
            linkPath = path;
            return this;
        }

        @Override
        public void moreSetup(){}

        @Override
        public FormatSpanLink moreTest(SpanBranch span,
                ArrayList<Executable> tests){
            FormatSpanLinkDirect test = assertClass(FormatSpanLinkDirect.class);

            tests.add(() -> assertEquals(linkPath, test.getPath(), "getPath()"));
            return test;
        }
    }

    public static class FormatRefLinkAssert
        extends FormatLinkAssert<FormatRefLinkAssert>
    {

        private int[] linkPath;

        public FormatRefLinkAssert(DocumentAssert doc){
            super(FormatRefLinkAssert.class, doc);
            linkPath = null;
        }

        /** For {@link FormatDirectSpanLink#getPath()} (no default). */
        public FormatRefLinkAssert setText(int ... indexes){
            linkPath = indexes;
            return this;
        }

        @Override
        public void moreSetup(){
            setCatalogued();
        }

        @Override
        public FormatSpanLink moreTest(SpanBranch span,
                ArrayList<Executable> tests){
            FormatSpanLinkRef test = assertClass(FormatSpanLinkRef.class);

            tests.add(() -> assertChild(SpanBranch.class, linkPath,
                () -> test.getPathSpan(), "getPathSpan()"));
            return test;
        }
    }

    public static class FormatNoteAssert extends SpanBranchAssert<FormatNoteAssert>{
        private DirectoryType idType;
        private FormatTypeStyle[] spanFormats;
        private int[] spanTarget;

        public FormatNoteAssert(DocumentAssert doc){
            super(FormatNoteAssert.class, doc);
            spanFormats = new FormatTypeStyle[0];
            spanTarget = null;
            idType = DirectoryType.FOOTNOTE;
        }

        /** For {@link FormatSpanPointId#getIdType()}
         * (default: {@link DirectoryType#FOOTNOTE}).
         */
        public FormatNoteAssert setDirectoryType(DirectoryType type){
            assert type == DirectoryType.RESEARCH ||
                type == DirectoryType.FOOTNOTE ||
                type == DirectoryType.ENDNOTE : "Wrong type: " + type;
            idType = type;
            return this;
        }

        /** For {@link FormatSpan formats}  (default: no formats). */
        public FormatNoteAssert setFormats(FormatTypeStyle ... types){
            spanFormats = types;
            return this;
        }

        /** For {@link FormatSpanPointId#getTarget()} (no default).*/
        public FormatNoteAssert setTarget(int ... indexes){
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
            Class<? extends SpanBranch> clazz;
            switch (idType){
                case RESEARCH:
                    clazz = NoteCardSpan.class;
                    break;
                case FOOTNOTE:
                case ENDNOTE:
                    clazz = LinedSpanPointNote.class;
                    break;
                default:
                    clazz = SpanBranch.class;
            }
            tests.add(() -> assertChild(clazz, spanTarget, () -> test.getTarget(),
                "getTarget()"));
            tests.addAll(testFormats(test, spanFormats));
        }
    }

    public static class FormatKeyAssert extends SpanBranchAssert<FormatKeyAssert>{

        private FormatTypeStyle[] spanFormats;
        private FormatTypeField keyField;

        public FormatKeyAssert(DocumentAssert doc){
            super(FormatKeyAssert.class, doc);
            spanFormats = new FormatTypeStyle[0];
            keyField = FormatTypeField.ERROR;
        }

        /** For {@link FormatSpanPointKey#getField()} (default: {@code ERROR}). */
        public FormatKeyAssert setField(FormatTypeField key){
            keyField = key;
            return this;
        }

        /** For {@link FormatSpan spanFormats} (default: none). */
        public FormatKeyAssert setFormats(FormatTypeStyle ... types){
            spanFormats = types;
            return this;
        }

        @Override
        public void setup(){
            setStyles(AuxiliaryType.REF_KEY, keyField);
            addStyles(spanFormats);
        }

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            FormatSpanPointKey test = assertClass(FormatSpanPointKey.class);

            tests.add(() -> assertEquals(keyField, test.getField(), "getField()"));
            tests.add(() -> testFormats(test, spanFormats));
        }
    }
    public static class FormattedSpanAssert
        extends SpanBranchAssert<FormattedSpanAssert>
    {
        private int publishTotal;
        private int noteTotal;
        private String parsedText;

        public FormattedSpanAssert(DocumentAssert doc){
            super(FormattedSpanAssert.class, doc);
            parsedText = "";
            publishTotal = 1;
            noteTotal = 0;
        }

        /** For {@link FormattedSpan#getPublishTotal()} (default: {@code 1}). */
        public FormattedSpanAssert setPublish(int count){
            publishTotal = count;
            return this;
        }

        /** For {@link FormattedSpan#getNoteTotal()} (default: {@code 0}). */
        public FormattedSpanAssert setNote(int count){
            noteTotal = count;
            return this;
        }

        /** For {@link FormattedSpan#getParsedText()} (default: {@code ""}). */
        public FormattedSpanAssert setParsed(String text){
            parsedText = text;
            return this;
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

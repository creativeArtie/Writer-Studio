package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;

import java.util.*;
import java.time.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.BranchBasicAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchFormatAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchTextAssert.*;

import static org.junit.jupiter.api.Assertions.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

public class TextFileTest {

    public class TextFileAssert{
        private final Map<TextTypeInfo, String> infoStrings;
        private final Map<TextTypeMatter, List<Integer>> matterSpans;
        private final WritingData testDoc;
        private final DocumentAssert assertDoc;

        public TextFileAssert(int size, String raw){
            testDoc = new WritingData(raw);
            assertDoc = assertDoc(size, raw, testDoc);
            infoStrings = new TreeMap<>();
            matterSpans = new TreeMap<>();
        }

        public WritingData getDocument(){
            return testDoc;
        }

        public DocumentAssert getAsserter(){
            return assertDoc;
        }

        public void assertRest(){
            assertDoc.assertRest();
        }

        public TextFileAssert addInfo(TextTypeInfo type, String data){
            infoStrings.put(type, data);
            return this;
        }

        public TextFileAssert addMatter(TextTypeMatter type, int data){
            if (matterSpans.containsKey(type)){
                matterSpans.get(type).add(data);
            } else {
                ArrayList<Integer> create = new ArrayList<>();
                create.add(data);
                matterSpans.put(type, create);
            }
            return this;
        }

        public TextFileAssert test(int size, String raw){
            assertDoc.assertDoc(size, raw);
            ArrayList<Executable> tests = new ArrayList<>();
            for (TextTypeInfo info: TextTypeInfo.values()){
                String text = infoStrings.containsKey(info)? infoStrings.get(info):
                    "";
                tests.add(() -> assertEquals(text, testDoc.getInfo(info),
                    "getInfoText(" + info.toString() + ")"));
            }
            for (TextTypeMatter matter: TextTypeMatter.values()){
                ArrayList<SpanBranch> expect = new ArrayList<>();
                if (matterSpans.containsKey(matter)){
                    for (int span: matterSpans.get(matter)){
                        expect.add(testDoc.get(span));
                    }
                }
                tests.add(() -> assertArrayEquals(expect.toArray(),
                    testDoc.getMatter(matter).toArray(),
                    "getMatter(" + matter + ")"));
            }
            assertAll("Document", tests);
            return this;
        }
    }

    private static SetupParser PARSER = TextParser.PARSER;

    @Test
    public void infoBasic(){
        String raw = "meta-keywords|text|key-1, key-2, key-etc\n";
        TextFileAssert doc = new TextFileAssert(1, raw);
        commonInfo(doc);
    }

    @Test
    public void editInfoNew(){
        String raw = "";
        TextFileAssert doc = new TextFileAssert(0, raw);
        doc.getAsserter().call(WritingData.class,
            s -> s.setInfo(TextTypeInfo.KEYWORDS, "key-1, key-2, key-etc"));
        commonInfo(doc);
    }

    @Test
    public void editInfoOld(){
        String raw = "meta-keywords|text|abc";
        TextFileAssert doc = new TextFileAssert(1, raw);
        doc.getAsserter().call(() -> doc.getDocument(),
            s -> s.setInfo(TextTypeInfo.KEYWORDS, "key-1, key-2, key-etc"), 0);
        commonInfo(doc);
    }

    private void commonInfo(TextFileAssert doc){
        String text = "key-1, key-2, key-etc";
        String raw = "meta-keywords|text|" + text + "\n";

        doc.addInfo(TextTypeInfo.KEYWORDS, text);
        InfoLineAssert info = new InfoLineAssert(doc)
            .setRow(TextTypeInfo.KEYWORDS)
            .setType(TextDataType.TEXT)
            .setData(0, 5);
        ContentAssert data = new ContentAssert(doc.getAsserter())
            .setBegin(false).setBoth(text)
            .setEnd(false)  .setCount(3);

        doc.test( 1, raw);
        info.test(6, raw, 0);
        data.test(1, text, 0, 4);
        doc.assertRest();
    }

    @Test
    public void infoEscapes(){
        String raw = "meta-keywords|text|\\\\adf\n";
        TextFileAssert doc = new TextFileAssert(1, raw);
        commonInfoEscapes(doc);
    }

    @Test
    public void editInfoEscpesNew(){
        String raw = "";
        TextFileAssert doc = new TextFileAssert(0, raw);
        doc.getAsserter().call(WritingData.class,
            s -> s.setInfo(TextTypeInfo.KEYWORDS, "\\adf"));
        commonInfoEscapes(doc);
    }

    @Test
    public void editInfoEscapesOld(){
        String raw = "meta-keywords|text|adedafasfafas\\\\adfasdfsaf\n";
        TextFileAssert doc = new TextFileAssert(1, raw);

        doc.getAsserter().call(() -> doc.getDocument(),
            s -> s.setInfo(TextTypeInfo.KEYWORDS, "\\adf"), 0);

        commonInfoEscapes(doc);
    }

    private void commonInfoEscapes(TextFileAssert doc){
        String text = "\\\\adf";
        String raw = "meta-keywords|text|" + text + "\n";

        doc.addInfo(TextTypeInfo.KEYWORDS, "\\adf");
        InfoLineAssert info = new InfoLineAssert(doc)
            .setRow(TextTypeInfo.KEYWORDS)
            .setType(TextDataType.TEXT)
            .setData(0, 5);
        ContentAssert data = new ContentAssert(doc.getAsserter())
            .setBegin(false).setBoth("\\adf")
            .setEnd(false)  .setCount(1);

        doc.test( 1, raw);
        info.test(6, raw, 0);
        data.test(2, text, 0, 4);
        doc.assertRest();
    }

    @Test
    public void matterBasic(){
        String raw = "text-break|center|Hello *WORLD*!\n";
        TextFileAssert doc = new TextFileAssert(1, raw);
        commonMatterSingle(doc);
    }

    @Test
    public void editMatterNew(){
        String raw = "";
        TextFileAssert doc = new TextFileAssert(0, raw);
        doc.getAsserter().call(WritingData.class,
            s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Hello *WORLD*!"));
        commonMatterSingle(doc);
    }

    @Test
    public void editMatterNewEscape(){
        String raw = "";
        TextFileAssert doc = new TextFileAssert(0, raw);
        doc.getAsserter().call(WritingData.class,
            s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Hello *WORLD*!\\"));
        commonMatterSingle(doc);
    }

    @Test
    public void editMatterEdited(){
        String raw = "text-break|center|abc\n";
        TextFileAssert doc = new TextFileAssert(1, raw);
        doc.getAsserter().call(() -> doc.getDocument(),
            s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Hello *WORLD*!"), 0);
        commonMatterSingle(doc);
    }

    @Test
    public void editMatterEditedEscape(){
        String raw = "text-break|center|abc\n";
        TextFileAssert doc = new TextFileAssert(1, raw);
        doc.getAsserter().call(() -> doc.getDocument(),
            s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Hello *WORLD*!\\"), 0);
        commonMatterSingle(doc);
    }

    private void commonMatterSingle(TextFileAssert doc){
        String text = "Hello *WORLD*!";
        String raw = "text-break|center|" + text + "\n";

        doc.addMatter(TextTypeMatter.TEXT_BREAK, 0);
        MatterLineAssert matter = new MatterLineAssert(doc)
            .setRow(TextTypeMatter.TEXT_BREAK)
            .setType(TextDataType.CENTER)
            .setData(0, 5).setIndex(0);
        FormattedSpanAssert data = new FormattedSpanAssert(doc.getAsserter())
            .setPublish(2).setNote(0)
            .setParsed("Hello WORLD!");

        doc.test(   1, raw);
        matter.test(6, raw, 0);
        data.test(  5, text, 0, 4);
        doc.assertRest();
    }

    @Test
    public void matterDouble(){
        String raw = "text-break|center|Abc\\*\n" +
                     "text-break|center|Hello\n";
        TextFileAssert doc = new TextFileAssert(2, raw);
        commonMatterDouble(doc);
    }

    @Test
    public void matterDoubleNew(){
        TextFileAssert doc = new TextFileAssert(0, "");
        doc.getAsserter().call(WritingData.class,
            s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Abc\\*\nHello"));
        commonMatterDouble(doc);
    }

    @Test
    public void matterDouble1Line(){
        String raw = "text-break|center|Abc\\*\n";
        TextFileAssert doc = new TextFileAssert(1, raw);
        doc.getAsserter().call(() -> doc.getDocument(),
            s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Abc\\*\nHello"),
            () -> new SpanNode[]{
                doc.getDocument(),
                doc.getDocument().get(0)
            });
        commonMatterDouble(doc);
    }

    @Test
    public void matterDouble2Line(){
        String line1 = "text-break|center|Joy\n";
        String line2 = "text-break|center|Fun\n";
        String raw = line1 + line2;
        TextFileAssert doc = new TextFileAssert(2, raw);
        doc.getAsserter().call(() -> doc.getDocument(),
            s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Abc\\*\nHello"),
            () -> new SpanNode[]{
                doc.getDocument().get(0),
                doc.getDocument().get(1)
            });
        commonMatterDouble(doc);
    }

    @Test
    public void matterDouble3Line(){
        String line1 = "text-break|center|Joy\n";
        String line2 = "text-break|center|Fun\n";
        String line3 = "text-break|center|Cool\n";
        String raw = line1 + line2 + line3;
        TextFileAssert doc = new TextFileAssert(3, raw);
        doc.getAsserter().call(true, () -> doc.getDocument(),
            s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Abc\\*\nHello"),
            () -> new SpanNode[]{
                doc.getDocument(),
                doc.getDocument().get(0),
                doc.getDocument().get(1),
                doc.getDocument().get(2),
            });
        commonMatterDouble(doc);
    }

    private void commonMatterDouble(TextFileAssert doc){
        String text1 = "Abc\\*";
        String text2 = "Hello";
        String line1 = "text-break|center|" + text1 + "\n";
        String line2 = "text-break|center|" + text2 + "\n";
        String raw = line1 + line2;

        doc.addMatter(TextTypeMatter.TEXT_BREAK, 0);
        doc.addMatter(TextTypeMatter.TEXT_BREAK, 1);
        MatterLineAssert matter1 = new MatterLineAssert(doc)
            .setRow(TextTypeMatter.TEXT_BREAK)
            .setType(TextDataType.CENTER)
            .setData(0, 5).setIndex(0);
        FormattedSpanAssert data1 = new FormattedSpanAssert(doc.getAsserter())
            .setPublish(1).setNote(0)
            .setParsed("Abc*");
        MatterLineAssert matter2 = new MatterLineAssert(doc)
            .setRow(TextTypeMatter.TEXT_BREAK)
            .setType(TextDataType.CENTER)
            .setData(0, 5).setIndex(1);
        FormattedSpanAssert data2 = new FormattedSpanAssert(doc.getAsserter())
            .setPublish(1).setNote(0)
            .setParsed("Hello");

        doc.test(    2, raw);
        matter1.test(6, line1, 0);
        data1.test(  1, text1, 0, 4);
        matter2.test(6, line2, 1);
        data2.test(  1, text2, 1, 4);
        doc.assertRest();
    }
}

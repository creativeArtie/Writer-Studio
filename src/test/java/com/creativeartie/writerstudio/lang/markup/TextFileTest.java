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
        String key = "meta-keywords";
        String text = "key-1, key-2, key-etc";
        String raw = key + "|text|" + text + "\n";

        doc.addInfo(TextTypeInfo.KEYWORDS, text);
        InfoLineAssert info = new InfoLineAssert(doc)
            .setRow(TextTypeInfo.KEYWORDS)
            .setType(TextDataType.TEXT)
            .setData(0, 4);
        ContentAssert data = new ContentAssert(doc.getAsserter())
            .setBegin(false).setBoth(text)
            .setEnd(false)  .setCount(3);

        DocumentAssert main = doc.getAsserter();
        doc.test( 1,             raw);
        info.test(6,             raw,   0);
        main.assertField(0, 13,  key,   0, 0);
        main.assertKey( 13, 14, "|",    0, 1);
        main.assertData(14, 18, "text", 0, 2);
        main.assertKey( 18, 19, "|",    0, 3);
        data.test(1,             text,  0, 4);
        main.assertText(19, 40,  text,  0, 4, 0);
        main.assertKey( 40, 41, "\n",   0, 5);
        main.assertRest();

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
        String key = "meta-keywords";
        String text = "\\\\adf";
        String raw = key + "|text|" + text + "\n";

        doc.addInfo(TextTypeInfo.KEYWORDS, "\\adf");
        InfoLineAssert info = new InfoLineAssert(doc)
            .setRow(TextTypeInfo.KEYWORDS)
            .setType(TextDataType.TEXT)
            .setData(0, 4);
        ContentAssert data = new ContentAssert(doc.getAsserter())
            .setBegin(false).setBoth("\\adf")
            .setEnd(false)  .setCount(1);

        DocumentAssert main = doc.getAsserter();
        doc.test( 1,             raw);
        info.test(6,             raw,   0);
        main.assertField(0, 13,  key,   0, 0);
        main.assertKey( 13, 14, "|",    0, 1);
        main.assertData(14, 18, "text", 0, 2);
        main.assertKey( 18, 19, "|",    0, 3);
        data.test(2,             text,  0, 4);
        main.assertChild(2,     "\\\\", 0, 4, 0);
        main.assertKey( 19, 20, "\\",   0, 4, 0, 0);
        main.assertText(20, 21, "\\",   0, 4, 0, 1);
        main.assertText(21, 24, "adf",  0, 4, 1);
        main.assertKey( 24, 25, "\n",   0, 5);
        main.assertRest();
    }

    @Test
    public void unknownWithSpace(){
        String key = "    version  ";
        String text = "abc";
        String raw = key + "| text |" + text  +"\n";
        TextFileAssert doc = new TextFileAssert(1, raw);
        UnknownAssert info = new UnknownAssert(doc)
            .setData(0, 4).setType(TextDataType.TEXT);
        ContentAssert data = new ContentAssert(doc.getAsserter())
            .setBegin(false).setBoth("abc")
            .setEnd(false)  .setCount(1);

        DocumentAssert main = doc.getAsserter();
        doc.test( 1,             raw);
        info.test(6,             raw,    0);
        main.assertField(0, 13,  key,    0, 0);
        main.assertKey( 13, 14, "|",     0, 1);
        main.assertData(14, 19, " text", 0, 2);
        main.assertKey( 19, 21, " |",    0, 3);
        data.test(1,            text,    0, 4);
        main.assertText(21, 24, text,    0, 4, 0);
        main.assertKey( 24, 25, "\n",    0, 5);
        main.assertRest();
    }

    @Test
    public void unknownWithText(){
        String key = "new-version  ";
        String text = "abc";
        String raw = key + "|text  |" + text  +"\n";
        TextFileAssert doc = new TextFileAssert(1, raw);
        UnknownAssert info = new UnknownAssert(doc)
            .setData(0, 4).setType(TextDataType.TEXT);
        ContentAssert data = new ContentAssert(doc.getAsserter())
            .setBegin(false).setBoth("abc")
            .setEnd(false)  .setCount(1);

        DocumentAssert main = doc.getAsserter();
        doc.test( 1,             raw);
        info.test(6,             raw,   0);
        main.assertField(0, 13,  key,   0, 0);
        main.assertKey( 13, 14, "|",    0, 1);
        main.assertData(14, 18, "text", 0, 2);
        main.assertKey( 18, 21, "  |",  0, 3);
        data.test(1,            text,   0, 4);
        main.assertText(21, 24, text,   0, 4, 0);
        main.assertKey( 24, 25, "\n",   0, 5);
        main.assertRest();
    }

    @Test
    public void unknownAll(){
        String text = "abc";
        String raw = "wtf| why |" + text  +"\n";
        TextFileAssert doc = new TextFileAssert(1, raw);
        UnknownAssert info = new UnknownAssert(doc)
            .setData(0, 4).setType(TextDataType.TEXT);
        ContentAssert data = new ContentAssert(doc.getAsserter())
            .setBegin(false).setBoth("abc")
            .setEnd(false)  .setCount(1);

        DocumentAssert main = doc.getAsserter();
        doc.test( 1, raw);
        info.test(6,            raw,     0);
        main.assertField(0, 3,  "wtf",   0, 0);
        main.assertKey(  3, 4,  "|",     0, 1);
        main.assertData( 4, 9,  " why ", 0, 2);
        main.assertKey(  9,10,  "|",     0, 3);
        data.test(1,             text,   0, 4);
        main.assertText(10, 13,  text,   0, 4, 0);
        main.assertKey( 13, 14, "\n",    0, 5);
        main.assertRest();
    }

    @Test
    public void matterSpaces(){
        String key = "   text-break";
        String text = "  lo  ";
        String raw = key + "   |center   |" + text + "\n";

        TextFileAssert doc = new TextFileAssert(1, raw);
        doc.addMatter(TextTypeMatter.TEXT_BREAK, 0);
        MatterLineAssert info = new MatterLineAssert(doc)
            .setRow(TextTypeMatter.TEXT_BREAK)
            .setType(TextDataType.CENTER)
            .setData(0, 4).setIndex(0);
        FormattedSpanAssert data = new FormattedSpanAssert(doc.getAsserter())
            .setPublish(1).setNote(0)
            .setParsed("lo");

        DocumentAssert main = doc.getAsserter();
        doc.test( 1,             raw);
        info.test(6,             raw,     0);
        main.assertField(0, 13,  key,     0, 0);
        main.assertKey( 13, 17, "   |",      0, 1);
        main.assertData(17, 23, "center", 0, 2);
        main.assertKey( 23, 27, "   |",   0, 3);
        data.test(1,             text,    0, 4);
        main.assertChild(1,     "  lo  ", 0, 4, 0);
        main.assertText(27, 33, "  lo  ", 0, 4, 0, 0);
        main.assertKey( 33, 34, "\n",     0, 5);
        main.assertRest();
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

    @Test
    public void editMatterAlginChange(){
        String raw = "text-break|left|Hello *WORLD*!\n";
        TextFileAssert doc = new TextFileAssert(1, raw);
        doc.getAsserter().call(TextSpanMatter.class,
            s -> s.setFormat(TextDataType.CENTER), 0);
        commonMatterSingle(doc);
    }

    private void commonMatterSingle(TextFileAssert doc){
        String key = "text-break";
        String text = "Hello *WORLD*!";
        String raw = key + "|center|" + text + "\n";

        doc.addMatter(TextTypeMatter.TEXT_BREAK, 0);
        MatterLineAssert info = new MatterLineAssert(doc)
            .setRow(TextTypeMatter.TEXT_BREAK)
            .setType(TextDataType.CENTER)
            .setData(0, 4).setIndex(0);
        FormattedSpanAssert data = new FormattedSpanAssert(doc.getAsserter())
            .setPublish(2).setNote(0)
            .setParsed("Hello WORLD!");

        DocumentAssert main = doc.getAsserter();
        doc.test( 1,             raw);
        info.test(6,             raw,     0);
        main.assertField(0, 10,  key,     0, 0);
        main.assertKey( 10, 11, "|",      0, 1);
        main.assertData(11, 17, "center", 0, 2);
        main.assertKey( 17, 18, "|",      0, 3);
        data.test(5,             text,    0, 4);
        main.assertChild(1,     "Hello ", 0, 4, 0);
        main.assertText(18, 24, "Hello ", 0, 4, 0, 0);
        main.assertKey( 24, 25, "*",      0, 4, 1);
        main.assertChild(1,     "WORLD",  0, 4, 2);
        main.assertText(25, 30, "WORLD",  0, 4, 2, 0);
        main.assertKey( 30, 31, "*",      0, 4, 3);
        main.assertChild(1,     "!",      0, 4, 4);
        main.assertText(31, 32, "!",      0, 4, 4, 0);
        main.assertKey( 32, 33, "\n",     0, 5);
        main.assertRest();
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
    public void matterDoubleNewExtra(){
        TextFileAssert doc = new TextFileAssert(0, "");
        doc.getAsserter().call(WritingData.class,
            s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Abc\\*\\\nHello\\"));
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
    public void matterDouble2LineExtra(){
        String line1 = "text-break|center|Joy\n";
        String line2 = "text-break|center|Fun\n";
        String raw = line1 + line2;
        TextFileAssert doc = new TextFileAssert(2, raw);
        doc.getAsserter().call(() -> doc.getDocument(),
            s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Abc\\*\\\nHello\\"),
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
        doc.getAsserter().call(() -> doc.getDocument(),
            s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Abc\\*\nHello"),
            () -> new SpanNode[]{
                doc.getDocument(),
                doc.getDocument().get(0),
                doc.getDocument().get(1)
            });
        commonMatterDouble(doc);
    }

    private void commonMatterDouble(TextFileAssert doc){
        String key = "text-break";
        String text1 = "Abc\\*";
        String text2 = "Hello";
        String line1 = key + "|center|" + text1 + "\n";
        String line2 = key + "|center|" + text2 + "\n";
        String raw = line1 + line2;

        doc.addMatter(TextTypeMatter.TEXT_BREAK, 0);
        doc.addMatter(TextTypeMatter.TEXT_BREAK, 1);
        MatterLineAssert matter1 = new MatterLineAssert(doc)
            .setRow(TextTypeMatter.TEXT_BREAK)
            .setType(TextDataType.CENTER)
            .setData(0, 4).setIndex(0);
        FormattedSpanAssert data1 = new FormattedSpanAssert(doc.getAsserter())
            .setPublish(1).setNote(0)
            .setParsed("Abc*");
        MatterLineAssert matter2 = new MatterLineAssert(doc)
            .setRow(TextTypeMatter.TEXT_BREAK)
            .setType(TextDataType.CENTER)
            .setData(1, 4).setIndex(1);
        FormattedSpanAssert data2 = new FormattedSpanAssert(doc.getAsserter())
            .setPublish(1).setNote(0)
            .setParsed("Hello");


        DocumentAssert main = doc.getAsserter();
        doc.test(    2, raw);
        matter1.test(6,          line1,    0);
        main.assertField( 0, 10, key,      0, 0);
        main.assertKey(  10, 11, "|",      0, 1);
        main.assertData( 11, 17, "center", 0, 2);
        main.assertKey(  17, 18,  "|",     0, 3);
        data1.test(1,           text1,     0, 4);
        main.assertChild(2,     text1,     0, 4, 0);
        main.assertText( 18, 21, "Abc",    0, 4, 0, 0);
        main.assertChild(2,     "\\*",     0, 4, 0, 1);
        main.assertKey(  21, 22, "\\",     0, 4, 0, 1, 0);
        main.assertText( 22, 23, "*",      0, 4, 0, 1, 1);
        main.assertKey(  23, 24, "\n",     0, 5);
        matter2.test(6,         line2,     1);
        main.assertField(24, 34, key,      1, 0);
        main.assertKey(  34, 35, "|",      1, 1);
        main.assertData( 35, 41, "center", 1, 2);
        main.assertKey(  41, 42, "|",      1, 3);
        data2.test(1,           text2,     1, 4);
        main.assertChild(1,     text2,     1, 4, 0);
        main.assertText(42, 47, "Hello",   1, 4, 0, 0);
        main.assertKey( 47, 48, "\n",      1, 5);
        main.assertRest();
    }

    //TODO mix tests

    private void mixed(TextFileAssert doc){
        String text1 = "Title";
        String text2 = "First, last";
        String text3 = "new-data";
        String text4 = "Today date";
        String line1 = "head-center|left|"  + text1 + "\n";
        String line2 = "meta-author|text|"   + text2 + "\n";
        String line3 = "new-version|joy|"    + text3 + "\n";
        String line4 = "head-center|center|" + text4 + "\n";

        String raw = line1 + line2 + line3 + line4;
        doc.addMatter(TextTypeMatter.FRONT_CENTER, 0);
        doc.addInfo(  TextTypeInfo.  AUTHOR,       text2);
        doc.addMatter(TextTypeMatter.FRONT_CENTER, 3);

        MatterLineAssert matter1 = new MatterLineAssert(doc)
            .setRow(TextTypeMatter.FRONT_CENTER)
            .setType(TextDataType.CENTER)
            .setData(0, 5).setIndex(0);
        FormattedSpanAssert data1 = new FormattedSpanAssert(doc.getAsserter())
            .setPublish(1).setNote(0)
            .setParsed("Abc*");
        MatterLineAssert matter2 = new MatterLineAssert(doc)
            .setRow(TextTypeMatter.FRONT_CENTER)
            .setType(TextDataType.CENTER)
            .setData(1, 5).setIndex(2);
        FormattedSpanAssert data2 = new FormattedSpanAssert(doc.getAsserter())
            .setPublish(1).setNote(0)
            .setParsed("Hello");

    }
}

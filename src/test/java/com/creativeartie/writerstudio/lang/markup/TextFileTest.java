package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

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
        doc.getAsserter().setListenTester(ListenerAssert
            .builder(
                s -> s.setInfo(TextTypeInfo.KEYWORDS, "key-1, key-2, key-etc"),
                doc.getAsserter(), WritingData.class
            ).setEdited()
        );
        commonInfo(doc);
    }

    @Test
    public void editInfoOld(){
        String raw = "meta-keywords|text|abc";
        TextFileAssert doc = new TextFileAssert(1, raw);
        doc.getAsserter().setListenTester(ListenerAssert
            .builder(
                s -> s.setInfo(TextTypeInfo.KEYWORDS, "key-1, key-2, key-etc"),
                doc.getAsserter(), WritingData.class
            ).replacesChildren(0)
        );
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
        doc.getAsserter().setListenTester(ListenerAssert
            .builder(
                s -> s.setInfo(TextTypeInfo.KEYWORDS, "\\adf"),
                doc.getAsserter(), WritingData.class
            ).setEdited()
        );
        commonInfoEscapes(doc);
    }

    @Test
    public void editInfoEscapesOld(){
        String raw = "meta-keywords|text|adedafasfafas\\\\adfasdfsaf\n";
        TextFileAssert doc = new TextFileAssert(1, raw);
        doc.getAsserter().setListenTester(ListenerAssert
            .builder(
                s -> s.setInfo(TextTypeInfo.KEYWORDS, "\\adf"),
                doc.getAsserter(), WritingData.class
            ).replacesChildren(0)
        );

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
        String raw = key + "   |centre   |" + text + "\n";

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
        main.assertData(17, 23, "centre", 0, 2);
        main.assertKey( 23, 27, "   |",   0, 3);
        data.test(1,             text,    0, 4);
        main.assertChild(1,     "  lo  ", 0, 4, 0);
        main.assertText(27, 33, "  lo  ", 0, 4, 0, 0);
        main.assertKey( 33, 34, "\n",     0, 5);
        main.assertRest();
    }

    @Test
    public void matterBasic(){
        String raw = "text-break|centre|Hello *WORLD*!\n";
        TextFileAssert doc = new TextFileAssert(1, raw);
        commonMatterSingle(doc);
    }

    @Test
    public void editMatterNew(){
        String raw = "";
        TextFileAssert doc = new TextFileAssert(0, raw);

        doc.getAsserter().setListenTester(ListenerAssert
            .builder(
                s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Hello *WORLD*!"),
                doc.getAsserter(), WritingData.class
            ).setEdited()
        );

        commonMatterSingle(doc);
    }

    @Test
    public void editMatterNewEscape(){
        String raw = "";
        TextFileAssert doc = new TextFileAssert(0, raw);

        doc.getAsserter().setListenTester(ListenerAssert
            .builder(
                s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Hello *WORLD*!\\"),
                doc.getAsserter(), WritingData.class
            ).setEdited()
        );

        commonMatterSingle(doc);
    }

    @Test
    public void editMatterEdited(){
        String raw = "text-break|centre|abc\n";
        TextFileAssert doc = new TextFileAssert(1, raw);
        doc.getAsserter().setListenTester(ListenerAssert
            .builder(
                s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Hello *WORLD*!"),
                doc.getAsserter(), WritingData.class
            ).replacesChildren(0)
        );

        commonMatterSingle(doc);
    }

    @Test
    public void editMatterEditedEscape(){
        String raw = "text-break|centre|abc\n";
        TextFileAssert doc = new TextFileAssert(1, raw);
        doc.getAsserter().setListenTester(ListenerAssert
            .builder(
                s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Hello *WORLD*!\\"),
                doc.getAsserter(), WritingData.class
            ).replacesChildren(0)
        );

        commonMatterSingle(doc);
    }

    @Test
    public void editMatterAlginChange(){
        String raw = "text-break|left|Hello *WORLD*!\n";
        TextFileAssert doc = new TextFileAssert(1, raw);
        doc.getAsserter().setListenTester(ListenerAssert
            .builder(
                s -> s.setFormat(TextDataType.CENTER),
                doc.getAsserter(), TextSpanMatter.class, 0
            ).replacesChildren(0)
        );

        commonMatterSingle(doc);
    }

    private void commonMatterSingle(TextFileAssert doc){
        String key = "text-break";
        String text = "Hello *WORLD*!";
        String raw = key + "|centre|" + text + "\n";

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
        main.assertData(11, 17, "centre", 0, 2);
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
        String raw = "text-break|centre|Abc\\*\n" +
                     "text-break|centre|Hello\n";
        TextFileAssert doc = new TextFileAssert(2, raw);
        commonMatterDouble(doc);
    }

    @Test
    public void matterDoubleNew(){
        TextFileAssert doc = new TextFileAssert(0, "");
        doc.getAsserter().setListenTester(ListenerAssert
            .builder(
                s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Abc\\*\nHello"),
                doc.getAsserter(), WritingData.class
            ).replacesChildren()
        );
        commonMatterDouble(doc);
    }

    @Test
    public void matterDoubleNewExtra(){
        TextFileAssert doc = new TextFileAssert(0, "");
        doc.getAsserter().setListenTester(ListenerAssert
            .builder(
                s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Abc\\*\\\nHello\\"),
                doc.getAsserter(), WritingData.class
            ).replacesChildren()
        );
        commonMatterDouble(doc);
    }

    @Test
    public void matterDouble1Line(){
        String raw = "text-break|centre|Abc\\*\n";
        TextFileAssert doc = new TextFileAssert(1, raw);
        doc.getAsserter().setListenTester(ListenerAssert
            .builder(
                s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Abc\\*\\\nHello"),
                doc.getAsserter(), WritingData.class
            ).replacesChildren(0)
            .setEdited()
        );
        commonMatterDouble(doc);
    }

    @Test
    public void matterDouble2Line(){
        String line1 = "text-break|centre|Joy\n";
        String line2 = "text-break|centre|Fun\n";
        String raw = line1 + line2;
        TextFileAssert doc = new TextFileAssert(2, raw);
        doc.getAsserter().setListenTester(ListenerAssert
            .builder(
                s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Abc\\*\\\nHello"),
                doc.getAsserter(), WritingData.class
            ).replacesChildren(0)
            .replacesChildren(1)
        );
        commonMatterDouble(doc);
    }

    @Test
    public void matterDouble2LineExtra(){
        String line1 = "text-break|centre|Joy\n";
        String line2 = "text-break|centre|Fun\n";
        String raw = line1 + line2;
        TextFileAssert doc = new TextFileAssert(2, raw);
        doc.getAsserter().setListenTester(ListenerAssert
            .builder(
                s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Abc\\*\\\nHello"),
                doc.getAsserter(), WritingData.class
            ).replacesChildren(0)
            .replacesChildren(1)
        );
        commonMatterDouble(doc);
    }

    @Test
    public void matterDouble3Line(){
        String line1 = "text-break|centre|Joy\n";
        String line2 = "text-break|centre|Fun\n";
        String line3 = "text-break|centre|Cool\n";
        String raw = line1 + line2 + line3;
        TextFileAssert doc = new TextFileAssert(3, raw);
        doc.getAsserter().setListenTester(ListenerAssert
            .builder(
                s -> s.setMatter(TextTypeMatter.TEXT_BREAK, "Abc\\*\\\nHello"),
                doc.getAsserter(), WritingData.class
            ).replacesChildren(0)
            .replacesChildren(1)
            .setRemoved(2)
        );
        commonMatterDouble(doc);
    }

    private void commonMatterDouble(TextFileAssert doc){
        String key = "text-break";
        String text1 = "Abc\\*";
        String text2 = "Hello";
        String line1 = key + "|centre|" + text1 + "\n";
        String line2 = key + "|centre|" + text2 + "\n";
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
        main.assertData( 11, 17, "centre", 0, 2);
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
        main.assertData( 35, 41, "centre", 1, 2);
        main.assertKey(  41, 42, "|",      1, 3);
        data2.test(1,           text2,     1, 4);
        main.assertChild(1,     text2,     1, 4, 0);
        main.assertText(42, 47, "Hello",   1, 4, 0, 0);
        main.assertKey( 47, 48, "\n",      1, 5);
        main.assertRest();
    }

    //TODO mix tests

    @Test
    public void manyMixed(){
        String line1 = "head-centre|left|Title\n";
        String line2 = "meta-author|text|First, last\n";
        String line3 = "new-version|joy|new-data\n";
        String line4 = "head-centre|centre|Today date\n";

        String raw = line1 + line2 + line3 + line4;
        TextFileAssert doc = new TextFileAssert(4, raw);
        commonMixed(doc);
    }

    @Test
    public void editMixed(){
        String line1 = "head-centre|left|Title\n";
        String line2 = "meta-author|text|First, last\n";
        String line3 = "new-version|joy|new-data\n";

        String raw = line1 + line2 + line3;
        TextFileAssert doc = new TextFileAssert(3, raw);

        doc.getAsserter().setListenTester(ListenerAssert
            .builder(
                s -> s.setMatter(TextTypeMatter.FRONT_CENTER, "Title\nToday date"),
                doc.getAsserter(), WritingData.class
            ).replacesChildren(0)
            .setEdited()
        );
        commonMixed(doc);
    }


    private void commonMixed(TextFileAssert doc){
        String key1  = "head-centre";
        String key2  = "meta-author";
        String key3  = "new-version";

        String text1 = "Title";
        String text2 = "First, last";
        String text3 = "new-data";
        String text4 = "Today date";

        String centre = "centre";

        String line1 = key1 + "|left|"          + text1 + "\n";
        String line2 = key2 + "|text|"          + text2 + "\n";
        String line3 = key3 + "|joy|"           + text3 + "\n";
        String line4 = key1 + "|" + centre +"|" + text4 + "\n";

        String raw = line1 + line2 + line3 + line4;

        doc.addMatter(TextTypeMatter.FRONT_CENTER, 0);
        doc.addInfo(TextTypeInfo.AUTHOR, text2);
        doc.addMatter(TextTypeMatter.FRONT_CENTER, 3);

        MatterLineAssert matter1 = new MatterLineAssert(doc)
            .setRow(TextTypeMatter.FRONT_CENTER)
            .setType(TextDataType.LEFT)
            .setData(0, 4).setIndex(0);
        FormattedSpanAssert data1 = new FormattedSpanAssert(doc.getAsserter())
            .setPublish(1).setNote(0)
            .setParsed(text1);

        InfoLineAssert info = new InfoLineAssert(doc)
            .setRow(TextTypeInfo.AUTHOR)
            .setData(1, 4);
        ContentAssert data2 = new ContentAssert(doc.getAsserter())
            .setBegin(false).setBoth(text2)
            .setEnd(false)  .setCount(2);

        UnknownAssert unknown = new UnknownAssert(doc)
            .setType(TextDataType.TEXT)
            .setData(2, 4);
        ContentAssert data3 = new ContentAssert(doc.getAsserter())
            .setBegin(false).setBoth(text3)
            .setEnd(false)  .setCount(1);

        MatterLineAssert matter2 = new MatterLineAssert(doc)
            .setRow(TextTypeMatter.FRONT_CENTER)
            .setType(TextDataType.CENTER)
            .setData(3, 4).setIndex(1);
        FormattedSpanAssert data4 = new FormattedSpanAssert(doc.getAsserter())
            .setPublish(2).setNote(0)
            .setParsed(text4);

        DocumentAssert main = doc.getAsserter();

        doc.test(4,               raw);
        matter1.test(6,           line1,  0);
        main.assertField( 0,  11, key1,   0, 0);
        main.assertKey(  11,  12, "|",    0, 1);
        main.assertData( 12,  16, "left", 0, 2);
        main.assertKey(  16,  17, "|",    0, 3);
        data1.test(1,             text1,  0, 4);
        main.assertChild(1,       text1,  0, 4, 0);
        main.assertText(17,  22,  text1,  0, 4, 0, 0);
        main.assertKey( 22,  23, "\n",    0, 5);

        info.test(6,             line2,  1);
        main.assertField(23,  34, key2,   1, 0);
        main.assertKey(  34,  35, "|",    1, 1);
        main.assertData( 35,  39, "text", 1, 2);
        main.assertKey(  39,  40, "|",    1, 3);
        data2.test(1,             text2,  1, 4);
        main.assertText(40,  51,  text2,  1, 4, 0);
        main.assertKey( 51,  52, "\n",    1, 5);

        unknown.test(6,           line3,  2);
        main.assertField(52,  63, key3,   2, 0);
        main.assertKey(  63,  64, "|",    2, 1);
        main.assertData( 64,  67, "joy",  2, 2);
        main.assertKey(  67,  68, "|",    2, 3);
        data3.test(1,             text3,  2, 4);
        main.assertText(68,   76, text3,  2, 4, 0);
        main.assertKey( 76,   77, "\n",    2, 5);

        matter2.test(6,           line4,  3);
        main.assertField(77,  88, key1,   3, 0);
        main.assertKey(  88,  89, "|",    3, 1);
        main.assertData( 89,  95, centre, 3, 2);
        main.assertKey(  95,  96, "|",    3, 3);
        data4.test(1,             text4,  3, 4);
        main.assertChild(1,       text4,  3, 4, 0);
        main.assertText( 96, 106, text4,  3, 4, 0, 0);
        main.assertKey( 106, 107, "\n",    3, 5);
        main.assertRest();
    }

    @ParameterizedTest(name = "TextTypeInfo.{2}")
    @CsvSource({ "author,Jane Doe,AUTHOR",
        "keywords,'test1, manuscript',KEYWORDS",
        "subject,test file,SUBJECT",
        "title,Some Novel,TITLE"
    })
    public void infoTest(String name, String data, String base){
        String raw = "meta-" + name + "|text|" + data + "\n";
        TextTypeInfo type = TextTypeInfo.valueOf(base);

        TextFileAssert doc = new TextFileAssert(1, raw);
        DocumentAssert main = doc.getAsserter();

        doc.addInfo(type, data);
        InfoLineAssert info = new InfoLineAssert(doc)
            .setRow(type)
            .setData(0, 4);
        ContentAssert content = new ContentAssert(main)
            .setBegin(false).setBoth(data)
            .setEnd(false)  .setCount(2);

        doc.test(1, raw);
        info.test(6, raw, 0);
        content.test(1, data, 0, 4);
    }

    @ParameterizedTest(name = "TextTypeMatter.{2}")
    @CsvSource({ "head-top,Hello,FRONT_TOP",
        "head-centre,Hello,FRONT_CENTER",
        "head-bottom,Hello,FRONT_BOTTOM",
        "text-header,Hello,TEXT_HEADER",
        "text-break,Hello,TEXT_BREAK",
        "text-ender,Hello,TEXT_ENDER",
        "cite-starter,Hello,SOURCE_STARTER"
    })
    public void matterTest(String name, String data, String base){
        String raw = name + "|centre|" + data + "\n";
        TextTypeMatter type = TextTypeMatter.valueOf(base);

        TextFileAssert doc = new TextFileAssert(1, raw);
        DocumentAssert main = doc.getAsserter();

        doc.addMatter(type, 0);
        MatterLineAssert matter = new MatterLineAssert(doc)
            .setRow(type)
            .setType(TextDataType.CENTER)
            .setData(0, 4).setIndex(0);
        FormattedSpanAssert content = new FormattedSpanAssert(main)
            .setPublish(1).setNote(0)
            .setParsed(data);

        doc.test(1, raw);
        matter.test(6, raw, 0);
        content.test(1, data, 0, 4);
        main.assertRest();
    }
}

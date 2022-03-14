package com.creativeartie.writer.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.BranchBasicAsserts.*;
import com.creativeartie.writer.lang.markup.BranchFormatAsserts.*;

import static com.creativeartie.writer.lang.DocumentAssert.*;

public class FormatKeyTest {
    private static final SetupParser parser = new FormatParsePointKey(new
        boolean[4]);

    @Test
    public void statErrorText(){
        String ref = "Error";
        String raw = "{%" + ref + "}";
        DocumentAssert doc = assertDoc(1, raw, parser);

        FormatKeyAssert key = new FormatKeyAssert(doc)
            .setField(FormatTypeField.ERROR);
        ContentAssert data = new ContentAssert(doc)
            .setBegin(false).setBoth(ref)
            .setEnd(false)  .setCount(1);

        key.test(3,           raw,  0);
        doc.assertKey(  0, 2, "{%", 0, 0);
        data.test(1,          ref,  0, 1);
        doc.assertField(2, 7, ref,  0, 1, 0);
        doc.assertKey ( 7, 8, "}",  0, 2);
        doc.assertRest();
    }

    @Test
    public void statErrorMisspell(){
        String ref = "Stats.PagsNumber";
        String raw = "{%" + ref + "}";
        DocumentAssert doc = assertDoc(1, raw, parser);

        FormatKeyAssert key = new FormatKeyAssert(doc)
            .setField(FormatTypeField.ERROR);
        ContentAssert data = new ContentAssert(doc)
            .setBegin(false).setBoth(ref)
            .setEnd(false)  .setCount(1);

        key.test(3,             raw,  0);
        doc.assertKey(  0,  2,  "{%", 0, 0);
        data.test(1,            ref,  0, 1);
        doc.assertField(2,  18, ref,  0, 1, 0);
        doc.assertKey ( 18, 19, "}",  0, 2);
        doc.assertRest();
    }

    @Test
    public void editWordCount(){
        String before = "{%ats.WordCountEst  }";
        DocumentAssert doc = assertDoc(1, before, parser);
        doc.insert(2, "St", 0);
        commonWordCount(doc);
    }

    @Test
    public void statWordCount(){
        String before = "{%Stats.WordCountEst  }";
        DocumentAssert doc = assertDoc(1, before, parser);
        commonWordCount(doc);
    }

    private void commonWordCount(DocumentAssert doc){
        String ref = "Stats.WordCountEst  ";
        String text = "Stats.WordCountEst ";
        String raw = "{%" + ref + "}";
        doc.assertDoc(1, raw);

        FormatKeyAssert key = new FormatKeyAssert(doc)
            .setField(FormatTypeField.WORD_COUNT);
        ContentAssert data = new ContentAssert(doc)
            .setBegin(false).setBoth(text)
            .setEnd(true)  .setCount(1);

        key.test(3,             raw,  0);
        doc.assertKey(  0,  2,  "{%", 0, 0);
        data.test(1,            ref,  0, 1);
        doc.assertField(2,  22, ref,  0, 1, 0);
        doc.assertKey ( 22, 23, "}",  0, 2);
        doc.assertRest();
    }

    @Test
    public void statPageNumber(){
        String ref = "Stats.PageNumber";
        String raw = "{%" + ref + "}";
        DocumentAssert doc = assertDoc(1, raw, parser);

        FormatKeyAssert key = new FormatKeyAssert(doc)
            .setField(FormatTypeField.PAGE_NUMBER);
        ContentAssert data = new ContentAssert(doc)
            .setBegin(false).setBoth(ref)
            .setEnd(false)  .setCount(1);

        key.test(3,             raw,  0);
        doc.assertKey(  0,  2,  "{%", 0, 0);
        data.test(1,            ref,  0, 1);
        doc.assertField(2,  18, ref,  0, 1, 0);
        doc.assertKey ( 18, 19, "}",  0, 2);
        doc.assertRest();
    }

    @Test
    public void startOnly(){
        ///           01
        String raw = "{%";
        DocumentAssert doc = assertDoc(1, raw, parser);

        FormatKeyAssert key = new FormatKeyAssert(doc)
            .setField(FormatTypeField.ERROR);

        key.test(1,           raw,   0);
        doc.assertKey(  0, 2, "{%",  0, 0);
        doc.assertRest();
    }

    @Test
    public void twoWords(){
        ///           0123456
        String raw = "{%a b}";
        DocumentAssert doc = assertDoc(1, raw, parser);

        FormatKeyAssert key = new FormatKeyAssert(doc)
            .setField(FormatTypeField.ERROR);
        ContentAssert data = new ContentAssert(doc)
            .setBegin(false).setBoth("a b")
            .setEnd(false)  .setCount(2);

        key.test(3,           raw,   0);
        doc.assertKey(  0, 2, "{%",  0, 0);
        data.test(1,          "a b", 0, 1);
        doc.assertField(2, 5, "a b", 0, 1, 0);
        doc.assertKey ( 5, 6, "}",   0, 2);
        doc.assertRest();
    }

    @Test
    public void noSpacesOnly(){
        ///           0123456
        String raw = "{%   }";
        DocumentAssert doc = assertDoc(1, raw, parser);

        FormatKeyAssert key = new FormatKeyAssert(doc)
            .setField(FormatTypeField.ERROR);
        ContentAssert data = new ContentAssert(doc)
            .setBegin(true).setBoth(" ")
            .setEnd(true)  .setCount(0);

        key.test(3,           raw,   0);
        doc.assertKey(  0, 2, "{%",  0, 0);
        data.test(1,          "   ", 0, 1);
        doc.assertField(2, 5, "   ", 0, 1, 0);
        doc.assertKey ( 5, 6, "}",   0, 2);
        doc.assertRest();
        doc.assertRest();
    }

    @Test
    public void noText(){
        ///           012
        String raw = "{%}";
        DocumentAssert doc = assertDoc(1, raw, parser);

        FormatKeyAssert key = new FormatKeyAssert(doc)
            .setField(FormatTypeField.ERROR);

        key.test(2,         raw,   0);
        doc.assertKey(0, 2, "{%",  0, 0);
        doc.assertKey(2, 3, "}",   0, 1);
        doc.assertRest();
    }

    @Test
    public void noEnding(){
        ///           012345
        String raw = "{%abc";
        DocumentAssert doc = assertDoc(1, raw, parser);

        FormatKeyAssert key = new FormatKeyAssert(doc)
            .setField(FormatTypeField.ERROR);
        ContentAssert data = new ContentAssert(doc)
            .setBegin(false).setBoth("abc")
            .setEnd(false)  .setCount(1);

        key.test(2,           raw,   0);
        doc.assertKey(  0, 2, "{%",  0, 0);
        data.test(1,          "abc", 0, 1);
        doc.assertField(2, 5, "abc", 0, 1, 0);
        doc.assertRest();
    }

    @Test
    public void basic(){
        ///              0123456
        String before = "{%abc}";
        DocumentAssert doc = assertDoc(1, before, parser);
        commonBasic(doc);
    }

    @Test
    public void editAddText(){
        ///              0123456
        String before = "{%}";
        DocumentAssert doc = assertDoc(1, before, parser);
        doc.insert(2, "abc", 0);
        commonBasic(doc);
    }

    @Test
    public void editRemoveText(){
        ///              0123456789
        String before = "{%abcded}";
        DocumentAssert doc = assertDoc(1, before, parser);
        doc.delete(5, 8, 0);
        commonBasic(doc);
    }

    @Test
    public void editAddEnding(){
        ///              012345
        String before = "{%abc";
        DocumentAssert doc = assertDoc(1, before, parser);
        doc.insert(5, "}", 0);
        commonBasic(doc);
    }

    private void commonBasic(DocumentAssert doc){
        String raw = "{%abc}";
        doc.assertDoc(1, raw);

        FormatKeyAssert key = new FormatKeyAssert(doc)
            .setField(FormatTypeField.ERROR);
        ContentAssert data = new ContentAssert(doc)
            .setBegin(false).setBoth("abc")
            .setEnd(false)  .setCount(1);

        key.test(3,            raw,   0);
        doc.assertKey(  0, 2, "{%",  0, 0);
        data.test(1,          "abc", 0, 1);
        doc.assertField(2, 5, "abc", 0, 1, 0);
        doc.assertKey ( 5, 6, "}",   0, 2);
        doc.assertRest();
    }
}

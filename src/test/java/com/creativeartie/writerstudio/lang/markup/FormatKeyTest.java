package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.BranchBasicAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchFormatAsserts.*;

import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

public class FormatKeyTest {
    private static final SetupParser parser = new FormatParsePointKey(new
        boolean[4]);

    // TODO Stats.PageNumber, Stats.WordCountEst, Error

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
            .setBegin(true).setBoth("")
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
        doc.assertDoc(1, raw, parser);

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

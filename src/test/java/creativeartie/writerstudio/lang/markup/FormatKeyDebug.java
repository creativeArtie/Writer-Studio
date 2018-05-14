package com.creativeartie.writerstudio.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
import static com.creativeartie.writerstudio.lang.markup.BranchTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchFormatTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.io.File;

import com.creativeartie.writerstudio.lang.*;

@RunWith(JUnit4.class)
public class FormatKeyDebug {
    private static final SetupParser parser = new FormatParsePointKey(new
        boolean[4]);

    @Test
    public void startOnly(){
        ///           01
        String raw = "{%";
        DocumentAssert doc = assertDoc(1, raw, parser);

        FormatKeyTest key = new FormatKeyTest()
            .setField("");

        key.test(         doc, 1, raw,   0);
        doc.assertKeyLeaf(  0, 2, "{%",  0, 0);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void twoWords(){
        ///           0123456
        String raw = "{%a b}";
        DocumentAssert doc = assertDoc(1, raw, parser);

        FormatKeyTest key = new FormatKeyTest()
            .setField("a b");
        ContentTest data = new ContentTest()
            .setBegin(false).setText("a b")
            .setEnd(false)  .setCount(2);

        key.test(         doc, 3, raw,   0);
        doc.assertKeyLeaf(  0, 2, "{%",  0, 0);
        data.test(        doc, 1, "a b", 0, 1);
        doc.assertFieldLeaf(2, 5, "a b", 0, 1, 0);
        doc.assertKeyLeaf ( 5, 6, "}",   0, 2);
        doc.assertLast();
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void noSpacesOnly(){
        ///           0123456
        String raw = "{%   }";
        DocumentAssert doc = assertDoc(1, raw, parser);

        FormatKeyTest key = new FormatKeyTest()
            .setField("");
        ContentTest data = new ContentTest()
            .setBegin(true).setText("")
            .setEnd(true)  .setCount(0);

        key.test(         doc, 3, raw,   0);
        doc.assertKeyLeaf(  0, 2, "{%",  0, 0);
        data.test(        doc, 1, "   ", 0, 1);
        doc.assertFieldLeaf(2, 5, "   ", 0, 1, 0);
        doc.assertKeyLeaf ( 5, 6, "}",   0, 2);
        doc.assertLast();
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void noText(){
        ///           012
        String raw = "{%}";
        DocumentAssert doc = assertDoc(1, raw, parser);

        FormatKeyTest key = new FormatKeyTest()
            .setField("");

        key.test(         doc, 2, raw,   0);
        doc.assertKeyLeaf(  0, 2, "{%",  0, 0);
        doc.assertKeyLeaf ( 2, 3, "}",   0, 1);
        doc.assertLast();
        doc.assertIds();
    }

    @Test
    public void noEnding(){
        ///           012345
        String raw = "{%abc";
        DocumentAssert doc = assertDoc(1, raw, parser);

        FormatKeyTest key = new FormatKeyTest()
            .setField("abc");
        ContentTest data = new ContentTest()
            .setBegin(false).setText("abc")
            .setEnd(false)  .setCount(1);

        key.test(         doc, 2, raw,   0);
        doc.assertKeyLeaf(  0, 2, "{%",  0, 0);
        data.test(        doc, 1, "abc", 0, 1);
        doc.assertFieldLeaf(2, 5, "abc", 0, 1, 0);
        doc.assertLast();
        doc.assertIds();
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

        FormatKeyTest key = new FormatKeyTest()
            .setField("abc");
        ContentTest data = new ContentTest()
            .setBegin(false).setText("abc")
            .setEnd(false)  .setCount(1);

        key.test(         doc, 3, raw,   0);
        doc.assertKeyLeaf(  0, 2, "{%",  0, 0);
        data.test(        doc, 1, "abc", 0, 1);
        doc.assertFieldLeaf(2, 5, "abc", 0, 1, 0);
        doc.assertKeyLeaf ( 5, 6, "}",   0, 2);
        doc.assertLast();
        doc.assertIds();
    }
}
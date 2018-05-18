package com.creativeartie.writerstudio.lang;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import java.util.*;
import java.util.Optional;
import java.util.function.*;

import com.google.common.base.*;

import com.creativeartie.writerstudio.lang.markup.WritingText;

@RunWith(JUnit4.class)
public class SetupPointerDebug{

    @Test
    public void trimStartsWithEmpty(){
        test("", (p, c) -> p.trimStartsWith(c, ":"));
    }

    @Test
    public void trimStartsWithSpaces(){
        test("  ", (p, c) -> p.trimStartsWith(c, ":"));
    }

    @Test
    public void trimStartsWithToken(){
        test(":", (p, c) -> p.trimStartsWith(c, ":"), ":",
            StyleInfoLeaf.KEYWORD);
    }

    @Test
    public void trimStartsWithSpaceToken(){
        test("  :", (p, c) -> p.trimStartsWith(c, ":"), "  :",
            StyleInfoLeaf.KEYWORD);
    }

    @Test
    public void trimStartsWithNext(){
        SetupPointer ptr = test("  :1", (p, c) ->  p.trimStartsWith(c, ":"),
            "  :", StyleInfoLeaf.KEYWORD);
        test(ptr, (p, c) ->  p.trimStartsWith(c, ":"));
        test(ptr, (p, c) ->  p.trimStartsWith(c, StyleInfoLeaf.DATA, "1"),
            "1", StyleInfoLeaf.DATA);
    }

    @Test
    public void trimStartsWithSpaceTokenExtra(){
        test("  :dd", (p, c) -> p.trimStartsWith(c, ":"), "  :",
            StyleInfoLeaf.KEYWORD);
    }

    @Test
    public void trimStartsWithText(){
        test("abc:", (p, c) -> p.trimStartsWith(c, ":"));
    }

    @Test
    public void trimStartsWithSpaceText(){
        test("  abc", (p, c) -> p.trimStartsWith(c, ":"));
    }

    @Test
    public void startWithStringTrue(){
        test(":", (p, c) -> p.startsWith(c, ":"), ":", StyleInfoLeaf.KEYWORD);
    }

    @Test
    public void startWithStringNext(){
        SetupPointer ptr = test(":1", (p, c) ->  p.startsWith(c, ":"), ":",
            StyleInfoLeaf.KEYWORD);
        test(ptr, (p, c) ->  p.startsWith(c, ":"));
        test(ptr, (p, c) ->  p.startsWith(c, StyleInfoLeaf.DATA, "1"),
            "1", StyleInfoLeaf.DATA);
    }

    @Test
    public void startWithStringFalse(){
        test("abc", (p, c) -> p.startsWith(c, ":"));
    }

    @Test
    public void startWithStringSpace(){
        test("  ", (p, c) -> p.startsWith(c, ":"));
    }

    @Test
    public void startWithStringEmpty(){
        test("", (p, c) -> p.startsWith(c, ":"));
    }

    @Test
    public void matchSimple(){
        test("abcd", (p, c) -> p.matches(c, CharMatcher.javaLetter()), "abcd",
            StyleInfoLeaf.DATA);
    }

    @Test
    public void matchesExtra(){
        SetupPointer ptr = test("abcd1",
            (p, c) -> p.matches(c, CharMatcher.javaLetter()), "abcd",
            StyleInfoLeaf.DATA);
        test(ptr, (p, c) -> p.matches(c, CharMatcher.javaLetter()));
        test(ptr, (p, c) -> p.matches(c, CharMatcher.javaDigit()), "1",
            StyleInfoLeaf.DATA);
    }

    @Test
    public void matchesEmpty(){
        test("", (p, c) -> p.matches(c, CharMatcher.javaLetter()));
    }

    @Test
    public void getToAll(){
        test("abc", (p, c) -> p.getTo(c, StyleInfoLeaf.TEXT, ":", ";"), "abc",
            StyleInfoLeaf.TEXT);
    }

    @Test
    public void getToPart1(){
        test("abc:", (p, c) -> p.getTo(c, ":", ";"), "abc",
            StyleInfoLeaf.KEYWORD);
    }

    @Test
    public void getToPart2(){
        test("abc;", (p, c) -> p.getTo(c, ":", ";"), "abc",
            StyleInfoLeaf.KEYWORD);
    }

    @Test
    public void getToExtra(){
        SetupPointer ptr = test("abc:", (p, c) -> p.getTo(c, ":"), "abc",
            StyleInfoLeaf.KEYWORD);
        test(ptr, (p, c) -> p.getTo(c, ":", ";"));
        test(ptr, (p, c) -> p.getTo(c, ";"), ":", StyleInfoLeaf.KEYWORD);
        test(ptr, (p, c) -> p.getTo(c, ":", ";"));
    }

    @Test
    public void getToEmpty(){
        test("", (p, c) -> p.getTo(c, ":"));
    }

    @Test
    public void nextCharsAlll(){
        test("abcde", (p, c) -> p.nextChars(c, 5), "abcde", StyleInfoLeaf.KEYWORD);
    }

    @Test
    public void nextCharsSplit(){
        SetupPointer ptr = test("abcdef", (p, c) -> p.nextChars(c, 2), "ab",
            StyleInfoLeaf.KEYWORD);
        test(ptr, (p, c) -> p.nextChars(c, 2), "cd", StyleInfoLeaf.KEYWORD);
        test(ptr, (p, c) -> p.nextChars(c, 2), "ef", StyleInfoLeaf.KEYWORD);
        test(ptr, (p, c) -> p.nextChars(c, 2));
    }

    @Test
    public void nextCharsEmpty(){
        test("", (p, c) -> p.nextChars(c, 5));
    }

    @Test
    public void nextCharsTooShort(){
        test("abc", (p, c) -> p.nextChars(c, 5));
    }

    @Test
    public void rollBackBegin(){
        SetupPointer ptr = makePointer("abc*po*av");
        ptr.mark();
        test(ptr, (p, c) -> p.getTo(c, "*"), "abc", StyleInfoLeaf.KEYWORD);
        ptr.rollBack();
        test(ptr, (p, c) -> p.getTo(c, "*"), "abc", StyleInfoLeaf.KEYWORD);
    }

    @Test
    public void rollBackBeginLate(){
        SetupPointer ptr = makePointer("abc*po*av");
        ptr.mark();
        test(ptr, (p, c) -> p.getTo(c, "*"), "abc", StyleInfoLeaf.KEYWORD);
        test(ptr, (p, c) -> p.startsWith(c, "*"), "*", StyleInfoLeaf.KEYWORD);
        ptr.rollBack();
        test(ptr, (p, c) -> p.getTo(c, "*"), "abc", StyleInfoLeaf.KEYWORD);
    }

    @Test
    public void rollBackBeginEnd(){
        SetupPointer ptr = makePointer("abc*po");
        ptr.mark();
        test(ptr, (p, c) -> p.getTo(c, "*"), "abc", StyleInfoLeaf.KEYWORD);
        test(ptr, (p, c) -> p.startsWith(c, "*"), "*", StyleInfoLeaf.KEYWORD);
        test(ptr, (p, c) -> p.getTo(c, "*"), "po", StyleInfoLeaf.KEYWORD);
        ptr.rollBack();
        test(ptr, (p, c) -> p.getTo(c, "*"), "abc", StyleInfoLeaf.KEYWORD);
    }

    @Test
    public void rollBackMiddle(){
        SetupPointer ptr = makePointer("abc*po*av");
        test(ptr, (p, c) -> p.getTo(c, "*"), "abc", StyleInfoLeaf.KEYWORD);
        test(ptr, (p, c) -> p.startsWith(c, "*"), "*", StyleInfoLeaf.KEYWORD);
        ptr.mark();
        test(ptr, (p, c) -> p.getTo(c, "*"), "po", StyleInfoLeaf.KEYWORD);
        ptr.rollBack();
        test(ptr, (p, c) -> p.getTo(c, "*"), "po", StyleInfoLeaf.KEYWORD);
    }

    private SetupPointer test(String text,
            BiPredicate<SetupPointer, ArrayList<Span>> tester){
        return test(makePointer(text), tester);
    }

    private SetupPointer test(SetupPointer ptr,
             BiPredicate<SetupPointer, ArrayList<Span>> tester){
        ArrayList<Span> test = new ArrayList<>();
        assertFalse("main", tester.test(ptr, test));
        assertEquals("Size", test.size(), 0);
        return ptr;
    }

    private SetupPointer test(String text, BiPredicate<SetupPointer,
            ArrayList<Span>> tester, String raw, StyleInfoLeaf style){
        return test(makePointer(text), tester, raw, style);
    }

    private SetupPointer test(SetupPointer ptr, BiPredicate<SetupPointer,
            ArrayList<Span>> tester, String raw, StyleInfoLeaf style){
        ArrayList<Span> test = new ArrayList<>();

        assertTrue("main", tester.test(ptr, test));
        assertEquals("Size", test.size(), 1);
        assertEquals("raw", raw, test.get(0).getRaw());
        assertEquals("style", style, ((SpanLeaf)test.get(0)).getLeafStyle());
        return ptr;
    }

    private SetupPointer makePointer(String text){
        return SetupPointer.newPointer(text, new Document("",
            pointer -> Optional.empty()){});
    }
}

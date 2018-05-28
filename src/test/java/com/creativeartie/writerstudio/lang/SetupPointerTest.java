package com.creativeartie.writerstudio.lang;

import org.junit.jupiter.api.*;

import java.util.*;
import java.util.Optional;
import java.util.function.*;

import com.google.common.base.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Setup Pointer Tester")
public class SetupPointerTest{

    private static CharMatcher LETTER = CharMatcher.inRange('a', 'z')
        .or(CharMatcher.inRange('A', 'Z'));
    private static CharMatcher DIGIT = CharMatcher.inRange('0', '9');

    @Nested
    @DisplayName("Pointer Trim Start With")
    class TrimStartWith{
        @Test
        @DisplayName("Empty")
        public void trimStartsWithEmpty(){
            wrong("", (p, c) -> p.trimStartsWith(c, ":"));
        }

        @Test
        @DisplayName("Space")
        public void trimStartsWithSpaces(){
            wrong("  ", (p, c) -> p.trimStartsWith(c, ":"));
        }

        @Test
        @DisplayName("Token")
        public void trimStartsWithToken(){
            found(":", (p, c) -> p.trimStartsWith(c, ":"), ":",
                StyleInfoLeaf.KEYWORD);
        }

        @Test
        @DisplayName("Space + Token")
        public void trimStartsWithSpaceToken(){
            found("  :", (p, c) -> p.trimStartsWith(c, ":"), "  :",
                StyleInfoLeaf.KEYWORD);
        }

        @Test
        @DisplayName("Space + Token + More")
        public void trimStartsWithNext(){
            SetupPointer ptr = found("  :1", (p, c) ->  p.trimStartsWith(c, ":"),
                "  :", StyleInfoLeaf.KEYWORD);
            wrong(ptr, (p, c) ->  p.trimStartsWith(c, ":"));
            found(ptr, (p, c) ->  p.trimStartsWith(c, StyleInfoLeaf.DATA, "1"),
                "1", StyleInfoLeaf.DATA);
        }

        @Test
        @DisplayName("Text + Token")
        public void trimStartsWithText(){
            wrong("abc:", (p, c) -> p.trimStartsWith(c, ":"));
        }

        @Test
        @DisplayName("Space + Text + Token")
        public void trimStartsWithSpaceText(){
            wrong("  abc", (p, c) -> p.trimStartsWith(c, ":"));
        }
    }

    @Nested
    @DisplayName("Pointer Start With")
    class StartWith{
        @Test
        @DisplayName("Token")
        public void startWithStringTrue(){
            found(":", (p, c) -> p.startsWith(c, ":"), ":", StyleInfoLeaf.KEYWORD);
        }

        @Test
        @DisplayName("Token + Text")
        public void startWithStringNext(){
            SetupPointer ptr = found(":1", (p, c) ->  p.startsWith(c, ":"), ":",
                StyleInfoLeaf.KEYWORD);
            wrong(ptr, (p, c) ->  p.startsWith(c, ":"));
            found(ptr, (p, c) ->  p.startsWith(c, StyleInfoLeaf.DATA, "1"),
                "1", StyleInfoLeaf.DATA);
        }

        @Test
        @DisplayName("Text")
        public void startWithStringFalse(){
            wrong("abc", (p, c) -> p.startsWith(c, ":"));
        }

        @Test
        @DisplayName("Space")
        public void startWithStringSpace(){
            wrong("  ", (p, c) -> p.startsWith(c, ":"));
        }

        @Test
        @DisplayName("Empty")
        public void startWithStringEmpty(){
            wrong("", (p, c) -> p.startsWith(c, ":"));
        }
    }

    @Nested
    @DisplayName("Pointer Match")
    class Match{
        @Test
        @DisplayName("Token")
        public void matchSimple(){
            found("abcd", (p, c) -> p.matches(c, LETTER), "abcd",
                StyleInfoLeaf.DATA);
        }

        @Test
        @DisplayName("Token + Text")
        public void matchesExtra(){
            SetupPointer ptr = found("abcd1",
                (p, c) -> p.matches(c, LETTER), "abcd", StyleInfoLeaf.DATA);
            wrong(ptr, (p, c) -> p.matches(c, LETTER));
            found(ptr, (p, c) -> p.matches(c, DIGIT), "1",
                StyleInfoLeaf.DATA);
        }

        @Test
        @DisplayName("Empty")
        public void matchesEmpty(){
            wrong("", (p, c) -> p.matches(c, LETTER));
        }
    }

    @Nested
    @DisplayName("Pointer Get To")
    class GetTo{
        @Test
        @DisplayName("Text")
        public void getToAll(){
            found("abc", (p, c) -> p.getTo(c, StyleInfoLeaf.TEXT, ":", ";"), "abc",
                StyleInfoLeaf.TEXT);
        }

        @Test
        @DisplayName("Text + Token[0]")
        public void getToPart1(){
            found("abc:", (p, c) -> p.getTo(c, ":", ";"), "abc",
                StyleInfoLeaf.KEYWORD);
        }

        @Test
        @DisplayName("Text + Token[1]")
        public void getToPart2(){
            found("abc;", (p, c) -> p.getTo(c, ":", ";"), "abc",
                StyleInfoLeaf.KEYWORD);
        }

        @Test
        @DisplayName("Text + Token")
        public void getToExtra(){
            SetupPointer ptr = found("abc:", (p, c) -> p.getTo(c, ":"), "abc",
                StyleInfoLeaf.KEYWORD);
            wrong(ptr, (p, c) -> p.getTo(c, ":", ";"));
            found(ptr, (p, c) -> p.getTo(c, ";"), ":", StyleInfoLeaf.KEYWORD);
            wrong(ptr, (p, c) -> p.getTo(c, ":", ";"));
        }

        @Test
        @DisplayName("Empty")
        public void getToEmpty(){
            wrong("", (p, c) -> p.getTo(c, ":"));
        }
    }

    @Nested
    @DisplayName("Pointer Next Chars")
    class NextChars{
        @Test
        @DisplayName("All")
        public void nextCharsAlll(){
            found("abcde", (p, c) -> p.nextChars(c, 5), "abcde", StyleInfoLeaf.KEYWORD);
        }

        @Test
        @DisplayName("3 tokens")
        public void nextCharsSplit(){
            SetupPointer ptr = found("abcdef", (p, c) -> p.nextChars(c, 2), "ab",
                StyleInfoLeaf.KEYWORD);
            found(ptr, (p, c) -> p.nextChars(c, 2), "cd", StyleInfoLeaf.KEYWORD);
            found(ptr, (p, c) -> p.nextChars(c, 2), "ef", StyleInfoLeaf.KEYWORD);
            wrong(ptr, (p, c) -> p.nextChars(c, 2));
        }

        @Test
        @DisplayName("Empty")
        public void nextCharsEmpty(){
            wrong("", (p, c) -> p.nextChars(c, 5));
        }

        @Test
        @DisplayName("Under length")
        public void nextCharsTooShort(){
            wrong("abc", (p, c) -> p.nextChars(c, 5));
        }
    }

    @Nested
    @DisplayName("Pointer Roll Back")
    class RollBack{
        @Test
        @DisplayName("Earier to Beginning")
        public void rollBackBegin(){
            SetupPointer ptr = makePointer("abc*po*av");
            ptr.mark();
            found(ptr, (p, c) -> p.getTo(c, "*"), "abc", StyleInfoLeaf.KEYWORD);
            ptr.rollBack();
            found(ptr, (p, c) -> p.getTo(c, "*"), "abc", StyleInfoLeaf.KEYWORD);
        }

        @Test
        @DisplayName("Later to Beginging")
        public void rollBackBeginLate(){
            SetupPointer ptr = makePointer("abc*po*av");
            ptr.mark();
            found(ptr, (p, c) -> p.getTo(c, "*"), "abc", StyleInfoLeaf.KEYWORD);
            found(ptr, (p, c) -> p.startsWith(c, "*"), "*", StyleInfoLeaf.KEYWORD);
            ptr.rollBack();
            found(ptr, (p, c) -> p.getTo(c, "*"), "abc", StyleInfoLeaf.KEYWORD);
        }

        @Test
        @DisplayName("End to Beginning")
        public void rollBackBeginEnd(){
            SetupPointer ptr = makePointer("abc*po");
            ptr.mark();
            found(ptr, (p, c) -> p.getTo(c, "*"), "abc", StyleInfoLeaf.KEYWORD);
            found(ptr, (p, c) -> p.startsWith(c, "*"), "*", StyleInfoLeaf.KEYWORD);
            found(ptr, (p, c) -> p.getTo(c, "*"), "po", StyleInfoLeaf.KEYWORD);
            ptr.rollBack();
            found(ptr, (p, c) -> p.getTo(c, "*"), "abc", StyleInfoLeaf.KEYWORD);
        }

        @Test
        @DisplayName("Middle to Middle")
        public void rollBackMiddle(){
            SetupPointer ptr = makePointer("abc*po*av");
            found(ptr, (p, c) -> p.getTo(c, "*"), "abc", StyleInfoLeaf.KEYWORD);
            found(ptr, (p, c) -> p.startsWith(c, "*"), "*", StyleInfoLeaf.KEYWORD);
            ptr.mark();
            found(ptr, (p, c) -> p.getTo(c, "*"), "po", StyleInfoLeaf.KEYWORD);
            ptr.rollBack();
            found(ptr, (p, c) -> p.getTo(c, "*"), "po", StyleInfoLeaf.KEYWORD);
        }
    }

    /// Does not create new {@link SpanBranch}, from first
    private SetupPointer wrong(String text,
            BiPredicate<SetupPointer, ArrayList<Span>> tester){
        return wrong(makePointer(text), tester);
    }

    /// Does not create new {@link SpanBranch}, from middle
    private SetupPointer wrong(SetupPointer ptr,
             BiPredicate<SetupPointer, ArrayList<Span>> tester){
        ArrayList<Span> test = new ArrayList<>();
        assertFalse(tester.test(ptr, test), "main");
        assertEquals(test.size(), 0, "size");
        return ptr;
    }

    /// Create new {@link SpanBranch}, from first
    private SetupPointer found(String text, BiPredicate<SetupPointer,
            ArrayList<Span>> tester, String raw, StyleInfoLeaf style){
        return found(makePointer(text), tester, raw, style);
    }

    /// Create new {@link SpanBranch}, from middle
    private SetupPointer found(SetupPointer ptr, BiPredicate<SetupPointer,
            ArrayList<Span>> tester, String raw, StyleInfoLeaf style){
        ArrayList<Span> test = new ArrayList<>();

        assertTrue(tester.test(ptr, test), "main");
        assertEquals(1, test.size(), "size");
        assertEquals(raw, test.get(0).getRaw(), "raw");
        assertEquals(style, ((SpanLeaf)test.get(0)).getLeafStyle(), "style");
        return ptr;
    }

    /// Create a pointer
    private SetupPointer makePointer(String text){
        return SetupPointer.newPointer(text, new Document("",
            pointer -> Optional.empty()){});
    }
}

package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.io.*;
import java.util.*;
import java.util.function.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.BranchFormatTest.*;


@RunWith(Parameterized.class)
public class SupplementLinkDebug extends IDParamTest{

    @Test
    public void linkExternalTest(){
        String text = "Google";
        String id = "id";
        String target = "!@" + id + ":www.google.com";
        String pointer = "<@" + id + "|" + text + ">";
        WritingText doc = buildDoc(target, pointer);

        FormatSpanLink test = findSpan(doc, FormatSpanLink.class);
        linkTest(findSpan(doc, FormatSpanLink.class),
            findSpan(doc, LinedSpanPointLink.class), text);
    }

    @Test
    public void linkInternalTest(){
        String text = "Ch 1";
        String id = "a";
        String target = "=@" + id + ": Chapter 1: Rise and fall of tests";
        String pointer = "<@" + id + "|" + text + ">";
        WritingText doc = buildDoc(target, pointer);

        FormatSpanLink test = findSpan(doc, FormatSpanLink.class);
        linkTest(findSpan(doc, FormatSpanLink.class),
            findSpan(doc, LinedSpanLevelSection.class), text);
    }

    private void linkTest(FormatSpanLink test, SpanBranch expect, String text){
        switch(expected){
        case NOT_FOUND:
            assertEquals(text, test.getText());
            assertFalse(test.getPathSpan().isPresent());
            break;
        case MULTIPLE:
            assertEquals(text, test.getText());
            assertFalse(test.getPathSpan().isPresent());
            break;
        case READY:
            assertEquals(text, test.getText());
            assertTrue(test.getPathSpan().isPresent());
            assertSame(expect, test.getPathSpan().get());
            break;
        default:
            assert false: "Untesed status: " + expected;
        }
    }

    @Test
    public void footnoteTest(){
        noteLineTest("^");
    }

    @Test
    public void endnoteTest(){
        noteLineTest("*");
    }

    private void noteLineTest(String token){
        String id = "id";
        String target = "!" + token + id + ":_Random text_";
        String pointer = "{" + token + id + "}";
        WritingText doc = buildDoc(target, pointer);
        noteTest(findSpan(doc, FormatSpanDirectory.class),
            findSpan(doc, LinedSpanPointNote.class));
    }

    @Test
    public void noteCardTest(){
        String id = "id";
        String target = "!%@" + id + ":_Random text_";
        String pointer = "{@" + id + "}";
        WritingText doc = buildDoc(target, pointer);
        noteTest(findSpan(doc, FormatSpanDirectory.class),
            findSpan(doc, NoteCardSpan.class));
    }

    private void noteTest(FormatSpanDirectory test, SpanBranch expect){
        switch(expected){
        case NOT_FOUND:
            assertFalse(test.getTarget().isPresent());
            break;
        case MULTIPLE:
            assertFalse(test.getTarget().isPresent());
            break;
        case READY:
            assertTrue(test.getTarget().isPresent());
            assertSame(expect, test.getTarget().get());
            break;
        default:
            assert false: "Untesed status: " + expected;
        }
    }

    private WritingText buildDoc(String target, String pointer){
        StringBuilder raw = new StringBuilder();
        boolean hasRef = false;
        for (IDParamTest.States state: input){
            switch(state){
            case ID:
                raw.append(target + "\n");
                break;
            default:
                assert state == IDParamTest.States.REF;
                raw.append(pointer + "\n");
                hasRef = true;
            }
        }
        Assume.assumeTrue(hasRef);
        return new WritingText(raw.toString());
    }

    private <T> T findSpan(SpanNode<?> span, Class<T> cast){
        if (cast.isInstance(span)){
            return cast.cast(span);
        }
        for (Span child: span){
            if (child instanceof SpanNode){
                T found = findSpan((SpanNode<?>)child, cast);
                if (found != null){
                    return found;
                }
            }
            // if (child instanceof SpanLeaf) { continue; }
        }
        return null;
    }
}

package com.creativeartie.writer.lang.markup;

import org.junit.jupiter.api.*;

import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import java.time.*;

import com.google.common.base.*;

import com.creativeartie.writer.lang.*;

import static org.junit.jupiter.api.Assertions.*;

public class WritingTest {

    @Test
    @Tag("heavy")
    @Disabled("This test takes too long to run.")
    public void reallyLarge() throws Exception{
        String raw = new String(Files.readAllBytes(Paths.get(
            "build/resources/test/pdf-stress.txt")),
            StandardCharsets.UTF_8);
        System.out.println(raw.length());
        assertTimeout(Duration.ofSeconds(60), () -> new WritingText(raw),
            "Over 60 seconds");
    }

    private String[] spans = {
        "=", "@", "Chapter 1", ":", " Story of nobody", "\n",
        "#", "An outline thing.", "\n",
        "See me fly", "{^", "random footnote", "}", "\n",
        "!^", "footnote", ":", "text for the foot note", "\n",

        "!%", "Add some random note", "\n",

        "Paragraph ", "*", "continue", "*", " from above", "\n",
        ">", "Random quote", "\n",

        "=", "@", "Chapter 2", ":", " More ", "#STUB", " abc", "\\", "d", "ee"
    };

    @Test
    public void getCount(){
        String text = Joiner.on("").join(spans);
        WritingText doc = new WritingText(text);
        assertEquals(16, doc.getPublishTotal(), "publish count");
        assertEquals(4, doc.getNoteTotal(), "note count");
    }

    @Test
    public void getLeaves(){
        String text = Joiner.on("").join(spans);
        Document doc = new WritingText(text);
        int start = 0;
        int i = 0;
        List<SpanLeaf> leaves = doc.getLeaves();
        assertEquals(spans.length, leaves.size());
        for(SpanLeaf span: leaves){
            int end = start + spans[i].length();
            assertEquals(start, span.getStart());
            assertEquals(end, span.getEnd());
            assertEquals(spans[i], span.getRaw());
            start = end;
            i++;
        }
    }
}


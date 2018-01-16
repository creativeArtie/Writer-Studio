package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.Duration;
import com.creativeartie.jwriter.lang.*;

import java.util.List;
import com.google.common.base.Joiner;

@RunWith(JUnit4.class)
public class ManuscriptDebug {

    @Test
    @Ignore("Because it doubles the test time for the package.")
    public void reallyLarge() throws Exception{
        String raw = new String(Files.readAllBytes(Paths.get("data/stressTest.txt")),
            StandardCharsets.UTF_8);
        LocalDateTime from = LocalDateTime.now();
        new ManuscriptDocument(raw);
        System.out.println(Duration.between(from, LocalDateTime.now()));
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
        ManuscriptDocument doc = new ManuscriptDocument(text);
        assertEquals(SpanBranchAssert.getError("publish count", doc), 16,
            doc.getPublishTotal());
        assertEquals(SpanBranchAssert.getError("note count", doc), 4,
            doc.getNoteTotal());
    }

    @Test
    public void getLeaves(){
        String text = Joiner.on("").join(spans);
        Document doc = new ManuscriptDocument(text);
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

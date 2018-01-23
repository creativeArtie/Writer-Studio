package com.creativeartie.jwriter.lang;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import com.creativeartie.jwriter.lang.markup.WritingText;

@RunWith(Parameterized.class)
public class DocumentAccessDebug{

    /// Print everything or not
    private static boolean verbose = true;

    private static int countColumn;
    private static int countLine;
    private static int countIt;

    private static String leafSpan(ArrayList<Object[]> data, String span,
        int ... indexes
    ){
        boolean isFirst = true;
        for(int i = 0; i < span.length(); i++){
            int store = isFirst? countIt: -1;
            data.add(new Object[]{data.size() - 1, false, indexes, countColumn,
                countLine, store});
            if (span.charAt(i) == '\n'){
                countLine++;
                countColumn = 0;
            } else {
                countColumn++;
            }
        }
        countIt++;
        return span;
    }

    @Parameters
    public static Collection<Object[]> data() {
        ArrayList<Object[]> data = new ArrayList<>();
        StringBuilder docRaw = new StringBuilder();
        countColumn = 0;
        countLine = 1;

        data.add(new Object[]{-1, false, new int[0], 0 , 1, -1});

        docRaw.append(leafSpan(data, "=",                0, 0, 0));
        docRaw.append(leafSpan(data, "@",                0, 0, 1));
        docRaw.append(leafSpan(data, "Chapter 1",        0, 0, 2, 0, 0));
        docRaw.append(leafSpan(data, ":",                0, 0, 3));
        docRaw.append(leafSpan(data, "\\",               0, 0, 4, 0, 0, 0));
        docRaw.append(leafSpan(data, "\n",               0, 0, 4, 0, 0, 1));
        docRaw.append(leafSpan(data, " Story of nobody", 0, 0, 4, 0, 1));
        docRaw.append(leafSpan(data, "\n",               0, 0, 5));

        docRaw.append(leafSpan(data, "!%",                   0, 1, 0, 0));
        docRaw.append(leafSpan(data, "Add some random note", 0, 1, 0, 1, 0, 0));
        docRaw.append(leafSpan(data, "\n",                   0, 1, 0, 2));

        docRaw.append(leafSpan(data, "!>",            0, 1, 1, 0));
        docRaw.append(leafSpan(data, "in-text",       0, 1, 1, 1, 0));
        docRaw.append(leafSpan(data, ":",             0, 1, 1, 2));
        docRaw.append(leafSpan(data, "Some sources.", 0, 1, 1, 3, 0, 0));
        docRaw.append(leafSpan(data, "\n",            0, 1, 1, 4));

        docRaw.append(leafSpan(data, "Paragraph ",  0, 2, 0, 0, 0));
        docRaw.append(leafSpan(data, "*",           0, 2, 0, 1));
        docRaw.append(leafSpan(data, "continue",    0, 2, 0, 2, 0));
        docRaw.append(leafSpan(data, "*",           0, 2, 0, 3));
        docRaw.append(leafSpan(data, " from above", 0, 2, 0, 4, 0));
        docRaw.append(leafSpan(data, "\n",          0, 2, 1));

        docRaw.append(leafSpan(data, "***\n", 0, 3, 0));

        docRaw.append(leafSpan(data, "Text after the break.", 0, 4, 0, 0, 0));
        docRaw.append(leafSpan(data, "\n",                    0, 4, 1));


        docRaw.append(leafSpan(data, "=",     1, 0, 0));
        docRaw.append(leafSpan(data, "@",     1, 0, 1));
        docRaw.append(leafSpan(data, "Ch 2",  1, 0, 2, 0, 0));
        docRaw.append(leafSpan(data, ":",     1, 0, 3));
        docRaw.append(leafSpan(data, " More", 1, 0, 4, 0, 0));
        docRaw.append(leafSpan(data, "#STUB", 1, 0, 5, 0));
        docRaw.append(leafSpan(data, " abc",  1, 0, 5, 1, 0));
        docRaw.append(leafSpan(data, "\\",    1, 0, 5, 1, 1, 0));
        docRaw.append(leafSpan(data, "d",     1, 0, 5, 1, 1, 1));
        docRaw.append(leafSpan(data, "ee",    1, 0, 5, 1, 2));
        docRaw.append(leafSpan(data, "\n",    1, 0, 6));

        docRaw.append(leafSpan(data, "#",                 1, 1, 0));
        docRaw.append(leafSpan(data, "An outline thing.", 1, 1, 1, 0, 0));
        docRaw.append(leafSpan(data, "\n",                1, 1, 2));

        docRaw.append(leafSpan(data, "#",          1, 2, 0));
        docRaw.append(leafSpan(data, "See me fly", 1, 2, 1, 0, 0));
        docRaw.append(leafSpan(data, "{^",         1, 2, 1, 1, 0));
        docRaw.append(leafSpan(data, "random ",    1, 2, 1, 1, 1, 0, 0));
        docRaw.append(leafSpan(data, "}",          1, 2, 1, 1, 2));
        docRaw.append(leafSpan(data, "\n",         1, 2, 2));

        docRaw.append(leafSpan(data, "!^",                     1, 3, 0));
        docRaw.append(leafSpan(data, "footnote",               1, 3, 1, 0, 0));
        docRaw.append(leafSpan(data, ":",                      1, 3, 2));
        docRaw.append(leafSpan(data, "text for the foot note", 1, 3, 3, 0, 0));
        docRaw.append(leafSpan(data, "\n",                     1, 3, 4));

        data.add(new Object[]{data.size() - 1, false, new int[]{1, 3, 4}, 0,
                countLine, countIt++});

        data.add(new Object[]{data.size() - 1, false, new int[0], 0, 1, countIt++});

        data.add(new Object[]{data.size() - 1, true, new int[0], 0, 1, countIt++});

        docText = docRaw.toString();
        return data;
    }

    private static String docText;
    private static WritingText filledDoc;
    private static WritingText emptyDoc;

    @Parameter
    public int ptr;

    @Parameter(value = 1)
    public boolean useEmpty;

    @Parameter(value = 2)
    public int[] indexes;

    @Parameter(value = 3)
    public int column;

    @Parameter(value = 4)
    public int line;

    @Parameter(value = 5)
    public int to;


    @BeforeClass
    public static void beforeClass(){
        filledDoc = new WritingText(docText);
        emptyDoc = new WritingText();
    }

    @Test
    public void getColumn(){
        if (useEmpty){
            assertFalse(emptyDoc.getLeaf(0).isPresent());
            assertEquals(column, emptyDoc.getColumn(0));
        }
        Assume.assumeTrue(indexes.length > 0);
        assertEquals(column, filledDoc.getColumn(ptr));
    }

    @Test
    public void getLine(){
        if (useEmpty){
            assertFalse(emptyDoc.getLeaf(0).isPresent());
            assertEquals(line, emptyDoc.getLine(0));
        }
        Assume.assumeTrue(indexes.length > 0);
        assertEquals(line, filledDoc.getLine(ptr));
    }

    private Span findLeaf(){
        Span span = filledDoc;
        for(int index: indexes){
            assertTrue("Span is not a SpanNode: " + span, span instanceof
                SpanNode);
            SpanNode parent = (SpanNode) span;
            assertTrue("Index (" + index + ") is not in range(" + parent.size()
                +"):" + parent, index < parent.size());
            span = ((SpanNode)span).get(index);
        }
        return span;
    }

    @Test
    public void iterateLeaves(){
        Assume.assumeTrue(countIt > 0);
        if (useEmpty || indexes.length == 0){
            try {
                (useEmpty? emptyDoc: filledDoc).getLeaves().get(to);
            } catch (IndexOutOfBoundsException ex){
                return;
            }
            fail("No IndexOutOfBoundsException thrown.");
        }
        Span span = findLeaf();
        assertSame(span, filledDoc.getLeaves().get(to));
    }

    @Test
    public void getLeaf(){
        if (indexes.length == 0){
            try {
                filledDoc.getLeaf(ptr);
            } catch (IndexOutOfBoundsException ex){
                return;
            }
            fail("No IndexOutOfBoundsException thrown.");
        }
        if (useEmpty){
            Optional<SpanLeaf> found = emptyDoc.getLeaf(ptr);
            assertFalse("Leaf is found.", found.isPresent());
        }

        Optional<SpanLeaf> found = filledDoc.getLeaf(ptr);
        assertTrue("Leaf not found.", found.isPresent());
        SpanLeaf leaf = found.get();
        Span span = findLeaf();
        assertSame(span, leaf);
    }

}

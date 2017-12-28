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

import com.creativeartie.jwriter.lang.markup.ManuscriptDocument;

@RunWith(Parameterized.class)
public class DocumentAccessDebug{

    /// Print everything or not
    private static boolean verbose = true;

    private static String leafSpan(ArrayList<Object[]> data, String span,
        int ... indexes
    ){
        for(int i = 0; i < span.length(); i++){
            data.add(new Object[]{data.size() - 1, indexes});
        }

        return span;
    }

    @Parameters
    public static Collection<Object[]> data() {
        ArrayList<Object[]> data = new ArrayList<>();
        StringBuilder docRaw = new StringBuilder();

        data.add(new Object[]{-1, new int[0]});

        docRaw.append(leafSpan(data, "=",                0, 0, 0));
        docRaw.append(leafSpan(data, "@",                0, 0, 1));
        docRaw.append(leafSpan(data, "Chapter 1",        0, 0, 2, 0, 0));
        docRaw.append(leafSpan(data, ":",                0, 0, 3));
        docRaw.append(leafSpan(data, " Story of nobody", 0, 0, 4, 0, 0));
        docRaw.append(leafSpan(data, "\n",               0, 0, 5));


        docRaw.append(leafSpan(data, "!%",                   1, 0, 0));
        docRaw.append(leafSpan(data, "Add some random note", 1, 0, 1, 0, 0));
        docRaw.append(leafSpan(data, "\n",                   1, 0, 2));

        docRaw.append(leafSpan(data, "!>",            1, 1, 0));
        docRaw.append(leafSpan(data, "in-text",       1, 1, 1, 0));
        docRaw.append(leafSpan(data, ":",             1, 1, 2));
        docRaw.append(leafSpan(data, "Some sources.", 1, 1, 3, 0, 0));
        docRaw.append(leafSpan(data, "\n",            1, 1, 4));


        docRaw.append(leafSpan(data, "Paragraph ",  2, 0, 0, 0, 0));
        docRaw.append(leafSpan(data, "*",           2, 0, 0, 1));
        docRaw.append(leafSpan(data, "continue",    2, 0, 0, 2, 0));
        docRaw.append(leafSpan(data, "*",           2, 0, 0, 3));
        docRaw.append(leafSpan(data, " from above", 2, 0, 0, 4, 0));
        docRaw.append(leafSpan(data, "\n",          2, 0, 1));

        docRaw.append(leafSpan(data, "***\n", 2, 1, 0));

        docRaw.append(leafSpan(data, "Text after the break.", 2, 2, 0, 0, 0));
        docRaw.append(leafSpan(data, "\n",                    2, 2, 1));


        docRaw.append(leafSpan(data, "=",     3, 0, 0));
        docRaw.append(leafSpan(data, "@",     3, 0, 1));
        docRaw.append(leafSpan(data, "Ch 2",  3, 0, 2, 0, 0));
        docRaw.append(leafSpan(data, ":",     3, 0, 3));
        docRaw.append(leafSpan(data, " More", 3, 0, 4, 0, 0));
        docRaw.append(leafSpan(data, "#STUB", 3, 0, 5, 0));
        docRaw.append(leafSpan(data, " abc",  3, 0, 5, 1, 0));
        docRaw.append(leafSpan(data, "\\",    3, 0, 5, 1, 1, 0));
        docRaw.append(leafSpan(data, "d",     3, 0, 5, 1, 1, 1));
        docRaw.append(leafSpan(data, "ee",    3, 0, 5, 1, 2));
        docRaw.append(leafSpan(data, "\n",    3, 0, 6));

        docRaw.append(leafSpan(data, "#",                 3, 1, 0));
        docRaw.append(leafSpan(data, "An outline thing.", 3, 1, 1, 0, 0));
        docRaw.append(leafSpan(data, "\n",                3, 1, 2));

        docRaw.append(leafSpan(data, "#",          3, 2, 0));
        docRaw.append(leafSpan(data, "See me fly", 3, 2, 1, 0, 0));
        docRaw.append(leafSpan(data, "{^",         3, 2, 1, 1, 0));
        docRaw.append(leafSpan(data, "random ",    3, 2, 1, 1, 1, 0, 0));
        docRaw.append(leafSpan(data, "}",          3, 2, 1, 1, 2));
        docRaw.append(leafSpan(data, "\n",         3, 2, 2));

        docRaw.append(leafSpan(data, "!^",                     3, 3, 0));
        docRaw.append(leafSpan(data, "footnote",               3, 3, 1, 0, 0));
        docRaw.append(leafSpan(data, ":",                      3, 3, 2));
        docRaw.append(leafSpan(data, "text for the foot note", 3, 3, 3, 0, 0));
        docRaw.append(leafSpan(data, "\n",                     3, 3, 4));

        data.add(new Object[]{data.size() - 1, new int[0]});

        data.add(new Object[]{data.size() - 1, new int[0]});

        docText = docRaw.toString();
        return data;
    }

    private static String docText;
    private static ManuscriptDocument doc;

    @Parameter
    public int ptr;

    @Parameter(value = 1)
    public int[] indexes;

    @BeforeClass
    public static void beforeClass(){
        doc = new ManuscriptDocument(docText);
    }

    @Test
    public void getLeaf(){
        if (indexes.length == 0){
            try {
                doc.getLeaf(ptr);
            } catch (IndexOutOfBoundsException ex){
                return;
            }
            fail("No IndexOutOfBoundsException thrown.");
        }

        SpanLeaf leaf = doc.getLeaf(ptr);
        Span span = doc;
        for(int index: indexes){
            assertTrue("Span is not a SpanNode: " + span, span instanceof
                SpanNode);
            SpanNode parent = (SpanNode) span;
            assertTrue("Index (" + index + ") is not in range(" + parent.size()
                +")", index < parent.size());
            span = ((SpanNode)span).get(index);
        }
        assertSame(span, leaf);
    }

}

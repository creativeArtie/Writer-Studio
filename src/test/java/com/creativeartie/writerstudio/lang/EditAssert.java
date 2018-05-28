package com.creativeartie.writerstudio.lang;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.*;

import java.util.*;

public class EditAssert{

    private boolean showEdits;

    private SpanNode<?> expectedEdited;
    private ArrayList<SpanNode<?>> actualEdited;

    private ArrayList<SpanNode<?>> expectedParents;
    private ArrayList<SpanNode<?>> actualParents;

    private ArrayList<SpanNode<?>> expectedSpans;
    private ArrayList<SpanNode<?>> actualSpans;

    private ArrayList<SpanNode<?>> expectedRemoves;
    private ArrayList<SpanNode<?>> actualRemoves;

    EditAssert(Document doc, SpanNode<?> edited){
        this(doc, edited, false);
    }

    EditAssert(Document doc, SpanNode<?> edited, boolean show){
        showEdits = show;
        expectedEdited = edited;
        actualEdited = new ArrayList<>();

        expectedParents = new ArrayList<>();
        SpanNode<?> ptr = edited;
        while (ptr instanceof SpanBranch){
            ptr = ptr.getParent();
            expectedParents.add(ptr);
        }
        actualParents = new ArrayList<>();

        assert ptr instanceof Document;
        expectedSpans = addAll(ptr);
        expectedSpans.add(ptr);
        actualSpans = new ArrayList<>();

        expectedRemoves = addAll(edited);
        actualRemoves = new ArrayList<>();

        addListeners(doc);
    }

    private ArrayList<SpanNode<?>> addAll(SpanNode<?> from){
        ArrayList<SpanNode<?>> ans = new ArrayList<>();
        for (Span span: from){
            if (span instanceof SpanNode){
                ans.add((SpanNode<?>)span);
                ans.addAll(addAll((SpanBranch) span));
            }
        }
        return ans;
    }

    private void addListeners(SpanNode<?> span){
        span.addSpanEdited(this::edited);
        span.addChildEdited(this::childed);
        span.addDocEdited(this::doc);
        span.addSpanRemoved(this::removed);
        for (Span child: span){
            if (child instanceof SpanNode){
                addListeners((SpanNode<?>) child);
            }
        }
    }

    private void edited(SpanNode<?> span){
        actualEdited.add(span);
    }

    private void childed(SpanNode<?> span){
        actualParents.add(span);
    }

    private void doc(SpanNode<?> span){
        actualSpans.add(span);
    }

    private void removed(SpanNode<?> span){
        actualRemoves.add(span);
    }

    void testRest(){
        ArrayList<Excu
        if (showEdits) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(expectedEdited.getDocument());
            System.out.println(expectedEdited);

            System.out.println("Edits-----------------");
            System.out.println(expectedEdited);
            System.out.println(actualEdited);
        }
        assertAll("edited"
            () -> assertEquals(actualEdited.size(), 1,
                () -> "esize: " + actualEdited),
            () -> assertSame(expectedEdited, actualEdited.get(0),
                () -> "span: " + actualEdited.get(0));
        );

        if (showEdits) {
            System.out.println("Parents-----------------");
            System.out.println(expectedParents);
            System.out.println(actualParents);
        }
        search("parent", expectedParents, actualParents);

        if (showEdits) {
            System.out.println("Spans-----------------");
            System.out.println(expectedSpans);
            System.out.println(actualSpans);
        }
        search("spans", expectedSpans, actualSpans);


        if (showEdits) {
            System.out.println("Removes-----------------");
            System.out.println(expectedRemoves);
            System.out.println(actualRemoves);
        }
        search("remove", expectedRemoves, actualRemoves);


    }

    private void search(String name, List<SpanNode<?>> expect,
            List<SpanNode<?>> actual){
        if (showEdits) System.out.println("remove......");
        for (SpanNode<?> got: actual){
            assertTrue(expect.contains(got),
                () -> "unexpected " + name + ": "  + got);
            expect.remove(got);
            if (showEdits) System.out.println(got);
        }
        if (showEdits) System.out.println("ends......");
        if (showEdits) System.out.println(expect);
        assertTrue(expect.isEmpty(),
            () -> name + " not called: " + expect);
    }
}

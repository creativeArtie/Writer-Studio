package com.creativeartie.writerstudio.lang;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.*;

import java.util.*;

/// Tests for listeners
public class EditAssert{

    /// %Part 1: Setup tests ###################################################

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

        SpanNode<?> ptr = edited; /// For parent and span list

        expectedParents = new ArrayList<>();
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

    /// Add the children and grade children to the list
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

    /// Adds the listeners
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

    /// %Part 2: Listener functions ############################################

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

    /// %Part 3: Assertion Calls ###############################################

    void testRest(){
        if (showEdits) {
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(expectedEdited.getDocument());
            System.out.println(expectedEdited);

            System.out.println("Edits-----------------");
            System.out.println(expectedEdited);
            System.out.println(actualEdited);

            System.out.println("Parents-----------------");
            System.out.println(expectedParents);
            System.out.println(actualParents);

            System.out.println("Spans-----------------");
            System.out.println(expectedSpans);
            System.out.println(actualSpans);

            System.out.println("Removes-----------------");
            System.out.println(expectedRemoves);
            System.out.println(actualRemoves);
        }

        ArrayList<Executable> tests = new ArrayList<>();

        /// Tests
        tests.add(() -> assertAll("edited",
            () -> assertEquals(actualEdited.size(), 1,
                () -> "esize: " + actualEdited),
            () -> assertSame(expectedEdited, actualEdited.get(0),
                () -> "span: " + actualEdited.get(0))
        ));

        tests.add(search("parent", expectedParents, actualParents));

        tests.add(search("spans", expectedSpans, actualSpans));

        tests.add(search("remove", expectedRemoves, actualRemoves));

        assertAll("edit listeners", tests);

    }

    private Executable search(String name, List<SpanNode<?>> expect,
            List<SpanNode<?>> actual){
        ArrayList<Executable> list = new ArrayList<>();
        for (SpanNode<?> got: actual){
            list.add(() -> {
                assertTrue(expect.contains(got),
                    () -> "Unexpected: "  + got);
                expect.remove(got);
            });
        }

        /// since the one called are removed from `expect`:
        list.add(() ->  assertTrue(expect.isEmpty(), () -> "Not called: " + expect));
        return () -> assertAll(name, list);
    }
}

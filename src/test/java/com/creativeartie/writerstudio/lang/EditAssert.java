package com.creativeartie.writerstudio.lang;

import org.junit.jupiter.api.function.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/// Tests for listeners
public class EditAssert{

    /// %Part 1: Setup tests ###################################################

    private final boolean showEdits;

    private final Document useDoc;

    private final ArrayList<SpanNode<?>> expectedEdited;
    private final ArrayList<SpanNode<?>> actualEdited;

    private final ArrayList<SpanNode<?>> expectedParents;
    private final ArrayList<SpanNode<?>> actualParents;

    private final ArrayList<SpanNode<?>> expectedSpans;
    private final ArrayList<SpanNode<?>> actualSpans;

    private final ArrayList<SpanNode<?>> expectedRemoves;
    private final ArrayList<SpanNode<?>> actualRemoves;

    EditAssert(boolean show, Document doc, SpanNode<?> ... edits){
        showEdits = show;
        useDoc = doc;

        expectedEdited = new ArrayList<>();
        actualEdited = new ArrayList<>();

        expectedParents = new ArrayList<>();
        actualParents = new ArrayList<>();

        expectedSpans = new ArrayList<>();
        actualSpans = new ArrayList<>();

        expectedRemoves = new ArrayList<>();
        actualRemoves = new ArrayList<>();

        for (SpanNode<?> edit: edits){
            addEdited(edit);
        }
    }

    void addEdited(SpanNode<?> edited){
        expectedEdited.add(edited);
        SpanNode<?> ptr = edited; /// For parent and span list

        while (ptr instanceof SpanBranch){
            ptr = ptr.getParent();
            expectedParents.add(ptr);
        }

        expectedSpans.addAll(addAll(useDoc));
        expectedSpans.add(useDoc);

        expectedRemoves.addAll(addAll(edited));

        addListeners(useDoc);
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

    private void printSpans(List<SpanNode<?>> expected, List<SpanNode<?>> actual){
            int i = 1;
            for (Span span : expected){
                System.out.println("expect[" + i + "]: " + span);
                i++;
            }
            i = 1;
            for (Span span : actual){
                System.out.println("actual[" + i + "]: " + span);
                i++;
            }
    }

    void testRest(){
        if (showEdits) {
            System.out.println("##############################################");
            System.out.println("Document======================================");
            System.out.println(useDoc);

            System.out.println("Edits----------------------------------------");
            printSpans(expectedEdited, actualEdited);

            System.out.println("Parents-----------------");
            printSpans(expectedParents, actualParents);

            System.out.println("Spans-----------------");
            printSpans(expectedSpans, actualSpans);

            System.out.println("Removes-----------------");
            printSpans(expectedRemoves, actualRemoves);
        }

        ArrayList<Executable> tests = new ArrayList<>();

        /// Tests
        tests.add(search("edited", expectedEdited, actualEdited));

        tests.add(search("parent", expectedParents, actualParents));

        tests.add(search("spans", expectedSpans, actualSpans));

        tests.add(search("remove", expectedRemoves, actualRemoves));

        assertAll("edit listeners", tests);

    }

    private Executable search(String name, List<SpanNode<?>> expect,
            List<SpanNode<?>> actual){
        ArrayList<Executable> list = new ArrayList<>();
        ArrayList<SpanNode<?>> called = new ArrayList<>();
        for (SpanNode<?> got: actual){
            list.add(() -> {
                assertTrue(expect.contains(got) || called.contains(got),
                    () -> "Unexpected: "  + got);
                expect.remove(got);
                called.add(got);
            });
        }

        /// since the one called are removed from `expect`:
        list.add(() ->  assertTrue(expect.isEmpty(), () -> "Not called: " + expect));
        return () -> assertAll(name, list);
    }
}

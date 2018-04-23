package com.creativeartie.writerstudio.lang;

import java.util.*;

import static org.junit.Assert.*;

public class EditAssert{
    private enum Call{
        /// target is sibling to this span
        NONE,
        /// target is a child to this span
        PARENT,
        /// target is this span
        SELF,
        /// targe is a parent to this span
        CHILD;
    }

    private final Span targetSpan;
    private final Map<Span, Call> expectedCalls;
    private int totalRemoved;
    private int totalChildren;
    private int countRemoved;
    private int countParent;
    private boolean isPass;

    EditAssert(Document doc, Span edited){
        targetSpan = edited;

        expectedCalls = new TreeMap<>();
        for (SpanBranch span: doc){
            load(span, edited == span);
        }

        /// fix targetSpan = child
        expectedCalls.put(edited, Call.SELF);

        /// fix everything else = none
        Span self = edited;
        while ( ! (self instanceof Document)){
            self = self.getParent();
            expectedCalls.put(self, Call.PARENT);
            totalParent++;
        }
        expectedCalls.put(doc, Call.PARENT);

        addListeners(doc);
    }

    private void load(SpanNode<?> span, boolean child){
        /// Pretends targetSpan = child
        /// Pretends everything else = none
        expectedCalls.put(span, child? Call.CHILD: Call.NONE);
        totalRemoved++;
        for(Span found: span){
            if (found instanceof SpanBranch){
                load((SpanBranch)found, found == targetSpan? true: child);
            }
        }
    }

    private void addListeners(SpanNode<?> span){
        span.addSpanEdited(this::edited);
        span.addChildEdited(this::chiled);
        span.addSpanRemoved(this::removed);
    }

    private void edited(SpanNode<?> span){
        switch (expectedCalls.get(span)){
        case NONE:
            fail("Sibling span is being edited: " + span);
            break;
        case PARENT:
            fail("Parent span is being edited: " + span);
        case SELF:
            isPass = true;
            break;
        case CHILD:
            fail("removed span is being edited:" + span);
            break;
        }
    }

    private void childed(SpanNode<?> span){
        switch (expectedCalls.get(span)){
        case NONE:
            fail("Sibling span fire it children listeners: " + span);
            break;
        case PARENT:
            countParent++;
            break;
        case SELF:
            fail("Self span fire it children listeners: " + span);
            break;
        case CHILD:
            fail("Child span fire it children listeners: " + span);
            break;
        }
    }

    private void removed(SpanNode<?> span){
        switch (expectedCalls.get(span)){
        case NONE:
            fail("Sibling span is being removed: " + span);
            break;
        case PARENT:
            fail("Parent span is being removed: " + span);
            break;
        case SELF:
            fail("Self span is being removed: " + span);
            break;
        case CHILD:
            countRemoved++;
            break;
        }
    }

    void testRest(){
        assertEquals("Parent count", totalParent, countParent);
        assertTrue("spanEdited fired", isPass;
        assertEquals("Removed count", totalRemoved, countRemoved);
    }
}
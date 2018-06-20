package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static org.junit.jupiter.api.Assertions.*;

public class SectionEditTest {

    private class Tree{
        ArrayList<Tree> childrenSpans;

        Tree(Tree ... trees){
            childrenSpans = new ArrayList<>();
            for (Tree t: trees){
                childrenSpans.add(t);
            }
        }

        Tree(){
            childrenSpans = new ArrayList<>();
            childrenSpans.add(null);
        }

        ArrayList<Tree> getSpans(){
            return childrenSpans;
        }

        void test(WritingText span){
            ArrayList<Integer> ptr = new ArrayList<>();
            ptr.add(0);
            test(span, ptr);
        }

        void test(SpanNode<?> span, ArrayList<Integer> ptr){
            Iterator<Tree> it = childrenSpans.iterator();
            for (Span child: span){
                assertTrue(it.hasNext(),
                    () -> "Unexpected child(" + ptr + "): " + child
                );
                Tree tree = it.next();
                if (tree == null){
                    assertTrue(child instanceof LinedSpan,
                        () -> "Unexpected section(" + ptr + "): " + child
                    );
                } else {
                    assertTrue(child instanceof SectionSpan,
                        () -> "Unexpected line:(" + ptr + ") " + child
                    );
                    ptr.add(0);
                    tree.test((SpanBranch)child, ptr);
                    ptr.remove(ptr.size() - 1);
                }
                ptr.set(ptr.size() - 1, ptr.get(ptr.size() - 1));
            }
            assertFalse(it.hasNext(), "Missing children: " + ptr);
        }
    }


    @Test@Disabled
    public void noneToHeading(){
        WritingText test = new WritingText("abc");
        EditAssert listener = new EditAssert(false, test, test.get(0));
        Tree structure = new Tree(
            new Tree()
        );

        test.insert(0, "=");

        listener.testRest();
        structure.test(test);
    }

    @Test@Disabled
    public void noneToChild(){
        WritingText test = new WritingText("abc");
        EditAssert listener = new EditAssert(false, test, test.get(0));
        Tree structure = new Tree(
            new Tree(new Tree())
        );

        test.insert(0, "==");

        listener.testRest();
        structure.test(test);
    }

    @Test@Disabled
    public void noneToOutline(){
        WritingText test = new WritingText("abc");
        EditAssert listener = new EditAssert(false, test, test.get(0));
        Tree structure = new Tree(
            new Tree(new Tree())
        );

        test.insert(0, "!#");

        listener.testRest();
        structure.test(test);
    }

    @Test@Disabled
    public void outlineToNone(){
        WritingText test = new WritingText("!#abc");
        EditAssert listener = new EditAssert(false, test, test.get(0));
        Tree structure = new Tree(
            new Tree()
        );

        test.delete(0, 2);

        listener.testRest();
        structure.test(test);
    }

    @Test
    public void childToNone(){
        WritingText test = new WritingText("==abc");
        EditAssert listener = new EditAssert(false, test, test.get(0));
        Tree structure = new Tree(
            new Tree()
        );

        test.delete(0, 2);

        listener.testRest();
        structure.test(test);
    }
}

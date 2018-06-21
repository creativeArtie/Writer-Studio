package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static org.junit.jupiter.api.Assertions.*;

public class SectionEditTest {

    @Test
    public void noneToHeading(){
        setup("abc", new TreeAsserter(0,  /// Document
            new TreeAsserter(1) /// Section Lvl 1
        ), 0);

        testFile.insert(0, "=");
    }

    @Test
    public void noneToChild(){
        setup("abc",new TreeAsserter(0,  /// Document
            new TreeAsserter(0, /// Section Lvl 1
                new TreeAsserter(1) /// Section Lvl 2
            )
        ), 0);

        testFile.insert(0, "==");
    }

    @Test
    public void noneToOutline(){
        setup("abc", new TreeAsserter(0,  /// Document
            new TreeAsserter(0, /// Section Lvl 1
                new TreeAsserter(1) /// Outline Lvl 1
            )
        ), 0);

        testFile.insert(0, "!#");
    }

    @Test
    public void outlineToNone(){
        setup("!#abc", new TreeAsserter(0,  /// Document
            new TreeAsserter(1) /// Section Lvl 1
        ), 0);

        testFile.delete(0, 2);
    }

    @Test
    public void childToNone(){
        setup("==abc", new TreeAsserter(0,  /// Document
            new TreeAsserter(1) /// Section Lvl 1
        ), 0);

        testFile.delete(0, 2);
    }

    @Test
    public void addLine(){
        setup("abc", new TreeAsserter(0,  /// Document
            new TreeAsserter(2) /// Section Lvl 1
        ), 0);

        testFile.insert(2, "\nabc\\");
    }

    @Test
    public void outlineToParent(){
        setup("!##abc", new TreeAsserter(0,  /// Document
            new TreeAsserter(0, /// Section Lvl 1
                new TreeAsserter(1) /// Outline Lvl 1
            )
        ), 0, 0);

        testFile.delete(1, 2);
    }

    @Test
    public void mergeHeading1(){
        /// ...0123 456789
        setup("=abc\n=abc", new TreeAsserter(0,  /// Document
            new TreeAsserter(2) /// Section Lvl 1
        ));

        testFile.delete(5, 6);
    }

    @Test
    public void splitHeading1(){
        /// ...123 45678
        setup("123\n123", new TreeAsserter(0, /// Document
            new TreeAsserter(1), /// First section
            new TreeAsserter(1) /// Second section
        ));

        testFile.insert(4, "=");
    }

    @Test
    public void escapeHeading2(){
        /// ...123 4567890
        setup("123\n==0123", new TreeAsserter(0, /// Document
            new TreeAsserter(1) /// First section
        ), 0);

        testFile.insert(3, "\\");
    }

    @Test
    public void heading4AddHeading3(){
        /// ...000 000000 11111
        /// ...123 456789 01234
        setup("==h\n====3\nabc", new TreeAsserter(0, /// Document
            new TreeAsserter(0, /// Heading 1
                new TreeAsserter(1, /// Heading 2
                    new TreeAsserter(0, /// Section with heading 3
                        new TreeAsserter(1) /// Section 4
                    ),
                    new TreeAsserter(1) /// new Section
                )
            )
        ), 0, 0);

        testFile.insert(10, "===");

    }

    @Test
    public void outline4AddOutine3(){
        /// ...0000 0000011 11111
        /// ...1234 5678901 23456
        setup("!##h\n!####3\nabc", new TreeAsserter(0, /// Document
            new TreeAsserter(0, /// Heading 1
                new TreeAsserter(0, /// Scene 1
                    new TreeAsserter(1, /// Scene 2
                        new TreeAsserter(0, /// Scene 3 with Scene 4
                            new TreeAsserter(1) /// Scene 4
                        ),
                        new TreeAsserter(1) /// new Scene
                    )
                )
            )
        ), 0, 0, 0);

        testFile.insert(12, "!###");

    }

    @Test
    public void outlineAddHeading(){
        /// ...12345 67890
        setup("!#abc\n123", new TreeAsserter(0, /// Docu
            new TreeAsserter(0, /// Heading 1
                new TreeAsserter(1), /// Scene 1
                new TreeAsserter(1) /// Heading 2
            )
        ), 0);

        testFile.insert(6, "==");
    }

    private static class TreeAsserter{
        private int numberOfLines;
        private ArrayList<TreeAsserter> childrenBranches;

        TreeAsserter(int lines, TreeAsserter ... children){
            numberOfLines = lines;
            childrenBranches = new ArrayList<>();
            for (TreeAsserter child: children){
                childrenBranches.add(child);
            }
        }

        void test(SpanNode<? extends Span> text){
            Iterator<? extends Span> it = text.iterator();
            int ptr = 0;
            for (int i = 0; i < numberOfLines; i++){
                assertTrue(it.hasNext(),
                    () -> "No more lines(" + ptr + ") at " + text);
                Span child = it.next();
                assertTrue(child instanceof LinedSpan ||
                    child instanceof NoteCardSpan,
                    () -> "Wrong class (" + ptr + ") at " + text);
            }

            for (TreeAsserter tester: childrenBranches){
                assertTrue(it.hasNext(),
                    () -> "No more sections(" + ptr + ") at " + text);
                Span child = it.next();
                assertTrue(child instanceof SectionSpan,
                    () -> "Not section (" + ptr + ") at" + text);
                tester.test((SectionSpan) child);
            }
        }
    }

    @BeforeEach
    public void beforeEach(){
        showAll = false;
    }

    @AfterEach
    public void afterEach(){
        if (showAll) System.out.println(testFile);
        testListener.testRest();
        testStructure.test(testFile);
    }

    private WritingText testFile;
    private TreeAsserter testStructure;
    private EditAssert testListener;
    private boolean showAll;

    private void setup(String raw, TreeAsserter structure, int ... nodes){
        testFile = new WritingText(raw);
        SpanNode<?> target = testFile;
        for (int ptr: nodes){
            target = (SpanNode<?>) target.get(ptr);
        }
        testListener = new EditAssert(showAll, testFile, target);
        testStructure = structure;
    }


}

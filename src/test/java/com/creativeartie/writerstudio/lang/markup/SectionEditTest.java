package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("SectionSpan parser is tempertory simplified by not parsing")
public class SectionEditTest {

    @Test
    public void noneToHeading(){
        testListener = ListenerAssert.insert(setup("abc",
            new TreeAsserter(0,  /// Document
                new TreeAsserter(1) /// Section Lvl 1
            )
        ), 0, "=", 0);
    }

    @Test
    public void noneToChild(){
        testListener = ListenerAssert.insert(setup("abc",
            new TreeAsserter(0,  /// Document
                new TreeAsserter(0, /// Section Lvl 1
                    new TreeAsserter(1) /// Section Lvl 2
                )
            )
        ), 0, "==", 0);
    }

    @Test
    public void noneToOutline(){
        testListener = ListenerAssert.insert(setup("abc",
            new TreeAsserter(0,  /// Document
                new TreeAsserter(0, /// Section Lvl 1
                    new TreeAsserter(1) /// Outline Lvl 1
                )
            )
        ), 0, "!#", 0);
    }

    @Test
    public void outlineToNone(){
        testListener = ListenerAssert.delete(setup("!#abc",
            new TreeAsserter(0,  /// Document
                new TreeAsserter(1) /// Section Lvl 1
            )
        ), 0, 2, /*<- delete | at ->*/ 0);

    }

    @Test
    public void childToNone(){
        testListener = ListenerAssert.delete(setup("==abc",
            new TreeAsserter(0,  /// Document
                new TreeAsserter(1) /// Section Lvl 1
            )
        ), 0, 2, /*<- delete | at ->*/ 0);

    }

    @Test
    public void addLine(){
        testListener = ListenerAssert.insert(setup(
            "abc", new TreeAsserter(0,  /// Document
                new TreeAsserter(2) /// Section Lvl 1
            )
        ), 2, "\nabc\\", 0);

    }

    @Test
    public void outlineToParent(){
        testListener = ListenerAssert.delete(setup("!##abc",
            new TreeAsserter(0,  /// Document
                new TreeAsserter(0, /// Section Lvl 1
                    new TreeAsserter(1) /// Outline Lvl 1
                )
            )
        ), 1, 2, /*<- delete | at ->*/ 0, 0);
    }

    @Test
    public void mergeHeading1(){
        /// ........................................0123 456789
        testListener = ListenerAssert.delete(setup("=abc\n=abc",
            new TreeAsserter(0,  /// Document
                new TreeAsserter(2) /// Section Lvl 1
            )
        ), 5, 6);

    }

    @Test
    public void splitHeading1(){
        /// ........................................123 45678
        testListener = ListenerAssert.insert(setup("123\n123",
            new TreeAsserter(0, /// Document
                new TreeAsserter(1), /// First section
                new TreeAsserter(1) /// Second section
            )
        ), 4, "=");
    }

    @Test
    public void escapeHeading2(){
        /// .......................................123 4567890
        testListener = ListenerAssert.insert(setup("123\n==0123",
            new TreeAsserter(0, /// Document
                new TreeAsserter(1) /// First section
            )
        ), 3, "\\", 0);
    }

    @Test
    public void heading4AddHeading3(){
        /// .......................................000 000000 11111
        /// .......................................123 456789 01234
        testListener = ListenerAssert.insert(setup("==h\n====3\nabc",
            new TreeAsserter(0, /// Document
                new TreeAsserter(0, /// Heading 1
                    new TreeAsserter(1, /// Heading 2
                        new TreeAsserter(0, /// Section with heading 3
                            new TreeAsserter(1) /// Section 4
                        ),
                        new TreeAsserter(1) /// new Section
                    )
                )
            )
        ), 10, "===", 0, 0);
    }

    @Test
    public void outline4AddOutine3(){
        /// ...0000 0000011 11111
        /// ...1234 5678901 23456
        testListener = ListenerAssert.insert(setup("!##h\n!####3\nabc",
            new TreeAsserter(0, /// Document
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
            )
        ), 12, "!###", 0, 0, 0);
    }

    @Test
    public void outlineAddHeading(){
        /// .......................................012345 67890
        testListener = ListenerAssert.insert(setup("!#abc\n123",
            new TreeAsserter(0, /// Docu
                new TreeAsserter(0, /// Heading 1
                    new TreeAsserter(1), /// Scene 1
                    new TreeAsserter(1) /// Heading 2
                )
            )
        ), 6, "==", 0);
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
        ListenerAssert<?> tester = testListener.build();
        if (showAll) System.out.println(testFile);
        tester.runCommand();
        testStructure.test(testFile);
        tester.test();
    }

    private WritingText testFile;
    private TreeAsserter testStructure;
    private ListenerAssert<?>.Builder testListener;
    private boolean showAll;

    private WritingText setup(String raw, TreeAsserter structure){
        testFile = new WritingText(raw);
        SpanNode<?> target = testFile;

        testStructure = structure;
        return testFile;
    }


}

package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;
import java.io.File;

import com.creativeartie.jwriter.lang.*;

@RunWith(Parameterized.class)
public class SectionListingDebug{

    private static class Builder{
        private int doc;
        private int ptr;
        private int[] sections;
        private int outlineStart;
        private int[] findOutline;

        private Builder(int docNum, int expect){
            doc = docNum;
            ptr = expect;
            outlineStart = -1;
            sections = new int[]{};
            findOutline = new int[]{};
        }

        private Builder sec(int ... ptr){
            sections = ptr;
            return this;
        }

        private Builder out(int start, int ... ptr){
            outlineStart = start;
            findOutline = ptr;
            return this;
        }

        private Object[] build(){
            return new Object[]{doc, ptr, sections, outlineStart, findOutline};
        }
    }


    @Parameters
    public static Collection<Object[]> data() throws Exception{
        ArrayList<Object[]> data = new ArrayList<>();
        data.add(new Builder(1, -1).build());
        data.add(new Builder(1, 5).out(0, 0).build());
        return data;
    }

    private static ManuscriptDocument[] docs;
    private static SectionHeading[] root;

    @BeforeClass
    public static void beforeClass() throws Exception{
        docs = SupplementSectionDebug.buildDocs(true);
        root = new SectionHeading[docs.length];
        int i = 0;
        for (ManuscriptDocument doc: docs){
            root[i++] = doc.getSections();
            System.out.println(root[i - 1]);
        }
    }


    @Parameter
    public int doc;

    @Parameter(1)
    public int ptrSpan;

    @Parameter(2)
    public int[] findSection;

    @Parameter(3)
    public int startOutline;

    @Parameter(4)
    public int[] findOutline;

    @Test
    public void test(){
        System.out.println("Search Headings");
        Section search = root[doc - 1];
        for (int i : findSection){
            System.out.println(search);
            assertTrue("Section not in range: " + search, i < search
                .getChildren().size());
            search = search.getChildren().get(i);
        }

        System.out.println("Search Outlines");
        if (findOutline.length != 0){
            SectionOutline outline = null;
            assertTrue("Outline not in range: " + search, startOutline <
                ((SectionHeading)search).getOutlines().size());
            search = ((SectionHeading)search).getOutlines().get(startOutline);
            for (int i: findOutline){
                System.out.println(search);
                assertTrue("Outline not in range: " + search, i < search
                    .getChildren().size());
                search = search.getChildren().get(i);
            }
        }
        System.out.println("Testing...");
        System.out.println(search);

        Span expect = ptrSpan != -1 ?
            ((SpanBranch)(docs[doc - 1]).get(ptrSpan)).get(0):
            null;

        Optional<LinedSpanLevelSection> test = search.getLine();
        System.out.println(expect);
        if (expect == null){
            assertFalse(test.toString(), test.isPresent());
        } else {
            assertTrue(test.toString(), test.isPresent());
            assertSame(expect, test.get());
        }
        System.out.println("Passed\n");
    }
}

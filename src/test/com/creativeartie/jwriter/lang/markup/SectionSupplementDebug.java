package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import com.google.common.io.*;
import com.google.common.base.*;
import org.junit.runners.Parameterized.*;

import java.io.*;
import java.util.*;

import com.creativeartie.jwriter.lang.*;

@RunWith(Parameterized.class)
public class SectionSupplementDebug {
    
    private static Object[] build(int file, boolean scene, int ... parts){
        ArrayList<Integer> heading = new ArrayList<>();
        ArrayList<Integer> outline = new ArrayList<>();
        boolean sep = false;
        for(int part: parts){
            if (part == -1){
                sep = true;
            } else if (sep){
                heading.add(part);
            } else {
                outline.add(part);
            }
        }
        return new Object[]{file, heading, outline, scene};
    }

    @Parameters
    public static Collection<Object[]> data() throws Exception{
        ArrayList<Object[]> test = new ArrayList<>();
        test.add(build(1, false, 0));
        test.add(build(1, false, 0, -1, 0));
        test.add(build(1, false, 0, -1, 1));
        test.add(build(1, false, 0, -1, 2));
        test.add(build(1, false, 0, -1, 3));
        test.add(build(1, true,  0, -1, 4));
        test.add(build(1, true,  0, -1, 4, 0));
        test.add(build(1, true,  0, -1, 4, 0, 0));
        for (int i = 1; i <= 6; i++){
            File file = new File("data/sectionDebug" + i + ".txt");
            System.out.println(new WritingText(Files
                .asCharSource(file, Charsets.UTF_8).read()));
        }
        return test;
    }
    
    private int level;
    private List<Integer> heading;
    private List<Integer> outline;
    private boolean scene;
    private WritingText text;

    public SectionSupplementDebug(int l, List<Integer> h, List<Integer> o, 
            boolean b){
        level = l;
        heading = h;
        outline = o;
        scene = b;
    }
    
    
    @Test
    public void testHeading() throws Exception{
        File file = new File("data/sectionDebug" + level + ".txt");
        text = new WritingText(Files.asCharSource(file, Charsets.UTF_8).read());
        getHeading();
    }
    
    private SpanNode<?> getHeading(){
        SpanNode<?> span = text;
        for(int head: heading){
            assertTrue("Size is too small for: " + span, span.size() > head);
            Span found = span.get(head);
            assertTrue("Span is not a SpanNode: ", found instanceof SpanNode);
            span = (SpanNode<?>) found;
        }
        assertTrue("Span is not an instance of SectionSpanHead: " + span, 
            span instanceof SectionSpanHead);
        return span;
    }
    
    @Test
    public void testOutline() throws Exception{
        File file = new File("data/sectionDebug" + level + ".txt");
        text = new WritingText(Files.asCharSource(file, Charsets.UTF_8).read());
        
        SpanNode<?> span = getHeading();
        
        Assume.assumeTrue(outline != null);
        for(int out: outline){
            assertTrue("Size is too small: " + span, out < span.size());
            Span found = span.get(out);
            assertTrue("Span is not an instance of SpanNode: " + found,
                found instanceof SpanNode);
            span = (SpanNode<?>) span;
        }
        if (scene){
            assertTrue("Span is not instance of SectionSpanScene: " + span, 
                span instanceof SectionSpanScene);
        } else {
            assertFalse("Span is an instance of SectionSpanScene: " + span, 
                span instanceof SectionSpanScene);
        }
    }
}
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

    private static final int FILE_SIZE = 7;

    private static boolean verbose = false;

    private static enum Type {HEADING, OUTLINE, OTHERS, SKIP_LINE, NEW_FILE};

    @Parameters
    public static Collection<Object[]> data() throws Exception{
        WritingText[] texts = new WritingText[FILE_SIZE];
        for (int i = 1; i <= FILE_SIZE; i++){
            File file = new File("data/sectionDebug" + i + ".txt");
            texts[i - 1] = new WritingText(Files
                .asCharSource(file, Charsets.UTF_8).read());
            if (verbose) System.out.println(texts[i - 1]);
            if (verbose) System.out.println();
        }
        ArrayList<Object[]> test = new ArrayList<>();
        File file = new File("data/sectionDebugOutcomes.txt");
        try (Scanner scan = new Scanner(new FileInputStream(file))){
            while(scan.hasNextInt()){
                int index = scan.nextInt();
                Type scene = Type.valueOf(scan.next());
                if (scene == Type.SKIP_LINE || scene == Type.NEW_FILE){
                    scan.next();
                    continue;
                }
                ArrayList<Integer> location = new ArrayList<>();
                while(scan.hasNextInt()){
                    location.add(scan.nextInt());
                }
                scan.next();
                test.add(new Object[]{texts[index - 1], scene, location});
            }
        }
        return test;
    }

    @Parameter
    public WritingText text;

    @Parameter(value = 1)
    public Type type;

    @Parameter(value = 2)
    public List<Integer> location;

    @Test
    public void test(){
        SpanNode<?> span = text;
        for (int ptr: location){
            assertTrue("Wrong size: " + span.size() + " <= " + ptr, span.size() > ptr);
            Span find = span.get(ptr);
            assertTrue(find instanceof SpanNode<?>);
            span = (SpanNode<?>) find;
        }
        switch (type){
            case HEADING:
                assertTrue(span.getRaw(), span instanceof SectionSpanHead);
                break;
            case OUTLINE:
                assertTrue(span.getRaw(), span instanceof SectionSpanScene);
                break;
            default:
                assertFalse(span.getRaw(), span instanceof SectionSpanHead);
                assertFalse(span.getRaw(), span instanceof SectionSpanScene);
        }
    }
}

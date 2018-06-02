package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.io.*;
import java.util.*;

import com.google.common.io.*;
import com.google.common.base.*;

import com.creativeartie.writerstudio.lang.*;

import static org.junit.jupiter.api.Assertions.*;

public class SectionSupplementTest {

    private static final int FILE_SIZE = 7;

    private static boolean verbose = false;

    private static enum Type {HEADING, OUTLINE, OTHERS, SKIP_LINE, NEW_FILE};

    public static ArrayList<Arguments> test() throws Exception{
        WritingText[] texts = new WritingText[FILE_SIZE];
        for (int i = 1; i <= FILE_SIZE; i++){
            File file = new File("build/resources/test/sectionDebug" + i + ".txt");
            texts[i - 1] = new WritingText(Files
                .asCharSource(file, Charsets.UTF_8).read());
            if (verbose) System.out.println(texts[i - 1]);
            if (verbose) System.out.println();
        }
        ArrayList<Arguments> test = new ArrayList<>();
        File file = new File("build/resources/test/sectionDebugOutcomes.txt");
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
                test.add(Arguments.of(texts[index - 1], scene, location));
            }
        }
        return test;
    }

    @ParameterizedTest(name = "{index}: {2} -> ({1})")
    @MethodSource()
    public void test(WritingText text, Type type, List<Integer> location){
        SpanNode<?> span = text;
        for (int ptr: location){
            SpanNode<?> target = span;
            assertTrue(span.size() > ptr,
                () -> "Wrong size: " + target.size() + " <= " + ptr);
            Span find = span.get(ptr);
            assertTrue(find instanceof SpanNode<?>);
            span = (SpanNode<?>) find;
        }
        SpanNode<?> test = span;
        switch (type){
            case HEADING:
                assertTrue(span instanceof SectionSpanHead, () -> test.getRaw());
                break;
            case OUTLINE:
                assertTrue(span instanceof SectionSpanScene, () -> test.getRaw());
                break;
            default:
                assertFalse(span instanceof SectionSpanHead, () ->
                    test.getRaw() + " is head");
                assertFalse(span instanceof SectionSpanScene, () ->
                    test.getRaw() + " is scene");
        }
    }
}

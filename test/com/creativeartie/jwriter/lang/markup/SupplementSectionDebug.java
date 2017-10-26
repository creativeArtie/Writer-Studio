package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;
import java.io.File;

import com.creativeartie.jwriter.lang.*;

@RunWith(Parameterized.class)
public class SupplementSectionDebug {

    @Parameter
    public int docNum;

    @Parameter(value = 1)
    public int index;

    @Parameter(value = 2)
    public int last;

    @Parameter(value = 3)
    public int next;

    @Parameter(value = 4)
    public int heading;

    @Parameter(value = 5)
    public int outline;

    @Parameters
    public static Collection<Object[]> data() throws Exception{
        ArrayList<Object[]> data = new ArrayList<>();
        try (Scanner scan = new Scanner(new File("data/mainSectionTest.csv"))){
            while (scan.hasNext()){
                data.add(new Object[]{
                    scan.nextInt(), /// 0
                    scan.nextInt(), /// 1
                    scan.nextInt(), /// 2
                    scan.nextInt(), /// 3
                    scan.nextInt(), /// 4
                    scan.nextInt(), /// 5
                });
            }
        }
        return data;
    }

    private static ManuscriptDocument[] docs;

    @BeforeClass
    public static void beforeClass() {
        docs = buildDocs(false);
    }

    public static ManuscriptDocument[] buildDocs(boolean verbose){
        ManuscriptDocument[] ans = new ManuscriptDocument[5];
        for (int i = 1; i <= 5; i++){
            String file = "data/sectionDebug" + i + ".txt";
            ans[i - 1] = getFile(file);
            if (verbose){
                System.out.println(file);
                int sec = 0;
                for (SpanBranch section: ans[i - 1]){
                    int ln = 0;
                    char t = section instanceof MainSpanNote? 'N': 'S';
                    for (Span line : section){
                        System.out.printf("Sec." + t + "%-3d:Ln.%-3d:\t", sec,
                            ln);
                        ln++;
                        System.out.println(line);
                    }
                    sec++;
                }
                System.out.println();
            }
        }
        return ans;
    }

    private static ManuscriptDocument getFile(String file) {
        File input = new File(file);
        StringBuilder rawBuilder = new StringBuilder();
        try(Scanner scan = new Scanner(input)){
            while (scan.hasNextLine()){
                String found = scan.nextLine();
                rawBuilder.append(found + "\n");
            }
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
        return new ManuscriptDocument(rawBuilder.toString());
    }

    private MainSpanSection test;
    private ManuscriptDocument doc;

    @Before
    public void before(){
        doc = docs[docNum - 1];
        test = (MainSpanSection) doc.get(index);
    }

    @Test
    public void testLastPart(){
        testCommon(getMainSection(last), test.getLastPart());
    }

    @Test
    public void testNextPart(){
        testCommon(getMainSection(next), test.getNextPart());
    }

    @Test
    public void testHeading(){
        testCommon(getTopLine(heading), test.getHeading());
    }

    @Test
    public void testOutline(){
        testCommon(getTopLine(outline), test.getOutline());
    }


    private MainSpanSection getMainSection(int loc){
        return loc == -1? null: (MainSpanSection)doc.get(loc);
    }

    private LinedSpanSection getTopLine(int loc){
        return loc == -1? null: (LinedSpanSection)doc.get(loc).get(0);
    }


    private <T> void testCommon(T expect, Optional<T> found){
        if (expect == null){
            assertFalse("Unexpected Span for " + test + " found: " + found,
                found.isPresent());
        } else {
            assertTrue("Unexpected empty for " + test + ". Expect: " + expect,
                found.isPresent());
            assertSame("Different spans: " + test.toString(), expect,
                found.get());
        }
    }
}

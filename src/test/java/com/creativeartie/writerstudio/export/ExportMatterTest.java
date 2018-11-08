package com.creativeartie.writerstudio.export;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.time.*;
import java.util.*;
import java.util.stream.*;

import com.creativeartie.writerstudio.export.mock.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExportMatterTest{

    private MockContentMatter newMatter(){
        MockContentMatter ans = new MockContentMatter();
        ans.add(new MockContentLine("The itsy-bitsy spider")); /// 0
        ans.add(new MockContentLine("Climed up ", "the water spout")); /// 1
        ans.add(new MockContentLine("Down came the rain")); /// 2
        ans.add(new MockContentLine("And washed the spider out")); /// 3
        ans.add(new MockContentLine("Out came the sun")); /// 4
        ans.add(new MockContentLine("And dried ", "up all the rain")); /// 5
        ans.add(new MockContentLine("And the ", "itsy-bitsy ", "spider")); /// 6
        ans.add(new MockContentLine("Climbed up ", "the spout again")); /// 7
        return ans;
    }

    private MockContentMatter newNotedMatter(){
        MockContentMatter ans = new MockContentMatter();

        MockContentLine line = new MockContentLine();
        line.add(new MockContentData("This old man, he played "));
        MockContentData note = new MockContentData("num");
        note.setFootnote(Optional.of(new MockContentLine("From one to ten")));
        line.add(note);
        line.add(new MockContentData("."));
        ans.add(line);

        line = new MockContentLine();
        line.add(new MockContentData("He played knick-knack on my "));
        note = new MockContentData("ex");
        note.setFootnote(Optional.of(new MockContentLine(
            "drum, shoe, knee,  door, hive, sticks, gate, spine"
        )));
        line.add(note);
        line.add(new MockContentData(";"));
        ans.add(line);

        ans.add(new MockContentLine("With a knick-knack paddywhack,"));

        ans.add(new MockContentLine("Give a dog a bone,"));

        line = new MockContentLine();
        line.add(new MockContentData("This "));
        note = new MockContentData("old man");
        note.setFootnote(Optional.of(
            new MockContentLine("last one is 'once again', not 'on my spine'")
        ));
        line.add(note);
        line.add(new MockContentData(" came rolling home"));
        ans.add(line);

        return ans;
    }

    @Test
    public void renderBasicRunning(){
        ExportMatterRunning<Integer> test = new ExportMatterRunning<>(newMatter(),
            new MockRenderMatter());
        test.render();
        assertNotNull(test.getFillHeight());
        assertEquals(85, test.getFillHeight().intValue());
    }

    @Test
    public void renderEmptyRunning(){
        ExportMatterRunning<Integer> test = new ExportMatterRunning<>(
            new MockContentMatter(), new MockRenderMatter());
        test.render();
        assertNull(test.getFillHeight());
    }

    @Test
    public void renderEmptyFootnote(){
        ExportMatterFootnote<Integer> test = new ExportMatterFootnote<>(
            new MockRenderMatter());
        assertNull(test.getFillHeight());
    }

    @Test
    public void renderSlowlyFootnote(){
        ExportMatterFootnote<Integer> test = new ExportMatterFootnote<>(
            new MockRenderMatter());
        int size = 5;
        int i = 0;

        for (ContentLine line: newMatter()){
            RenderLine<Integer> render = new MockRenderLine(100,100);
            ExportLineMain<Integer> in = new ExportLineMain<>(line, render);
            test.addNotes(Arrays.asList(in));
            size += 10;
            assertNotNull(test.getFillHeight(), "Line: " + i);
            assertEquals(size, test.getFillHeight().intValue(), "Line: " + i);
            i++;
        }
    }

    @Test
    public void renderAllFootnote(){
        ExportMatterFootnote<Integer> test = new ExportMatterFootnote<>(
            new MockRenderMatter());
        ArrayList<ExportLineMain<Integer>> lines = new ArrayList<>();
        for (ContentLine line: newMatter()){
            RenderLine<Integer> render = new MockRenderLine(100,100);
            lines.add(new ExportLineMain<>(line, render));
        }
        test.addNotes(lines);
        assertNotNull(test.getFillHeight());
        assertEquals(85, test.getFillHeight().intValue());
    }
}

package com.creativeartie.writer.export;

import org.junit.jupiter.api.*;
import java.util.*;
import com.creativeartie.writer.export.mock.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExportMatterTest{

    private MockContentMatter newMatter(){
        MockContentMatter ans = new MockContentMatter();
        ans.add(new MockContentLine("The itsy-bitsy spider")); /// 0
        ans.add(new MockContentLine("Climed up ", "the water spout")); /// 1
        ans.add(new MockContentLine("Down cam the rain")); /// 2
        ans.add(new MockContentLine("And washed the pider out")); /// 3
        ans.add(new MockContentLine("Out came the sun")); /// 4
        ans.add(new MockContentLine("And dried ", "up all the rain")); /// 5
        ans.add(new MockContentLine("And the ", "itsy-bitsy ", "spider")); /// 6
        ans.add(new MockContentLine("Climbed up ", "the spout again")); /// 7
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

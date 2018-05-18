package com.creativeartie.writerstudio.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.writerstudio.lang.DocumentAssert.*;
import static com.creativeartie.writerstudio.lang.markup.BranchTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchLineTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchSectionTest.*;
import static com.creativeartie.writerstudio.lang.markup.BranchFormatTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableListMultimap;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;

import com.creativeartie.writerstudio.lang.*;

@RunWith(JUnit4.class)
public class SectionAllowDebug {

    @Test
    public void outlineAllowHigherHeading(){
        String text = "====abc";
        assertFalse(SectionSpan.allowChild(text, 3, false));
    }

    @Test
    public void outlineAllowEqualHeading(){
        String text = "===abc";
        assertFalse(SectionSpan.allowChild(text, 3, false));
    }

    @Test
    public void outlineAllowLowerHeading(){
        String text = "==abc";
        assertFalse(SectionSpan.allowChild(text, 3, false));
    }

    @Test
    public void outlineAllowHigherOutline(){
        String text = "!####abc";
        assertTrue(SectionSpan.allowChild(text, 3, false));
    }

    @Test
    public void outlineAllowEqualOutline(){
        String text = "!###abc";
        assertFalse(SectionSpan.allowChild(text, 3, false));
    }

    @Test
    public void outlineAllowLowerOutline(){
        String text = "!##abc";
        assertFalse(SectionSpan.allowChild(text, 3, false));
    }

    @Test
    public void outlineAllowOthers(){
        String text = "abc";
        assertTrue(SectionSpan.allowChild(text, 3, false));
    }

    @Test
    public void headingAllowHigherHeading(){
        String text = "====abc";
        assertTrue(SectionSpan.allowChild(text, 3, true));
    }

    @Test
    public void headingAllowEqualHeading(){
        String text = "===abc";
        assertFalse(SectionSpan.allowChild(text, 3, true));
    }

    @Test
    public void headingAllowLowerHeading(){
        String text = "==abc";
        assertFalse(SectionSpan.allowChild(text, 3, true));
    }

    @Test
    public void headingAllowHigherOutline(){
        String text = "!####abc";
        assertTrue(SectionSpan.allowChild(text, 3, true));
    }

    @Test
    public void headingAllowEqualOutline(){
        String text = "!###abc";
        assertTrue(SectionSpan.allowChild(text, 3, true));
    }

    @Test
    public void headingAllowLowerOutline(){
        String text = "!##abc";
        assertTrue(SectionSpan.allowChild(text, 3, true));
    }

    @Test
    public void headingAllowOthers(){
        String text = "abc";
        assertTrue(SectionSpan.allowChild(text, 3, true));
    }
}
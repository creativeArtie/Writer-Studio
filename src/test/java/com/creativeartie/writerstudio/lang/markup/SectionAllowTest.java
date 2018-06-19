package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class SectionAllowTest {

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
        assertFalse(SectionSpan.allowChild(text, 3, false));
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

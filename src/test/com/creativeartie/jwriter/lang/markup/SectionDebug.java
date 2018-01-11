package com.creativeartie.jwriter.lang.markup;

import static org.junit.Assert.*;
import static com.creativeartie.jwriter.lang.DocumentAssert.*;
import static com.creativeartie.jwriter.lang.markup.BranchTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchLineTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchLinesTest.*;
import static com.creativeartie.jwriter.lang.markup.BranchFormatTest.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableListMultimap;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;

import com.creativeartie.jwriter.lang.*;

@RunWith(JUnit4.class)
public class SectionDebug {
    private static final SetupParser PARSER = SectionParseSection.SECTION_1;

    @Test
    public void noHeading(){
        String line1 = "Hello World!";
        String line2 = ">Beep! Beep! Beep?";
        String full = line1 + line2;
        DocumentAssert doc = assertDoc(1, full, PARSER);
    }
}
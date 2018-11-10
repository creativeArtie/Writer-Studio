package com.creativeartie.writerstudio.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.*;
import java.io.*;

@DisplayName("Properties Tests")
public class ProgramPropertiesTest {

    @Test
    public void programName(){
        assertEquals("Writer Studio", ProgramProperties.PROGRAM_NAME.get());
    }

    @Test
    public void programVersion() throws IOException{
        /// extracts the version name from the build.gradle file itself.
        String raw = "";
        try (Scanner scan = new Scanner(new File("../build.gradle"))){
            do {
                raw = scan.nextLine();
            } while (! raw.startsWith("version"));
        }
        String expect = raw.substring(raw.indexOf("\"")+1, raw.length()-1).trim();
        assertEquals(expect, ProgramProperties.PROGRAM_VERSION.get());
    }
}

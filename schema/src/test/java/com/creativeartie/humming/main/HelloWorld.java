package com.creativeartie.humming.main;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

/** The hello world test class. */
@DisplayName("Basic System")
@SuppressWarnings("nls")
class HelloWorld {
    /** If this run, JUnit5 is working! */
    @Test
    @DisplayName("Basic")
    public void helloWorld() {
        assertTrue(true);
    }

    /** Find a resource test. */
    @Test
    @DisplayName("File search")
    public void findTestFile() {
        File f = new File("build.gradle");
        assertTrue(f.exists());
    }

    @ParameterizedTest(name = "Parameter check {index}.")
    @DisplayName("Parameter check")
    @ValueSource(ints = { 1, 2, 3 })
    public void parameterRun(int value) {
        assertTrue(true);
    }
}

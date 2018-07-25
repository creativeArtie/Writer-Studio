package com.creativeartie.writerstudio.export;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ContentSplitTest{

    private static ExportContentText<Integer> buildExporter(
        MockBridgeContent content)
    {
        return new ExportContentText<>(MockFactoryRender.FACTORY, content);
    }

    @Test
    public void noSplit(){
        ExportContentText<Integer> text = buildExporter(
            new MockBridgeContent("Hello"));
        Optional<ExportContentText<Integer>> overflow = text.split(100);
        assertFalse(overflow.isPresent(),
            () -> "Unexpected: " + overflow);
        assertEquals("Hello", text.getText());
    }

    @Test
    public void noSplitCallTwice(){
        ExportContentText<Integer> text = buildExporter(
            new MockBridgeContent("Hello"));
        Optional<ExportContentText<Integer>> overflow = text.split(100);
        assertFalse(text.split(100).isPresent(), "Filled");

        assertEquals("Hello", text.getText());
    }

    @Test
    public void split(){
        ExportContentText<Integer> text = buildExporter(
            /// -------------------01234567890123456
            new MockBridgeContent("Hello World Song"));
        Optional<ExportContentText<Integer>> overflow = text.split(6);
        assertTrue(overflow.isPresent(), "Empty");
        assertEquals("World Song", overflow.get().getText());
        assertEquals("Hello", text.getText());
    }

    @Test
    public void notFit(){
        ExportContentText<Integer> text = buildExporter(
            /// -------------------01234567890123456
            new MockBridgeContent("Hello World Song"));
        Optional<ExportContentText<Integer>> overflow = text.split(3);
        assertTrue(overflow.isPresent(), "Empty");
        assertEquals("Hello World Song", overflow.get().getText());
        assertEquals("", text.getText());
    }
}

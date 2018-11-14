package com.creativeartie.writerstudio.pdf;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

import java.awt.*;

import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.pdf.WritingExporter.PdfFont;
import com.creativeartie.writerstudio.pdf.value.*;

@DisplayName("Font Editing")
@Tag("pdf")
@ExtendWith(PdfFontParameterResolver.class)
public class PdfFontTest{

    private static float baseSize;

    private PdfFont newFont;
    private PdfFont useFont;

    private Color outColor;
    private int outSize;
    private boolean outLined;
    private boolean outSuper;
    private String outFont;

    @BeforeAll
    public static void beforeAll(PdfFont font){
        baseSize = font.getHeight() / 12;
    }

    @BeforeEach
    public void beforeEach(){
        outColor = Color.BLACK;
        outSize = 12;
        outLined = false;
        outSuper = false;
        outFont = "FreeSerif";

    }

    @Test
    @DisplayName("Set Size")
    public void setSize(PdfFont font){
        useFont = font;
        newFont = font.changeSize(18);
        outSize = 18;
    }

    @Test
    @DisplayName("Set Color")
    public void setColor(PdfFont font){
        useFont = font;
        newFont = font.changeFontColor(Color.RED);
        outColor = Color.RED;
    }

    @Test
    @DisplayName("Set Bold")
    public void setBold(PdfFont font){
        useFont = font;
        newFont = font.changeBold(true);
        outFont = "FreeSerifBold";
    }

    @Test
    @DisplayName("Set Italaics")
    public void setItalics(PdfFont font){
        useFont = font;
        newFont = font.changeItalics(true);
        outFont = "FreeSerifItalic";
    }

    @Test
    @DisplayName("Set Underline")
    public void setUnderline(PdfFont font){
        useFont = font;
        newFont = font.changeUnderline(true);
        outLined = true;
    }

    @Test
    @DisplayName("Reset Underline")
    public void resetUnderline(PdfFont font){
        useFont = font;
        newFont = font.changeUnderline(true).changeUnderline(false);
    }

    @AfterEach
    public void afterEach(){
        assertEquals(12 * baseSize, useFont.getHeight(), 1f,   "In height");
        assertEquals(Color.BLACK, useFont.getColor(),          "In color");
        assertEquals(false,       useFont.isUnderline(),       "In underline");
        assertEquals(false,       useFont.isSuperscript(),     "In super");
        assertEquals("FreeSerif", useFont.getFont().getName(), "In Font");

        assertTrue(newFont instanceof PdfFont, "Wrong class.");
        PdfFont font = newFont;
        assertEquals(outSize * baseSize, font.getHeight(), 1f, "Out height");
        assertEquals(outColor, font.getColor(),                "Out color ");
        assertEquals(outLined, font.isUnderline(),             "Out underline");
        assertEquals(outSuper, font.isSuperscript(),           "Out super");
        assertEquals(outFont,  font.getFont().getName(),       "Out Font");
    }
}

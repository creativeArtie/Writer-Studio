package com.creativeartie.writerstudio.pdf;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.io.*; /// For exporting doc
import java.time.*; /// timeout assertion

import org.apache.pdfbox.pdmodel.*; /// superscript testing
import org.apache.pdfbox.pdmodel.font.*; /// font testing

import com.creativeartie.writerstudio.lang.markup.*;

@DisplayName("PDF General Export")
@Tag("pdf")
public class PdfTest{

    private static final String RESOURCE = "build/resources/test/";
    private static final String OUT = "build/outputs/";

    @BeforeAll
    public static void beforeAll(){
        new File(OUT).mkdirs();
    }

    @Test
    @DisplayName("Superscript Font")
    public void fontTest() {
        assertDoesNotThrow( () -> {
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(doc, page);

            PDFont font = PDType0Font.load(doc, PdfTest.class.getResourceAsStream(
                "/data/fonts/FreeSerif.ttf"));

            contentStream.beginText();
            contentStream.newLineAtOffset(100, 500);
            contentStream.setFont(font, 12);
            contentStream.showText("ab⁰¹²³⁴⁵⁶⁷⁸⁹ⁱᵛˣˡᶜᵈᵐ cd");
            contentStream.close();
            doc.save(OUT + "test-font.pdf");
            doc.close();
        });
    }

    /** Tests for the exporting 1.
     *
     * There are 4 tests:
     * <ul>
     * <li>{@code sample} is for showcase</li>
     * <li>{@code basic} test everything once</li>
     * <li>{@code foot} works with footnotes</li>
     * <li>{@code stress} deals a massive document</li>
     * </ul>
     */
    @ParameterizedTest(name = "Export Basic Document: pdf-{0}.txt")
    @DisplayName("Export Basic Document")
    @ValueSource(strings = { "sample", "basic", "foot" , "long" })
    public void exportWellFormDoc(String file) throws IOException{
        testExport("pdf-" + file + ".txt", file + ".pdf");
    }

    @ParameterizedTest(name = "Export Malform Document: pdf-{0}.txt")
    @DisplayName("Export Malform Document")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    public void exportMalFormDoc(int i) throws IOException{
        testExport("sectionDebug" + i + ".txt", "malform" + i + ".pdf");
    }

    private void testExport(String in, String out) throws IOException{
        File doc = new File(RESOURCE + in);
        WritingFile use = WritingFile.newSampleFile(doc);
        try (WritingExporter test = new WritingExporter(OUT + out)){
            assertTimeout(Duration.ofSeconds(60), () -> test.export(use),
            "Export takes over 1 minute");
        }
        System.out.println("Check file at: " + OUT + out);
    }

}

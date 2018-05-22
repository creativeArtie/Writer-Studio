package com.creativeartie.writerstudio.pdf;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import  org.junit.jupiter.params.*;
import  org.junit.jupiter.params.provider.*;

import java.io.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;

import com.creativeartie.writerstudio.lang.markup.*;

@DisplayName("PDF Export General Tests")
public class PdfTest{

	private static final String RESOURCE = "build/resources/test/";
	private static final String OUT = "build/outputs/pdf";
	
	@BeforeAll
	public static void beforeAll(){
		new File(OUT).mkdirs();
	}
	
	@Test
	@DisplayName("Superscript font test.")
	public void fontTest() {
		assertDoesNotThrow( () -> {
			PDDocument doc = new PDDocument();
			PDPage page = new PDPage();
			doc.addPage(page);
			PDPageContentStream contentStream = new PDPageContentStream(doc, page);
			
			PDFont font = PDType0Font.load(doc, PdfTest.class.getResourceAsStream(
				"/data/fonts/FreeSerif.ttf"));

			contentStream.beginText();
			contentStream.newLineAtOffset(100, 50);
			contentStream.setFont(font, 12);
			contentStream.showText("⁰¹²³⁴⁵⁶⁷⁸⁹ⁱᵛˣˡᶜᵈᵐ");
			contentStream.close();
			doc.save(OUT + "test-font.pdf");
			doc.close();
		});
	}
	
	@ParameterizedTest
	@DisplayName("Export files to test for IOExceptions")
	@ValueSource(strings = { "basic", "long" })
	public void exportDoc(String file) throws IOException{
		File doc = new File(RESOURCE + "pdf-" + file + ".txt");
		WritingFile use = WritingFile.newSampleFile(doc);
		try (WritingExporter out = new WritingExporter(OUT + file + ".pdf")){
			out.export(use);
		}
	}

}

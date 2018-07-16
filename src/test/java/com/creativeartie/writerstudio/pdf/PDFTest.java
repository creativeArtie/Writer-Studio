package com.creativeartie.writerstudio.pdf;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.*;

import com.creativeartie.writerstudio.lang.markup.*;

@DisplayName("PDF Export General Tests")
public class PDFTest{

	private static final String RESOURCE = "build/resources/test/";
	private static final String OUT = "build/resources/";

	@Test
	@DisplayName("Best scenario document.")
	public void docOutput(){
		File file = new File(RESOURCE + "test.txt");
		assertDoesNotThrow( () -> {
			WritingFile use = WritingFile.newSampleFile(file);
			try (WritingExporter out = 
					new WritingExporter(OUT + "test.pdf")){
				out.export(use);
			}
        });
	}
}

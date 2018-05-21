package com.creativeartie.writerstudio.main;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

/** The hello world test class. */
@DisplayName("Basic System Tests.")
public class HelloWorld{

	/** If this run, JUnit5 is working! */
	@Test
	@DisplayName("Hello World Test.")
	public void helloWorld(){
		assertTrue(true);
	}
	
	/** Find a resource test. */
	@Test
	@DisplayName("File search")
	public void findTestFile(){
		File f = new File("build/resources/test/test.txt");
		assertTrue(f.exists());
	}
	
}

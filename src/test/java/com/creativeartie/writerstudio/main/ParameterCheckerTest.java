package com.creativeartie.writerstudio.main;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** */
public class ParameterCheckerTest {

	@Test
	public void argumentCheckPass(){
		assertDoesNotThrow(
			() -> argumentCheck(true, "field", " error")
		);
	}
	
	@Test
	public void argumentCheckFail(){
		Throwable exception = assertThrows(
			IllegalArgumentException.class, 
			() -> argumentCheck(false, "field", " error")
		);
		assertEquals("Parameter \"field\" error", exception.getMessage());
	}
}

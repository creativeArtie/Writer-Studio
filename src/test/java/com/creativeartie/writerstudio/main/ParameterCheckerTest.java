package com.creativeartie.writerstudio.main;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** */
public class ParameterCheckerTest {
    private static final String FIELD = "field";
    private static final String ERROR = "Parameter \"field\"";
    
    @Nested
    @DisplayName("Simple Assertions")
    class CustomMessages{
        private static final String MESSAGE = "Exception thrown.";
        
        @Test
        @DisplayName("IOException Pass")
        public void ioPass(){
            assertDoesNotThrow(() -> ioCheck(true, MESSAGE));
        }
        
        @Test
        @DisplayName("IOException Fail")
        public void ioFail(){
            Throwable thrown = assertThrows(IOException.class, () -> 
                ioCheck(false, MESSAGE));
            assertEquals(MESSAGE, thrown.getMessage());
        }
        
        @Test
        @DisplayName("IllegalStateException Pass")
        public void statePass(){
            assertDoesNotThrow(() -> stateCheck(true, MESSAGE));
        }
        
        @Test
        @DisplayName("IllegalStateException Fail")
        public void stateFail(){
            /// Also tests ParameterCheckerTest#stateBuild(String)
            Throwable thrown = assertThrows(IllegalStateException.class, () -> 
                stateCheck(false, MESSAGE));
            assertEquals(MESSAGE, thrown.getMessage());
        }
    }
    
    @Nested
    @DisplayName("Class Cast Assertions")
    class ArgumentClass{
        @Test
        @DisplayName("Self test")
        public void self(){
            assertDoesNotThrow(() -> argumentClass(new Integer(3), FIELD, 
                Integer.class));
        }
        
        @Test
        @DisplayName("Child test")
        public void child(){
            assertDoesNotThrow(() -> argumentClass(new Integer(3), FIELD, 
                Number.class));
        }
        
        @Test
        @DisplayName("Fail test")
        public void fail(){
            Throwable thrown = assertThrows(IllegalArgumentException.class, () -> 
                 argumentClass(new Integer(3), FIELD, Boolean.class));
            assertEquals(ERROR + " (Integer) is not an instance of Boolean.", 
                thrown.getMessage());
        }
    }
    
    private enum TestEnum{
        A, B, C, D;
    }
    
    @Nested
    @DisplayName("Enum Assertions")
    class EnumAsserts{
        
        @Test
        @DisplayName("Not Enum Passes")
        public void notEnumNotMatch(){
            assertDoesNotThrow(() -> argumentNotEnum(TestEnum.C, FIELD,
                TestEnum.B, TestEnum.D));
        }
        
        @Test
        @DisplayName("Not Enum Null")
        public void notEnumNull(){
            Throwable thrown = assertThrows(IllegalArgumentException.class, () -> 
                 argumentNotEnum(null, FIELD, TestEnum.B, TestEnum.D));
            assertEquals(ERROR + " is null.", thrown.getMessage());
        }
        
        @Test
        @DisplayName("Not Enum Fail")
        public void notEnumMatch(){
            Throwable thrown = assertThrows(IllegalArgumentException.class, () -> 
                 argumentNotEnum(TestEnum.B, FIELD, TestEnum.B, TestEnum.C));
            assertEquals(ERROR + " can not be `B`.", thrown.getMessage());
        }
        
        @Test
        @DisplayName("Not Enum for Nothing")
        public void notEnumAssert(){
            Throwable thrown = assertThrows(AssertionError.class, () -> 
                 argumentNotEnum(null, FIELD));
            assertEquals("Empty nots", thrown.getMessage());
        }
        
        @Test
        @DisplayName("Match Enum Pass")
        public void requireEnumMatch(){
            assertDoesNotThrow(() -> argumentRequireEnum(TestEnum.B, FIELD,
                TestEnum.B, TestEnum.D));
        }
        
        @Test
        @DisplayName("Match Enum Null")
        public void requireEnumNull(){
            Throwable thrown = assertThrows(IllegalArgumentException.class, () -> 
                 argumentRequireEnum(null, FIELD, TestEnum.B, TestEnum.D));
            assertEquals(ERROR + " is null.", thrown.getMessage());
        }
        
        @Test
        @DisplayName("Match Enum Fail")
        public void requireEnumNotMatch(){
            Throwable thrown = assertThrows(IllegalArgumentException.class, () -> 
                 argumentRequireEnum(TestEnum.A, FIELD, TestEnum.B, TestEnum.C));
            assertEquals(ERROR + " can not be `A`.", thrown.getMessage());
        }
        
        @Test
        @DisplayName("Not Enum for Nothing")
        public void requireEnumAssert(){
            Throwable thrown = assertThrows(AssertionError.class, () -> 
                 argumentRequireEnum(null, FIELD));
            assertEquals("Empty requires.", thrown.getMessage());
        }
    }
    
    /// TODO more assertion tests 
}

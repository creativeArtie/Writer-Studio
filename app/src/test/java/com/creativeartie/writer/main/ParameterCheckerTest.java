package com.creativeartie.writer.main;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.*;

import static com.creativeartie.writer.main.ParameterChecker.*;

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

    @Nested
    @DisplayName("Empty Assertions")
    class EmptyAsserts{
        private static final String EMPTY = ERROR + " is empty.";
        private static final String NULL = ERROR + " is null.";

        @DisplayName("Collection Pass")
        @Test
        public void collectionFilled(){
            ArrayList<String> list = new ArrayList<String>();
            list.add("abc");
            assertDoesNotThrow(() -> argumentNotEmpty(list, FIELD));
        }

        @DisplayName("Collection Empty Fail")
        @Test
        public void collectionEmpty(){
            ArrayList<String> list = new ArrayList<String>();
            Throwable thrown = assertThrows(IllegalArgumentException.class, () ->
                 argumentNotEmpty(list, FIELD));
            assertEquals(EMPTY, thrown.getMessage());
        }

        @DisplayName("Collection Null Fail")
        @Test
        public void collectionNull(){
            ArrayList<String> list = null;
            Throwable thrown = assertThrows(IllegalArgumentException.class, () ->
                 argumentNotEmpty(list, FIELD));
            assertEquals(NULL, thrown.getMessage());
        }

        @DisplayName("Array Pass")
        @Test
        public void arrayFilled(){
            String[] list = new String[]{"abc"};
            assertDoesNotThrow(() -> argumentNotEmpty(list, FIELD));
        }

        @DisplayName("Array Empty Fail")
        @Test
        public void arrayEmpty(){
            String[] list = new String[0];
            Throwable thrown = assertThrows(IllegalArgumentException.class, () ->
                 argumentNotEmpty(list, FIELD));
            assertEquals(EMPTY, thrown.getMessage());
        }

        @DisplayName("Array Null Fail")
        @Test
        public void arrayNull(){
            String[] list = null;
            Throwable thrown = assertThrows(IllegalArgumentException.class, () ->
                 argumentNotEmpty(list, FIELD));
            assertEquals(NULL, thrown.getMessage());
        }

        @DisplayName("String Pass")
        @Test
        public void stringFilled(){
            String text = "abc";
            assertDoesNotThrow(() -> argumentNotEmpty(text, FIELD));
        }

        @DisplayName("String Empty Fail")
        @Test
        public void stringEmpty(){
            String text = "";
            Throwable thrown = assertThrows(IllegalArgumentException.class, () ->
                 argumentNotEmpty(text, FIELD));
            assertEquals(EMPTY, thrown.getMessage());
        }

        @DisplayName("String Null Fail")
        @Test
        public void stringNull(){
            String text = null;
            Throwable thrown = assertThrows(IllegalArgumentException.class, () ->
                 argumentNotEmpty(text, FIELD));
            assertEquals(NULL, thrown.getMessage());
        }
    }

    @Nested
    @DisplayName("Null And Equal Assertions")
    class NullEqual{

        @Test
        @DisplayName("Method `argumentNotNull`: Pass")
        public void notNullPass(){
            assertDoesNotThrow(() -> argumentNotNull("abc", FIELD));
        }

        @Test
        @DisplayName("Method  `argumentNotNull`: Fail")
        public void notNullFail(){
            Throwable thrown = assertThrows(IllegalArgumentException.class, () ->
                argumentNotNull(null, FIELD));
            assertEquals(ERROR + " is null.", thrown.getMessage());
        }

        @Test
        @DisplayName("Method `argumentSame`: Pass")
        public void samePass(){
            String answer = "abc";
            assertDoesNotThrow(() -> argumentSame(answer, FIELD, answer));
        }

        @Test
        @DisplayName("Method `argumentSame`: Equal not same")
        public void sameEqualButFail(){
            String answer = "abc";
            Throwable thrown = assertThrows(IllegalArgumentException.class, () ->
                argumentSame("1abc".substring(1), FIELD, answer));
            assertEquals(ERROR + " is not the same as <abc>.", thrown.getMessage());
        }

        @Test
        @DisplayName("Method `argumentSame`: Unexpected null.")
        public void sameUnexpectedNull(){
            String answer = "abc";
            Throwable thrown = assertThrows(IllegalArgumentException.class, () ->
                argumentSame(null, FIELD, answer));
            assertEquals(ERROR + " is null.", thrown.getMessage());
        }

        @Test
        @DisplayName("Method `argumentSame`: Expected null.")
        public void sameNullExpectedPass(){
            assertDoesNotThrow(() -> argumentSame(null, FIELD, null));
        }

        @Test
        @DisplayName("Method `argumentSame`: Unexpected value.")
        public void sameUnexpectedValuePass(){
            Throwable thrown = assertThrows(IllegalArgumentException.class, () ->
                argumentSame("Hello", FIELD, null));
            assertEquals(ERROR + " is not the same as <null>.", thrown.getMessage());
        }

        /// TODO more assertion tests
    }

}

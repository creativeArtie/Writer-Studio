package com.creativeartie.writer.pdf.value;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Number Rounding")
public class NumberRoundingTest {
    @DisplayName("Round Scaling")
    @ParameterizedTest(name = "Round Scaling: {1}")
    @CsvSource({
        "'1',   1",   "'200',   220",  "'500',     499",
        "'500', 500", "'2,000', 1923", "'200,000', 233344"
       })
    public void testScaling(String expect, int input){
        assertEquals(expect, Utilities.round(input));
    }

    @DisplayName("Thousands Rounding")
    @ParameterizedTest(name = "Rouding Thousands: {1}")
    @CsvSource({
        "'< 1,000', 1",     "'< 1,000', 46", "'< 1,000', 499",
        "'1,000', 500",     "'1,000', 1001", "'1,000', 1499",
        "'2,000', 1500",    "'2,000', 1923", "'2,000', 2130",
        "'233,000', 233344"
       })
    public void testThousands(String expect, int input){
        assertEquals(expect, Utilities.round(input, 3));
    }

    @DisplayName("Tens Rounding: 433")
    @Test
    public void testTen(){
        assertEquals("430", Utilities.round(433, 1));
    }
}

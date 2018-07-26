package com.creativeartie.writerstudio.export;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MockTests{

    static ExportContentText<Integer> contentExporter(String text){
        return new ExportContentText<>(new MockBridgeContent(text),
            DataLineType.DEFAULT, MockFactoryRender.FACTORY.getRenderContent()
        );
    }


    static ExportDivisionText<Integer> divisionExporter(String ... text){
        return new ExportDivisionText<>(new MockBridgeDivision(text),
            MockFactoryRender.FACTORY.getRenderDivision()
        );
    }

    @Nested
    @DisplayName("Split Content (smallest units)")
    public class ContentSplitTest{

        @Test
        public void empty(){
            ExportContentText<Integer> text = contentExporter("");
            Optional<ExportContentText<Integer>> overflow = text.split(100);
            assertFalse(overflow.isPresent(),
                () -> "Unexpected: " + overflow);
            assertEquals("", text.getText());
            assertTrue(text.isEmpty());
        }

        @Test
        public void noSplit(){
            ExportContentText<Integer> text = contentExporter("Hello");
            Optional<ExportContentText<Integer>> overflow = text.split(100);
            assertFalse(overflow.isPresent(),
                () -> "Unexpected: " + overflow);
            assertEquals("Hello", text.getText());
            assertFalse(text.isEmpty());
        }

        @Test
        public void noSplitCallTwice(){
            ExportContentText<Integer> text = contentExporter("Hello");
            Optional<ExportContentText<Integer>> overflow = text.split(100);
            assertFalse(text.split(100).isPresent(), "Filled");

            assertEquals("Hello", text.getText());
            assertFalse(text.isEmpty());
        }

        @Test
        public void split(){
            /// -----------------------------------------------00000000001111111
            /// -----------------------------------------------01234567890123456
            ExportContentText<Integer> text = contentExporter("Hello World Song");
            Optional<ExportContentText<Integer>> overflow = text.split(6);
            assertTrue(overflow.isPresent(), "Empty");
            assertEquals(" World Song", overflow.get().getText());
            assertEquals("Hello", text.getText());
            assertFalse(text.isEmpty());
        }

        @Test
        public void notFit(){
            /// -----------------------------------------------00000000001111111
            /// -----------------------------------------------01234567890123456
            ExportContentText<Integer> text = contentExporter("Hello World Song");
            Optional<ExportContentText<Integer>> overflow = text.split(3);
            assertTrue(overflow.isPresent(), "Empty");
            assertEquals("Hello World Song", overflow.get().getText());
            assertEquals("", text.getText());
            assertTrue(text.isEmpty());
        }

        @Test
        public void notFitAtAll(){
            /// -----------------------------------------------00000000001111111
            /// -----------------------------------------------01234567890123456
            ExportContentText<Integer> text = contentExporter("Hello World Song");
            Optional<ExportContentText<Integer>> overflow = text.split(0);
            assertTrue(overflow.isPresent(), "Empty");
            assertEquals("Hello World Song", overflow.get().getText());
            assertEquals("", text.getText());
            assertTrue(text.isEmpty());
        }
    }

    @Nested
    @DisplayName("Division (Line) Splitting test.")
    public class DivisionSplitTest{

        @Test
        public void empty(){
            ExportDivisionText<Integer> lines = divisionExporter("");
            assertEquals(1, lines.size());
            MockBridgeDivision.test(new String[][]{{}}, lines);
        }

        @Test
        public void basic(){
            ExportDivisionText<Integer> lines = divisionExporter("Hello World!");
            assertEquals(1, lines.size());
            MockBridgeDivision.test(new String[][]{{"Hello World!"}}, lines);
        }

        @Test
        public void appendNoSpace(){
            ExportDivisionText<Integer> lines = divisionExporter("Hello",
                "World!");
            assertEquals(1, lines.size());
            MockBridgeDivision.test(new String[][]{{"Hello", "World!"}}, lines);
        }

        @Test
        public void appendSplit(){
            ExportDivisionText<Integer> lines = divisionExporter("Hello World!",
                "Next Span!");
            assertEquals(2, lines.size());
            MockBridgeDivision.test(new String[][]{
            /// =0000000000111====11111112222
            /// =0123456789012====34567890123
                {"Hello World!", "Next"},
                {"Span!"}
            }, lines);
        }

        @Test
        public void startSplit(){
            ExportDivisionText<Integer> lines = divisionExporter(
                "Hello World! Next Span!");
            assertEquals(2, lines.size());
            MockBridgeDivision.test(new String[][]
            {
            /// =000000000011111111112222
            /// =012345678901234567890123
                {"Hello World! Next"},
                {"Span!"}
            }, lines);
        }

        @Test
        public void multipleSplit(){
            ExportDivisionText<Integer> lines = divisionExporter(
                "Twinkle, twinkle, little star/ " +
                "How I wonder what you are!/ " +
                "Up above the world so high,/ " +
                "Like a diamond in the sky");
            assertEquals(4, lines.size());
            MockBridgeDivision.test(new String[][]
            {
            /// =000000000011111111112
            /// =012345678901234567890 <- 1
               {"Twinkle, twinkle,"},
            /// =00000000001111111111222222222233333333334
            /// =01234567890123456789012345678901234567890 <- 2
               {"little star/ How I wonder what you"},
            /// =00000000001111111111222222222233333333334
            /// =01234567890123456789012345678901234567890 <- 3
               {"are!/ Up above the world so high,/ Like"},
            /// =00000000001111111111222222222233333333334
            /// =01234567890123456789012345678901234567890 <- 4
               {"a diamond in the sky"}
            }, lines);
        }

        @Test
        public void multipleContentSplit(){
            ExportDivisionText<Integer> lines = divisionExporter(
                "Twinkle, twinkle, little star/ ",
                "How I wonder what you are!/ ",
                "Up above the world so high,/ ",
                "Like a diamond in the sky");
            assertEquals(4, lines.size());
            MockBridgeDivision.test(new String[][]
            {
            /// =000000000011111111112
            /// =012345678901234567890 <- 1
               {"Twinkle, twinkle,"},
            /// =000000000011111====11111222222222233333333334
            /// =012345678901234====56789012345678901234567890 <- 2
               {"little star/", "How I wonder what you are!/"},
            /// =0000000000111111111122222222====2233333333334
            /// =0123456789012345678901234567====8901234567890 <- 3
               {"Up above the world so high,/", "Like a"},
            /// =00000000001111111111222222222233333333334
            /// =01234567890123456789012345678901234567890 <- 4
               {"diamond in the sky"}
            }, lines);
        }

    }

}

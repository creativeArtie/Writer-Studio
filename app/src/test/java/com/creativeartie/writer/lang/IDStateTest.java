package com.creativeartie.writer.lang;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.stream.*;

import com.creativeartie.writer.lang.markup.*;

@DisplayName("Catalogue Status Tester")
public class IDStateTest {

    private static class Tester extends IDParameterMethodSource{

        protected String getIdText(){
            return "!^abc:text\n";
        }
        protected String getRefText(){
            return "{^abc}\n";
        }
    }

    public static Stream<Arguments> provideText(){
        return Tester.provideText(new Tester());
    }

    @ParameterizedTest(name = "{0}: {2}")
    @MethodSource("provideText")
    public void test(CatalogueStatus expected, String text, boolean[] ids) {
        WritingText doc = new WritingText(text);
        IDBuilder builder = new IDBuilder();
        IDAssert asserters = new IDAssert();
        boolean isFirst = true;
        for (boolean id: ids){
            builder.reset().addCategory("foot").setId("abc");
            if (isFirst){
                /// Add id/ref for the first span
                if (id){
                    asserters.addId(builder, 0, expected);
                } else {
                    asserters.addRef(builder, 0, expected);
                }
                isFirst = false;
            } else {
                if (id){
                    asserters.addId(builder);
                } else {
                    asserters.addRef(builder);
                }
            }
        }
        asserters.assertIds(doc);
    }
}

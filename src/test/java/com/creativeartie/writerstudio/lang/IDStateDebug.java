package com.creativeartie.writerstudio.lang;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import com.creativeartie.writerstudio.lang.*;

public class IDStateDebug extends IDParamTest{
	private class Tester extends IDParameterMethodSource{
		protected String getIdText(){
			return "!^abc:text\n";
		}
		protected String getRefText(){
			return "{!^abc}";
		}
	}
	
    @ParameterTest
    public void test(CatalogueStatus status, String text) {
		WritingText text = new WritingText(text);
        IDBuilder builder = new IDBuilder();
        boolean isFirst = true;
        for (IDParamTest.States state: input){
            builder.reset().setId("abc");
            if (isFirst){
                switch(state){
                case ID:
                    doc.addId(builder, expected, 0);
                    break;
                case REF: 
                    doc.addRef(builder, expected, 0);
                }
                isFirst = false;
            } else {
                switch(state){
                case ID:
                    doc.addId(builder);
                    break;
                case REF:
                    doc.addRef(builder);
                }
            }
        }
        doc.assertIds();
    }
}

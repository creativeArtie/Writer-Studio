package com.creativeartie.writerstudio.export;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;

public class RenderTest {
    private class RenderTester implements RenderDivsion<Integer>{
        public void render(Division<Integer> divsion){}
    }

    private static Document buildDocument(String text){
        return new Document(text, AuxilaryParsers.getParagraphParser());
    }
}

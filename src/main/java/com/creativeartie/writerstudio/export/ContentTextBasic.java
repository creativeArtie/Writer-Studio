package com.creativeartie.writerstudio.export;

import com.creativeartie.writerstudio.lang.markup.*;

/** Draws contents on the page */
class ContentTextBasic<T extends Number> extends ContentText<T>{

    private String printText;

    ContentTextBasic(RenderContentFont<T> font, FormatSpanContent span){
        super(font, span);
        printText = span.getRendered();
    }

    private ContentTextBasic(ContentTextBasic copy, String text){
        super(copy);
        printText = text;
    }

    @Override
    String getText(){
        return printText;
    }

    ContentTextBasic<T> splitText(String text){
    }
}

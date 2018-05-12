package com.creativeartie.writerstudio.lang.markup;


/** Text contains escape character at the end of text. */
public class TextAreaLineException extends Exception{

    private static final long serialVersionUID = 0101l;

    private String rawText;

    /** Creates a text exception. */
    TextAreaLineException(String raw){
        super("Line can not ends with an escape slash character: " + raw);
        rawText = raw;
    }

    public String getRawText(){
        return rawText;
    }
}
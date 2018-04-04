package com.creativeartie.writerstudio.export;

import java.io.*;
import java.util.*;
import java.awt.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.export.value.*;

public class DivisionTextNote extends DivisionTextFormatted{

    private ContentFont superFont;

    public DivisionTextNote(SectionContent<?> content){
        super(content);
        superFont = content.newFont().changeToSuperscript();
    }

    public DivisionTextNote setNumbering(String text) throws IOException{
        if (isEmpty() || get(0).isEmpty()){
            appendSimpleText(text, superFont);
            return this;
        }
        if (get(0).get(0).getFont().equals(superFont)){
            get(0).get(0).setText(text);
        }
        return this;
    }
}
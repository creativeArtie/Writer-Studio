package com.creativeartie.writerstudio.export.markup;

import java.util.*;
import java.util.function.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.export.*;

abstract class Format<T extends FormatSpan> implements BridgeContent{

    private ArrayList<Consumer<String>> textListeners;
    private T contentSpan;

    Format(T span){
        contentSpan = span;
        textListeners = new ArrayList<>();
    }

    @Override
    public boolean isBold(){
        return contentSpan.isBold();
    }

    @Override
    public boolean isItalics(){
        return contentSpan.isItalics();
    }

    @Override
    public boolean isUnderline(){
        return contentSpan.isUnderline();
    }

    @Override
    public boolean isCoded(){
        return contentSpan.isCoded();
    }

    protected void textEdited(){
        String text = getText();
        for (Consumer<String> listener: textListeners){
            listener.accept(text);
        }
    }

    T getSpan(){
        return contentSpan;
    }


    @Override
    public boolean isEquals(BridgeContent content){
        if (content instanceof Format){
            return contentSpan.equals(((Format)content).contentSpan);
        }
        return false;
    }
}

package com.creativeartie.jwriter.window;

import java.util.*;

import org.fxmisc.richtext.*;

import com.google.common.base.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.property.*;
import com.creativeartie.jwriter.property.window.*;

public class PaneTextControl extends PaneTextView {

    @Override
    public void loadDoc(ManuscriptDocument doc){
        getTextArea().replaceText(0, getTextArea().getLength(),
            doc.getRaw());
    }

    @Override
    public void updateCss(final ManuscriptDocument doc){
        doc.getLeaves().forEach(leaf -> getTextArea().setStyle(
            leaf.getStart(), leaf.getEnd(), LeafStyleParser.DISPLAY.toCss(leaf)
        ));
    }

    @Override
    public void moveTo(int position){
        if (position == getTextArea().getLength()){
            getTextArea().moveTo(position);
        } else {
            getTextArea().moveTo(position - 1);
        }
    }

    @Override
    public void moveTo(Span span){
        int end = span.getEnd();
        if (end == getTextArea().getLength()){
            getTextArea().moveTo(end);
        } else {
            getTextArea().moveTo(end - 1);
        }
    }

    public void returnFocus(){
        getTextArea().requestFollowCaret();
        getTextArea().requestFocus();
    }
}
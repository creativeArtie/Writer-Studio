package com.creativeartie.jwriter.window;

import java.util.*;

import org.fxmisc.richtext.*;

import com.google.common.base.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.property.*;
import com.creativeartie.jwriter.property.window.*;

class TextPaneControl extends TextPaneView {

    @Override
    public void loadDoc(WritingText doc){
        getTextArea().replaceText(0, getTextArea().getLength(),
            doc.getRaw());
    }

    @Override
    public void updateCss(final WritingText doc){
        doc.getLeaves().forEach(leaf -> getTextArea().setStyle(
            leaf.getStart(), leaf.getEnd(), LeafStyleParser.SCREEN.toCss(leaf)
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

    @Override
    public WindowText setNextMode(WindowText last){
        WindowText ans = null;
        if (last == null){
            ans = WindowText.SYNTAX_MODE;
        } else {
            switch (last){
                case SYNTAX_MODE:
                    ans = WindowText.PARSED_MODE;
                    break;
                case PARSED_MODE:
                    ans = WindowText.SYNTAX_MODE;
                    break;
            }
        }
        getViewModeButton().setText(ans.getText());
        return ans;
    }
}
package com.creativeartie.writerstudio.javafx;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.beans.binding.*;

import java.util.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.main.*;
import com.creativeartie.writerstudio.resource.*;

/**
 * Controller for the Cheatsheet Pane
 *
 * @see CheatsheetPaneView
 */
class CheatsheetPaneControl extends CheatsheetPaneView{

    private WritingText writingText;
    private int caretPosition;

    public void setWritingText(ReadOnlyObjectProperty<WritingText> prop){
        prop.addListener((d, o, n) -> {
            writingText = n;
            updateLabels();
        });
    }

    public void setCaretPosition(ReadOnlyIntegerProperty prop){
        prop.addListener((d, o, n) -> {
            caretPosition = n.intValue();
            updateLabels();
        });
    }

     /** Updates the labels base on the cursor movements. */
    public void updateLabels(){
        if (writingText == null ||
            ! writingText.getRange().contains(caretPosition) ||
            writingText.getEnd() != caretPosition
        ){
            return;
        }
        for (CheatsheetLabel label: getLabels()){
            label.updateLabelStatus(writingText, caretPosition);
        }
    }
}

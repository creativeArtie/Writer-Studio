package com.creativeartie.writerstudio.fxgui;

import java.util.*;

import com.creativeartie.writerstudio.lang.markup.*;

/**
 * Controller for the Cheatsheet Pane
 *
 * @see CheatsheetPaneView
 */
final class CheatsheetPaneControl extends CheatsheetPaneView{

    protected void addBindings(){
        writingTextProperty().addListener(
            (d, o, n) -> updateLabels(n, getCaretPosition())
        );
        caretPositionProperty().addListener(
            (d, o, n) -> updateLabels(getWritingText(), n.intValue())
        );
    }

     /** Updates the labels base on the cursor movements. */
    private void updateLabels(WritingText doc, int position){
        for (CheatsheetLabel label: getLabels()){
            label.updateLabelStatus(doc, position);
        }
    }
}

package com.creativeartie.writer.javafx;

import com.creativeartie.writer.javafx.utils.*;
import com.creativeartie.writer.lang.markup.*;

/**
 * Controller for the Cheatsheet Pane
 *
 * @see CheatsheetPaneView
 */
class CheatsheetPaneControl extends CheatsheetPaneView{

    /// %Part 1: Private Fields and Constructor

    private WritingText writingText;
    private int caretPosition;

    /// %Part 2: Property Binding
    /// %Part 3: Bind Children Properties

    protected void bindChildren(WriterSceneControl control){
        control.writingTextProperty().addListener((d, o, n) -> listenWritingText(n));

        control.getTextPane().getTextArea().caretPositionProperty().addListener(
            (d, o, n) -> listenPosition(n.intValue())
        );
    }

    /// %Part 3.1: control.writingTextProperty()

    private void listenWritingText(WritingText text){
        writingText = text;
        if (text != null) showStatus();
    }

    /// %Part 3.2: control.caretPositionProperty()

    private void listenPosition(int position){
        caretPosition = position;
        if (writingText != null) showStatus();
    }

    /// %Part 3: Utilities

     /** Updates the labels base on the cursor movements. */
    private void showStatus(){
        if (writingText.getRange().contains(caretPosition) ||
            writingText.getEnd() == caretPosition
        ){
            for (HintLabel label: getHintLabels()){
                label.showStatus(writingText, caretPosition);
            }
        }
    }
}

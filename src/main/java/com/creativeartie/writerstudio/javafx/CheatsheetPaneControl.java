package com.creativeartie.writerstudio.javafx;

import com.creativeartie.writerstudio.lang.markup.*;

/**
 * Controller for the Cheatsheet Pane
 *
 * @see CheatsheetPaneView
 */
class CheatsheetPaneControl extends CheatsheetPaneView{

    private WritingText writingText;
    private int caretPosition;

    /// %Part 1: setupChildren

    protected void setupChildern(WriterSceneControl control){
        control.writingTextProperty().addListener((d, o, n) -> loadText(n));

        control.getTextPane().getTextArea().caretPositionProperty().addListener(
            (d, o, n) -> loadPosition(n.intValue())
        );
    }

    /// %Part 1.1: WriterSceneControl#writingTextProperty()

    private void loadText(WritingText text){
        writingText = text;
        if (text != null) showStatus();
    }

    /// %Part 1.2: WriterSceneControl#caretPositionProperty()

    private void loadPosition(int position){
        caretPosition = position;
        if (writingText != null) showStatus();
    }

    /// %Part 2: Utilities

     /** Updates the labels base on the cursor movements. */
    private void showStatus(){
        if (writingText.getRange().contains(caretPosition) ||
            writingText.getEnd() == caretPosition
        ){
            for (CheatsheetLabel label: getHintLabels()){
                label.showStatus(writingText, caretPosition);
            }
        }
    }
}

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

    protected void setupChildern(WriterSceneControl control){
        control.writingTextProperty().addListener((d, o, n) -> loadText(n));

        control.getTextPane().getTextArea().caretPositionProperty().addListener(
            (d, o, n) -> setPosition(n.intValue())
        );
    }

    /// %Part 1: WriterSceneControl#writingTextProperty()

    private void loadText(WritingText text){
        writingText = text;
        if (text != null) updateLabels();
    }

    /// %Part 2: WriterSceneControl#caretPositionProperty()

    private void setPosition(int position){
        caretPosition = position;
        if (writingText != null) updateLabels();
    }

     /** Updates the labels base on the cursor movements. */
    public void updateLabels(){
        if (writingText.getRange().contains(caretPosition) ||
            writingText.getEnd() == caretPosition
        ){
            for (CheatsheetLabel label: getHintLabels()){
                label.updateLabelStatus(writingText, caretPosition);
            }
        }
    }
}

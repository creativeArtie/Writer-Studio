package com.creativeartie.writerstudio.fxgui;

import javafx.stage.*;

import com.creativeartie.writerstudio.lang.markup.*;

public class WriterSceneControl extends WriterSceneView {

    public WriterSceneControl(Stage window){
        super(window);
    }

    @Override
    protected void addListeners(Stage window){
        getTextPane().writingTextProperty().bind(writingTextProperty());
        getTextPane().writingStatProperty().bind(writingStatProperty());

        getCheatsheetPane().writingTextProperty().bind(writingTextProperty());
        getCheatsheetPane().caretPositionProperty().bind(caretPositionProperty());

        window.setOnShowing(e -> getTextPane().returnFocus());
    }
}

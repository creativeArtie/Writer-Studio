package com.creativeartie.writerstudio.fxgui;

import javafx.stage.*;

import com.creativeartie.writerstudio.lang.markup.*;

public class WriterSceneControl extends WriterSceneView {

    public WriterSceneControl(Stage window){
        super(window);
    }


    @Override
    protected void addListeners(Stage window){
        for (TableDataControl<?> tab: getTableTabs()){
            tab.writingTextProperty().bind(writingTextProperty());
            tab.caretPositionProperty().bind(caretPositionProperty());
            tab.textReadyProperty().bind(textReadyProperty());
            tab.itemSelectedProperty().addListener((d, o, n) -> setLastSelected(n));
            tab.changedCountProperty().addListener((d, o, n) -> returnFocus());
        }

        getTextPane().writingTextProperty().bind(writingTextProperty());
        getTextPane().writingStatProperty().bind(writingStatProperty());
        getTextPane().lastSelectedProperty().bind(lastSelectedProperty());

        getCheatsheetPane().writingTextProperty().bind(writingTextProperty());
        getCheatsheetPane().caretPositionProperty().bind(caretPositionProperty());

        window.setOnShowing(e -> getTextPane().returnFocus());

    }

    public void returnFocus(){
        System.out.println("returnFocus()");
        getTextPane().returnFocus();
    }
}

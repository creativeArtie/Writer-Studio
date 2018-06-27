package com.creativeartie.writerstudio.fxgui;

import javafx.beans.binding.*;
import javafx.scene.*;
import javafx.stage.*;

import com.creativeartie.writerstudio.lang.markup.*;

public class WriterSceneControl extends WriterSceneView {

    public WriterSceneControl(Stage window){
        super(window);
    }

    @Override
    protected void addBindings(Stage window){
        window.sceneProperty().addListener((p, o, n) -> prepFoucsListener(n));
        window.setOnShowing(e -> getTextPane().returnFocus());

        /// writingFile
        getWritingFileProperty().bind(getMainMenuBar().getWritingFileProperty());
        /// writingText
        getWritingTextProperty().bind(Bindings.createObjectBinding(
            this::loadWritingText, getWritingFileProperty()
        ));
        /// writingStat
        getWritingStatProperty().bind(Bindings.createObjectBinding(
            this::loadWritingStat, getWritingFileProperty()
        ));
        /// textReady
        getTextReadyProperty().bind(getTextPane().getTextReadyProperty());
        /// caretPosition
        getCaretPositionProperty().bind(getTextPane().getCaretPositionProperty());

        // TODO bind refocusText


        getNoteCardPane().getWritingTextProperty().bind(getWritingTextProperty());
        getNoteCardPane().getGoToNoteProperty().addListener((d, o, n) -> setLastSelected(n));

        for (TableDataControl<?> tab: getTableTabs()){
            tab.getWritingTextProperty().bind(getWritingTextProperty());
            tab.getCaretPositionProperty().bind(getCaretPositionProperty());
            tab.getTextReadyProperty().bind(getTextReadyProperty());
            tab.getItemSelectedProperty().addListener((d, o, n) -> setLastSelected(n));
        }

        getTextPane().getWritingTextProperty().bind(getWritingTextProperty());
        getTextPane().getWritingStatProperty().bind(getWritingStatProperty());
        getTextPane().getLastSelectedProperty().bind(getLastSelectedProperty());

        getCheatsheetPane().getWritingTextProperty().bind(getWritingTextProperty());
        getCheatsheetPane().getCaretPositionProperty().bind(getCaretPositionProperty());
    }

    private WritingText loadWritingText(){
        WritingFile file = getWritingFile();
        return file == null? null: file.getDocument();
    }

    private WritingStat loadWritingStat(){
        WritingFile file = getWritingFile();
        return file == null? null: file.getRecords();
    }

    private void prepFoucsListener(Scene scene){
        if (scene != null){
            scene.focusOwnerProperty().addListener((p, o, n) -> refocusText(n));
        }
    }

    private void refocusText(Node owner){
        // System.out.println(owner);
        if (isRefocusText()){
            setRefocusText(false);
        }
    }

    public void returnFocus(){
        getTextPane().returnFocus();
    }
}

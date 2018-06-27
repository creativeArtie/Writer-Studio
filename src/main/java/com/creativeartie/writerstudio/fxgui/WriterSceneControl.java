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

        getWritingFileProperty().bind(getMainMenuBar().writingFileProperty());
        getWritingTextProperty().bind(Bindings.createObjectBinding(
            this::loadWritingText, getWritingFileProperty()
        ));
        getWritingStatProperty().bind(Bindings.createObjectBinding(
            this::loadWritingStat, getWritingFileProperty()
        ));

        getCaretPositionProperty().bind(getTextPane().caretPositionProperty());
        getTextReadyProperty().bind(getTextPane().textReadyProperty());

        // TODO bind refocusText


        getNoteCardPane().writingTextProperty().bind(writingTextProperty());
        getNoteCardPane().goToNoteProperty().addListener((d, o, n) -> setLastSelected(n));

        for (TableDataControl<?> tab: getTableTabs()){
            tab.writingTextProperty().bind(writingTextProperty());
            tab.caretPositionProperty().bind(caretPositionProperty());
            tab.textReadyProperty().bind(textReadyProperty());
            tab.itemSelectedProperty().addListener((d, o, n) -> setLastSelected(n));
        }

        getTextPane().writingTextProperty().bind(writingTextProperty());
        getTextPane().writingStatProperty().bind(writingStatProperty());
        getTextPane().lastSelectedProperty().bind(lastSelectedProperty());

        getCheatsheetPane().writingTextProperty().bind(writingTextProperty());
        getCheatsheetPane().caretPositionProperty().bind(caretPositionProperty());

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
        System.out.println(owner);
        if (isRefocusText()){
            setRefocusText(false);
        }
    }

    public void returnFocus(){
        getTextPane().returnFocus();
    }
}

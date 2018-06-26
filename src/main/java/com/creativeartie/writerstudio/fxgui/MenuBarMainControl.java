package com.creativeartie.writerstudio.fxgui;

import javafx.application.*;
import javafx.stage.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

class MenuBarMainControl extends MenuBarMainView{

    private FileChooser fileChooser;
    private Stage mainWindow; /// For fileChooser dialogs

    public MenuBarMainControl(Stage window){
        super(window);

        fileChooser = new FileChooser();
        fileChooser.setTitle(WindowText.MENU_CHOOSER_TITLE.getText());
        mainWindow = window;
    }

    @Override
    protected void addListeners(){
        getCreateItem().setOnAction(e -> newFile());
        // getOpenItem().setOnAction(e -> openFile());
        // getExportItem().setOnAction(e -> exportFile());
        getExitItem().setOnAction(e -> exit());
    }

    private void newFile(){
        setWritingFile(WritingFile.newFile());
    }

    private void exit(){
        try {
            WritingFile file = getWritingFile();/*
            if (file !+ null && file.canSave()){
                saveFile();
            }*/
            Platform.exit();
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}

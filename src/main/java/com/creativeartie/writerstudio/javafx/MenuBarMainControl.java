package com.creativeartie.writerstudio.javafx;

import java.io.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.stage.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.pdf.*;

class MenuBarMainControl extends MenuBarMainView{

    private WindowAbout aboutWindow;
    private FileChooser fileChooser;
    private Stage mainWindow; /// For fileChooser dialogs

    public MenuBarMainControl(Stage window){
        super(window);
        aboutWindow = new WindowAbout();

        fileChooser = new FileChooser();
        fileChooser.setTitle(WindowText.MENU_CHOOSER_TITLE.getText());
        mainWindow = window;
    }

    @Override
    protected void setupChildern(WriterSceneControl control){
        getCreateItem().setOnAction(e -> newFile());
        getOpenItem().setOnAction(e -> openFile());
        getExportItem().setOnAction(e -> exportPdf());
        getAboutItem().setOnAction(evt -> aboutWindow.show());
        getExitItem().setOnAction(e -> exit());
    }

    private void newFile(){
        setWritingFile(WritingFile.newFile());
    }

    private void exportPdf(){
        WritingFile writing = getWritingFile();
        if (writing != null){
            File file = fileChooser.showSaveDialog(mainWindow);
            try (WritingExporter out = new WritingExporter(file.getAbsolutePath())){
                out.export(writing);
            } catch (Exception ex){
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Undescriptive Error (Sorry)");
                error.setHeaderText("This is a temperatory solution for invalid markup format.");
                error.setContentText(ex.toString());
                error.showAndWait();
            }
        }
    }

    private void openFile(){
        File file = fileChooser.showOpenDialog(mainWindow);
        if (file != null){
            try {
                setWritingFile(WritingFile.open(file));
            } catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }
    }

    private void exit(){
        try {
            WritingFile file = getWritingFile();
            if (file != null && file.canSave()){
                // saveFile();
            }
            Platform.exit();
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}

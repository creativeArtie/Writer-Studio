package com.creativeartie.writerstudio.javafx;

import java.io.*;
import javafx.application.*;
import javafx.scene.control.*;
import javafx.stage.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.pdf.*;

import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    MenuBarConstants.*;

class MenuBarMainControl extends MenuBarMainView{

    /// %Part 1: Private Fields and Constructor

    private WindowAbout aboutWindow;
    private WindowStatControl statsWindow;
    private FileChooser fileChooser;
    private Stage mainWindow; /// For fileChooser dialogs

    public MenuBarMainControl(Stage window){
        super(window);
        aboutWindow = new WindowAbout();
        statsWindow = new WindowStatControl();
        fileChooser = new FileChooser();
        fileChooser.setTitle(OPEN_FILE);
        mainWindow = window;
    }

    /// %Part 2: Property Binding
    /// %Part 3: Bind Children Properties

    @Override
    protected void bindChildren(WriterSceneControl control){
        statsWindow.postLoad(control);

        getCreateItem().setOnAction(e -> createFile());
        getOpenItem().setOnAction(e -> openFile());
        getExportItem().setOnAction(e -> exportPdf());
        getExitItem().setOnAction(e -> exit());
        getAboutItem().setOnAction(e -> aboutWindow.show());
        getGoalsItem().setOnAction(e -> statsWindow.show());
    }

    /// %Part 3.1 getCreateItem().setOnAction(...)

    private void createFile(){
        setWritingFile(WritingFile.newFile());
    }

    /// %Part 3.2 getOpenItem().setOnAction(...)
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

    /// %Part 3.3 getExportItem().setOnAction(...)
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

    /// %Part 3.4 getExitItem().setOnAction(...)

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

    /// %Part 3.5 getAboutItem().setOnAction(...) <- show window
    /// %Part 3.6 getGoalsItem().setOnAction(...) <- show window
}

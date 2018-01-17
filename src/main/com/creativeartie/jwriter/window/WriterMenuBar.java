package com.creativeartie.jwriter.window;

import java.util.*;
import java.io.*;
import java.util.Optional;
import javafx.scene.control.*;
import javafx.beans.property.*;
import javafx.stage.*;
import javafx.beans.binding.*;
import javafx.application.*;

import com.google.common.base.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.stats.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.property.*;
import com.creativeartie.jwriter.property.window.*;

class WriterMenuBar extends MenuBar{

    private Stage aboutWindow;
    private Stage statWindow;
    private Stage mainWindow;
    private FileChooser chooser;
    private SimpleObjectProperty<ManuscriptFile> manuscriptFile;

    public WriterMenuBar(Stage window){
        SceneStatsControl statTable = new SceneStatsControl();
        statWindow = SceneStatsView.createStage(statTable);
        aboutWindow = new WriterAboutWindow();

        chooser = new FileChooser();
        mainWindow = window;

        manuscriptFile = new SimpleObjectProperty<>(this, "manuscriptFile");
        statTable.statTableProperty().bind(Bindings.createObjectBinding(
            () -> Optional.ofNullable(manuscriptFile.getValue()).map(
                value -> value.getRecords()
            ).orElse(null), manuscriptFile));

        Menu file = new Menu(WindowText.MENU_FILE.getText());
        MenuItem create = new MenuItem(WindowText.MENU_FILE_NEW.getText());
        create.setOnAction(evt -> newFile());
        MenuItem open = new MenuItem(WindowText.MENU_FILE_OPEN.getText());
        open.setOnAction(evt -> openFile());
        MenuItem save = new MenuItem(WindowText.MENU_FILE_SAVE.getText());
        save.setOnAction(evt -> saveFile());
        MenuItem exit = new MenuItem(WindowText.MENU_FILE_EXIT.getText());
        exit.setOnAction(evt -> exit());
        file.getItems().addAll(create, open, save, new SeparatorMenuItem(),
            exit);

        Menu stats = new Menu(WindowText.MENU_STATS.getText());
        MenuItem goals = new MenuItem(WindowText.MENU_STATS_GOALS.getText());
        goals.setOnAction(evt -> statWindow.show());
        stats.getItems().addAll(goals);

        Menu help = new Menu(WindowText.MENU_HELP.getText());
        MenuItem about = new MenuItem(WindowText.MENU_HELP_ABOUT.getText());
        about.setOnAction(evt -> aboutWindow.show());
        help.getItems().addAll(about);

        getMenus().addAll(file, stats, help);
    }

    public ObjectProperty<ManuscriptFile> manuscriptFileProperty(){
        return manuscriptFile;
    }

    public ManuscriptFile getManuscriptFile(){
        return manuscriptFile.getValue();
    }

    public void setManuscriptFile(ManuscriptFile file){
        manuscriptFile.setValue(file);
    }


    private void openFile(){
        File file = chooser.showOpenDialog(mainWindow);
        if (file != null){
            try {
                manuscriptFile.setValue(ManuscriptFile.open(file));
            } catch (Exception ex){
                System.err.println("unhandled exception (WriterMenuBar#openFile):");
                ex.printStackTrace();
                System.exit(-1);
            }
        }
    }

    private void newFile(){
        manuscriptFile.setValue(ManuscriptFile.newFile());
    }

    private void saveFile(){
        try {
            ManuscriptFile data = getManuscriptFile();
            if (data != null){
                if (! data.canSave()){
                    File file = chooser.showSaveDialog(mainWindow);
                    if (file == null){
                        return;
                    }
                    data.setSave(file);
                }
                data.save();
            }
        } catch (Exception ex){
            System.err.println("unhandled exception (WriterMenuBar#saveFile):");
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    private void exit(){
        try {
            if (getManuscriptFile().canSave()){
                saveFile();
            }
            Platform.exit();
        } catch (Exception ex){
            System.err.println("unhandled exception (WriterMenuBar#exit):");
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
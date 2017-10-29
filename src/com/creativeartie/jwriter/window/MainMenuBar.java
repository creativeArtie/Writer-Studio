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

public class MainMenuBar extends MenuBar{

    private String getString(String key){
        return Utilities.getString("MainMenu." + key);
    }

    private Stage statWindow;
    private Stage mainWindow;
    private FileChooser chooser;
    private SimpleObjectProperty<ManuscriptFile> manuscriptFile;

    public MainMenuBar(Stage window){
        SceneStatsControl statTable = new SceneStatsControl();
        statWindow = SceneStatsView.createStage(statTable);

        chooser = new FileChooser();
        mainWindow = window;

        manuscriptFile = new SimpleObjectProperty<>(this, "manuscriptFile");
        statTable.statTableProperty().bind(Bindings.createObjectBinding(
            () -> Optional.ofNullable(manuscriptFile.getValue()).map(
                value -> value.getRecords()
            ).orElse(null), manuscriptFile));

        Menu file = new Menu(getString("File"));
        MenuItem create = new MenuItem(getString("FileCreate"));
        create.setOnAction(evt -> newFile());
        MenuItem open = new MenuItem(getString("FileOpen"));
        open.setOnAction(evt -> openFile());
        MenuItem save = new MenuItem(getString("FileSave"));
        save.setOnAction(evt -> saveFile());
        MenuItem exit = new MenuItem(getString("FileExit"));
        exit.setOnAction(evt -> exit());
        file.getItems().addAll(create, open, save, new SeparatorMenuItem(),
            exit);

        Menu stats = new Menu(getString("Stats"));

        MenuItem goals = new MenuItem(getString("StatsGoal"));
        goals.setOnAction(evt -> statWindow.show());
        stats.getItems().addAll(goals);

        getMenus().addAll(file, stats);
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
                System.err.println("unhandled exception (MainMenuBar#openFile):");
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
            System.err.println("unhandled exception (MainMenuBar#saveFile):");
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
            System.err.println("unhandled exception (MainMenuBar#exite):");
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
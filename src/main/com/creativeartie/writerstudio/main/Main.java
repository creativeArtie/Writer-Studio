package com.creativeartie.writerstudio.main;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.fxml.*;
import java.util.*;
import java.io.*;

import com.creativeartie.writerstudio.file.*;
import com.creativeartie.writerstudio.stats.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.window.*;
import com.creativeartie.writerstudio.lang.markup.*;

public class Main extends Application{
    private static Stage mainStage;
    private static ManuscriptFile writeFile;

    public static void main(String[] args) {
        launch(args);
    }

    private static void killProgram(Thread t, Throwable e){
        try {
            e.printStackTrace();
            writeFile.dumpFile();
        } catch (Exception ex){
            /// An exception in default exception handling!!!
            ex.printStackTrace();
        } finally {
            mainStage.close();
            Platform.exit();
        }
    }

    @Override
    public void start(Stage stage) throws Exception{
        Thread.setDefaultUncaughtExceptionHandler(Main::killProgram);
        stage.setTitle(WindowText.PROGRAM_NAME.getText());
        mainStage = stage;
        setupWindow(ManuscriptFile.newFile());
    }

    private void setupWindow(ManuscriptFile file) {
        writeFile = file;
        WriterSceneControl writer = new WriterSceneControl(mainStage);
        Scene scene = new Scene(writer, 800, 600);
        writer.setManuscriptFile(file);
        writer.manuscriptFileProperty().addListener((data, oldValue, newValue)
            -> writeFile = newValue);
        mainStage.setScene(scene);
        mainStage.setMaximized(true);
        mainStage.show();
    }
}

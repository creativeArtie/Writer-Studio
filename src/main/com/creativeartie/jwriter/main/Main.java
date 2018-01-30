package com.creativeartie.jwriter.main;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.fxml.*;
import java.util.*;
import java.io.*;

import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.stats.*;
import com.creativeartie.jwriter.property.window.*;
import com.creativeartie.jwriter.window.*;
import com.creativeartie.jwriter.lang.markup.*;


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

        // testChildWindows();
        testMainWindow();
        // setupWindow(ManuscriptFile.newFile());
    }

    @Deprecated
    private void testMainWindow() throws Exception{
        File file = new File("data/sectionDebug3.txt");
        WritingText doc = new WritingText(file);
        setupWindow(ManuscriptFile.withManuscript(doc));
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

    @Deprecated
    private void testChildWindows() throws Exception{
        Button button1 = new Button("Stats");
        SceneStatsControl pane = new SceneStatsControl();
        pane.setStatTable(RecordList.build(new File("data/record3.txt")));
        Stage stats = SceneStatsControl.createStage(pane);
        button1.setOnAction(event -> stats.show());

        Button button2 = new Button("copyright");
        Stage about = new WriterAboutWindow();
        button2.setOnAction(event -> about.show());
        button2.setDefaultButton(true);

        Scene scene = new Scene(new FlowPane(button1, button2), 800, 600);
        mainStage.setScene(scene);
        mainStage.show();
    }
}

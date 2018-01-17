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
    private Stage mainStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle(WindowText.PROGRAM_NAME.getText());

        // testChildWindows(stage);
        testMainWindow(stage);
        // setupWindow(stage, ManuscriptFile.newFile());
    }

    @Deprecated
    private void testMainWindow(Stage stage) throws Exception{
        File file = new File("data/sectionDebug4.txt");
        WritingText doc = new WritingText(file);
        setupWindow(stage, ManuscriptFile.withManuscript(doc));
    }

    private void setupWindow(Stage stage, ManuscriptFile file) {
        WriterSceneControl writer = new WriterSceneControl(stage);
        Scene scene = new Scene(writer, 800, 600);
        writer.setManuscriptFile(file);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    @Deprecated
    private void testChildWindows(Stage stage) throws Exception{
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
        stage.setScene(scene);
        stage.show();
    }
}

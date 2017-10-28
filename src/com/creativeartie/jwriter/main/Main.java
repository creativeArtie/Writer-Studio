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
import com.creativeartie.jwriter.window.*;
import com.creativeartie.jwriter.lang.markup.*;

public class Main extends Application{
    private Stage mainStage;

    public final static ResourceBundle TEXTS = PropertyResourceBundle
        .getBundle("data.windowText", Locale.ENGLISH);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle(TEXTS.getString("MainWindow.Title"));

        // testStatsWindow(stage);
        // testMainWindow(stage);
        jarMainWindow(stage);
    }

    private void testMainWindow(Stage stage) throws Exception{
        setupWindow(stage, new File("data/help-text.txt"));
    }

    private void jarMainWindow(Stage stage) throws Exception{
        File classPath = new File(System.getProperty("java.class.path"));
        File file = new File(classPath.getParent(), "data/help-text.txt");
        setupWindow(stage, file);
    }

    private void setupWindow(Stage stage, File file) throws Exception{
        SceneWriterControl writer = new SceneWriterControl(stage);
        Scene scene = new Scene(writer, 800, 600);
        writer.setManuscriptFile(new ManuscriptFile(file));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private void testStatsWindow(Stage stage) throws Exception{
        Button button = new Button("Click");
        Scene scene = new Scene(new BorderPane(button), 800, 600);
        SceneStatsControl stats = new SceneStatsControl();
        stats.setStatTable(new RecordTable(new File("data/record3.txt")));
        Stage tmp = SceneStatsControl.createStage(stats);
        button.setOnAction(event -> tmp.show());
        button.setDefaultButton(true);
        stage.setScene(scene);
        stage.show();
    }
}

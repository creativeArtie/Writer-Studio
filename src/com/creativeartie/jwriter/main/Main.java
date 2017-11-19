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
        openMainWindow(stage);
    }

    private void openMainWindow(Stage stage) throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = getClass().getResourceAsStream("/data/help-text.txt");
        StringBuilder builder = new StringBuilder();
        int read = in.read();
        while (read != -1){
            builder.append((char) read);
            read = in.read();
        }
        setupWindow(stage, ManuscriptFile.newFile());
    }

    private void setupWindow(Stage stage, ManuscriptFile file) {
        SceneWriterControl writer = new SceneWriterControl(stage);
        Scene scene = new Scene(writer, 800, 600);
        writer.setManuscriptFile(file);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    @Deprecated
    private void testStatsWindow(Stage stage) throws Exception{
        Button button = new Button("Click");
        Scene scene = new Scene(new BorderPane(button), 800, 600);
        SceneStatsControl stats = new SceneStatsControl();
        stats.setStatTable(RecordList.build(new File("data/record3.txt")));
        Stage tmp = SceneStatsControl.createStage(stats);
        button.setOnAction(event -> tmp.show());
        button.setDefaultButton(true);
        stage.setScene(scene);
        stage.show();
    }
}

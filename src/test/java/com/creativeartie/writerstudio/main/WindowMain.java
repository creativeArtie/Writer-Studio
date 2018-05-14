package com.creativeartie.writerstudio.main;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.fxml.*;
import java.util.*;
import java.io.*;

import com.creativeartie.writerstudio.stats.*;
import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.window.*;
import com.creativeartie.writerstudio.lang.markup.*;

public class WindowMain extends Main{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        super.start(stage);
        // testChildWindows(stage);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected WritingFile getStartFile() throws IOException{
        // File file = new File("data/sectionDebug7.txt");
        File file = new File("data/help-text.txt");
        WritingFile use = WritingFile.newSampleFile(file);

        return use;
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

        Button button3 = new Button("Meta Area");
        Stage meta = new MetaDataEditWindow(TextDataType.Area.FRONT_BOTTOM,
            getStartFile().getMetaData());
        button3.setOnAction(event -> meta.show());
        button3.setDefaultButton(true);

        Scene scene = new Scene(new FlowPane(button1, button2, button3), 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
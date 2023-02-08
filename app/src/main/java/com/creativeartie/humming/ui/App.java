package com.creativeartie.humming.ui;

import com.creativeartie.humming.main.*;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * Not having Main extending Application seem to work here.
 *
 * @author wai
 */
public class App extends Application {
    /**
     * Main window. TODO WritingCoderPane is temporary.
     */
    private WritingCoderPane mainText = new WritingCoderPane();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // BorderPane mainPane = setup();
        // Scene scene = new Scene(mainPane, 400, 200);
        Parent root = FXMLLoader.load(DataFiles.MAIN_FXML.getFile());
        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Writer App");
        primaryStage.show();
    }

    /** Develop the main pane. TODO maybe set to delete. */
    BorderPane setup() {
        BorderPane mainPane = new BorderPane();
        // mainPane.setCenter(mainText);
        mainPane.setCenter(new Label("hello"));
        return mainPane;
    }

    @Override
    public void stop() {
        mainText.shutdown();
    }
}

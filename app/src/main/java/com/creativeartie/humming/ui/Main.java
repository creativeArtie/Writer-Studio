package com.creativeartie.humming.ui;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * Stores the main function
 *
 * @author wai-kin
 */
public class Main extends Application {

    /**
     * Main window. TODO WritingCoderPane is temporary.
     */
    private WritingCoderPane mainText = new WritingCoderPane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = setup();
        Scene scene = new Scene(mainPane, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /** Develop the main pane. TODO maybe set to delete. */
    BorderPane setup() {
        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(mainText);
        return mainPane;
    }

    @Override
    public void stop() {
        mainText.shutdown();
    }

}

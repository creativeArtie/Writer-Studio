package com.creativeartie.humming.ui;

import com.creativeartie.humming.main.*;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

/**
 * The starter method for the {@link Application}. <b>Not having Main extending
 * Application seem to work here.</b>
 *
 * @author wai
 */
public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(Main::handleExceptions);
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(UIText.getBundle());
        loader.setLocation(DataFiles.MAIN_FXML.getFile());
        Parent root;
        root = loader.load();

        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle(UIText.General.TITLE.getText());
        primaryStage.show();
    }

    @Override
    public void stop() {}
}

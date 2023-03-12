package com.creativeartie.humming.ui;

import com.creativeartie.humming.main.*;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

/**
 * Not having Main extending Application seem to work here.
 *
 * @author wai
 */
public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(App::handleExceptions);
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(UIText.getBundle());
        loader.setLocation(DataFiles.MAIN_FXML.getFile());
        Parent root;
        root = loader.load();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Writer App");
        primaryStage.show();
    }

    public static void handleExceptions(Thread t, Throwable e) {
        e.printStackTrace();
        System.exit(-1);
    }

    @Override
    public void stop() {}
}

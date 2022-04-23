package com.creativeartie.writer.writing;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class Main extends Application{

    private MainData mainText = new MainData();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

        BorderPane mainPane = setup();
		Scene scene = new Scene(mainPane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

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

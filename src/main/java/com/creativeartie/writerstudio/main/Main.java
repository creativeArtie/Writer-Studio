package com.creativeartie.writerstudio.main;

import java.io.*; // IOException;
import java.util.*;
import javafx.application.*;  // Appplication;
import javafx.scene.*; // Scene
import javafx.scene.control.*;
import javafx.stage.*; // Stage

import com.creativeartie.writerstudio.lang.markup.*; // WritingFile;
import com.creativeartie.writerstudio.resource.*; // WindowText
import com.creativeartie.writerstudio.javafx.*; // WriterSceneControl;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Main method */
public class Main extends Application{
    private static Stage mainStage;
    private static WritingFile writeFile;

    /** Main method
     *
     * @param args
     *      system arguments
     */
    public static void main(String[] args) {
        argumentNotNull(args, "args");
        launch(args);
    }

    /** Handle uncaught exceptions.
     *
     * @param thread
     *      killing thread
     * @param exception
     *      unhandled exception
     * @see #start(Stage)
     */
    private static void killProgram(Thread thread, Throwable exception){
        assert exception != null: "Null exception";
        try {
            exception.printStackTrace();
            File f = writeFile.dumpFile();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Writer Studio had crushed. :(");
            error.setHeaderText("You file had been save to: \n\t" + f.getCanonicalPath());
            error.setContentText("Error message for developer: " + exception.getMessage());
            error.showAndWait();

        } catch (Exception ex){
            /// An exception in default exception handling!!!
            ex.printStackTrace();
        } finally {
            /// Proper program shut down
            mainStage.close();
            Platform.exit();
            System.exit(-1);
        }
    }

    @Override
    public void start(Stage stage) throws Exception{
        /// Set uncaught exception handler
        Thread.setDefaultUncaughtExceptionHandler(Main::killProgram);

        /// Data to use for uncaught exceptions
        writeFile = getStartFile();
        mainStage = stage;

        /// create main pane
        WriterSceneControl writer = new WriterSceneControl(stage);
        writer.setupProperties();
        writer.setWritingFile(writeFile);

        /// set scene
        Scene scene = new Scene(writer, 800, 600);
        stage.setScene(scene);
        /// set stage info
        stage.setTitle(WindowText.PROGRAM_NAME.getText());
        stage.setMaximized(true);
        stage.show();
        // writer.returnFocus();
    }

    /** Create start file.
     *
     * File is made override able to test the file.
     *
     * @return answer
     */
    protected WritingFile getStartFile() throws IOException{
        return WritingFile.newSampleFile(new File("../../../src/back/resources/help-text.txt"));
        //return WritingFile.newFile();
    }
}

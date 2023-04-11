package com.creativeartie.humming.main;

import com.creativeartie.humming.ui.*;

import javafx.application.*;

/**
 * Stores the main function. This followed the code in here <a href=
 * "https://stackoverflow.com/questions/70175403/how-to-generate-javafx-jar-from-gradle-including-all-dependencies">
 * here</a>
 *
 * @author wai-kin
 */
public class Main {
    /**
     * The main method
     *
     * @param args
     *        console arguments
     */
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(Main::handleExceptions);
        Application.launch(App.class, args);
    }

    /**
     * Handle exceptions
     *
     * @param t
     *        the thread throwing error
     * @param e
     *        the error
     */
    public static void handleExceptions(Thread t, Throwable e) {
        e.printStackTrace();
        System.exit(-1);
    }
}

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
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(Main::handleExceptions);
        Application.launch(App.class, args);
    }

    public static void handleExceptions(Thread t, Throwable e) {
        e.printStackTrace();
        System.exit(-1);
    }
}

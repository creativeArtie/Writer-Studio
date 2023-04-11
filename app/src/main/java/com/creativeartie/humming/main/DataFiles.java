package com.creativeartie.humming.main;

import java.net.*;

/**
 * Lit of data files us in the program. Does not include files in jar that is
 * not used.
 *
 * @author wai
 */
@SuppressWarnings("nls")
public enum DataFiles {
    /** The main window. @see App#start() */
    MAIN_FXML("main.fxml"),
    /** The writer panel. @see MainWindowController */
    WRITER("writing.fxml");

    private static final String baseFolder = "/data/";
    private String fullPath;
    private URL file;

    private DataFiles(String path) {
        fullPath = baseFolder + path;
    }

    /**
     * Gets the file path as URL
     *
     * @return file path
     */
    public URL getFile() {
        if (file == null) {
            file = getClass().getResource(fullPath);
        }
        return file;
    }
}

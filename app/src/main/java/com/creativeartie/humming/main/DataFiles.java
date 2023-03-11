package com.creativeartie.humming.main;

import java.net.*;

public enum DataFiles {
    MAIN_FXML("main.fxml"), TEXT_CSS("text.css"), UI_TEXT("uiText.properties"), WRITER("writing.fxml");

    private static final String baseFolder = "/data/";
    private String fullPath;
    private URL file;

    private DataFiles(String path) {
        fullPath = baseFolder + path;
    }

    public URL getFile() {
        if (file == null) {
            file = getClass().getResource(fullPath);
        }
        return file;
    }
}

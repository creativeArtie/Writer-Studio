package com.creativeartie.writer.resource;

import java.io.*; // InputStrem
import java.net.*; // URL

public class FileResources{

    public static InputStream getCodeStyleProperties(){
        return getResourceStream("/data/code-styles.properties");
    }

    private static final String[] FONT_FILES = {
        "FreeSerif.ttf", "FreeSerifBold.ttf", "FreeSerifItalic.ttf",
        "FreeSerifBoldItalic.ttf",
        "FreeMono.ttf", "FreeMonoBold.ttf", "FreeMonoOblique.ttf",
        "FreeMonoBoldOblique.ttf"
    };

    public static InputStream[] getFontFiles(){
        InputStream[] stream = new InputStream[FONT_FILES.length];
        int i = 0;
        for (String font: FONT_FILES){
            stream[i++] = getResourceStream("/data/fonts/" + font);
        }
        return stream;
    }

    public static InputStream getButtonIcons(){
        return getResourceStream("/data/icons.png");
    }

    public static URL getBsdLicense(){
        return getResource("/data/bsd.txt");
    }

    public static URL getApacheLicense(){
        return getResource("/data/apache.txt");
    }

    public static String getStatsCss(){
        return "data/stats.css";
    }

    public static String getPrintCss(){
        return "data/print.css";
    }

    public static String getMainCss(){
        return "data/main.css";
    }

    public static String getAboutCss(){
        return "data/about.css";
    }

    private static URL getResource(String path){
        return FileResources.class.getResource(path);
    }

    private static InputStream getResourceStream(String path){
        return FileResources.class.getResourceAsStream(path);
    }
}

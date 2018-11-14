package com.creativeartie.writerstudio.export.pdf;

import java.io.*; // InputStrem
import java.net.*; // URL

public class FileResources{

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
            stream[i++] = FileResources.class.getResourceAsStream("pdf/" + font);
        }
        return stream;
    }
}

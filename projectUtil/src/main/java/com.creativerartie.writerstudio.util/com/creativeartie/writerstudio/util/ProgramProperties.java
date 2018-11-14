package com.creativeartie.writerstudio.util;

import java.util.*;

public enum ProgramProperties{
    PROGRAM_NAME("ProgramName"), PROGRAM_VERSION("ProgramVersion"),
    PROGRAM_LICENSE("Apache2License"), PDF_BOX_LICENSE("Apache2License"),
    GUAVA_LICENSE("Apache2License"), JAVAFX_LICENSE("GeneralPublicLicense"),
    JUNIT5_LICENSE("EclipsePublicLicense");
    private static final ResourceBundle fileProperties;

    static {
        fileProperties = PropertyResourceBundle.getBundle(
            "com.creativeartie.writerstudio.util.main", Locale.ENGLISH
        );
    }

    private final String keyName;

    private ProgramProperties(String key){
        keyName = key;
    }

    public String get(){
        return fileProperties.getString(keyName);
    }
}

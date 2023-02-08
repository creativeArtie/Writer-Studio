package com.creativeartie.humming.main;

import java.io.*;
import java.util.*;

import com.google.common.base.*;

import javafx.scene.control.*;

public interface TextBase {
    static Properties textProperties = loadProperties();

    static Properties loadProperties() {
        Properties props = new Properties();
        File file = new File(DataFiles.UI_TEXT.getFile().getPath());
        try (FileInputStream input = new FileInputStream(file)) {
            props.load(input);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return props;
    }

    String getPrefix();

    String name();

    default String getText() {
        String key = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
        return textProperties.getProperty(getPrefix() + "." + key);
    }

    default void setText(Label text) {
        text.setText(getText());
    }
}

package com.creativeartie.writerstudio.javafx;

import java.util.*;
import java.time.*;
import javafx.scene.layout.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.text.*;
import javafx.scene.*;
import java.io.*;

import org.fxmisc.richtext.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.main.*;
import com.creativeartie.writerstudio.resource.*;

class WindowMatterControl extends WindowMatterView{

    private WritingData writingData;

    protected void setupChildern(WriterSceneControl control){
        control.writingDataProperty().addListener((d, o, n) -> setData(n));
        showMatterProperty().addListener((d, o, n) -> showMatter(n));
    }

    private void setData(WritingData data){
        writingData = data;
    }

    private void showMatter(TextTypeMatter matter){
        setTitle(WindowText.getString(matter));
    }
}

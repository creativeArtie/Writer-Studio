package com.creativeartie.writerstudio.window;

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

class MetaDataEditWindow extends Stage{
    private final InlineCssTextArea textArea;
    private final TextFlow previewText;
    protected static int WIDTH = 650;
    protected static int HEIGHT = 500;
    protected static int AREA_HEIGHT = (500 / 2) - 10;

    MetaDataEditWindow(TextDataType.Area type){
        setTitle(WindowText.getString(type));
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        GridPane pane = new GridPane();
        textArea = initTextArea();
        textArea.setPrefWidth(WIDTH);
        textArea.setPrefHeight(AREA_HEIGHT);
        pane.add(createScrollBar(textArea), 0, 0);

        previewText = initPreviewArea();
        previewText.setPrefWidth(WIDTH);
        previewText.setPrefHeight(AREA_HEIGHT);
        pane.add(createScrollBar(previewText), 0, 1);

        Button cancel = new Button("Cancel");
        Button apply = new Button("Apply");
        Button ok = new Button("Okay");

        HBox buttons = new HBox();
        buttons.setSpacing(20.0);
        buttons.setAlignment(Pos.BASELINE_RIGHT);
        buttons.getChildren().addAll(apply, ok, cancel);
        pane.add(buttons, 0, 2);

        Scene scene = new Scene(pane, WIDTH, HEIGHT);
        setScene(scene);
    }

    private InlineCssTextArea initTextArea(){
        InlineCssTextArea area = new InlineCssTextArea();
        return area;
    }

    private TextFlow initPreviewArea(){
        TextFlow area = new TextFlow();
        area.getChildren().add(new Text("abc"));
        return area;
    }

    private ScrollPane createScrollBar(Node node){
        ScrollPane ans = new ScrollPane(node);
        ans.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return ans;
    }
}
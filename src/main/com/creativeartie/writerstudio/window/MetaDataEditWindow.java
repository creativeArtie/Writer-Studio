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
    private final WritingData writingData;
    private final TextDataType.Area showType;

    private final InlineCssTextArea textArea;
    private final TextFlow previewText;
    private final CheatsheetLabel[] textHints;
    protected static int WIDTH = 650;
    protected static int HEIGHT = 500;
    protected static int AREA_HEIGHT = (500 / 2) - 10;

    MetaDataEditWindow(TextDataType.Area type, WritingData data){
        writingData = data;
        showType = type;

        setTitle(WindowText.getString(type));
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        GridPane pane = new GridPane();
        textArea = initTextArea(pane);
        previewText = initPreviewArea(pane);
        textHints = initHintLabels(pane);

        updateTextHints();

        setScene(new Scene(pane, WIDTH, HEIGHT));
    }

    private InlineCssTextArea initTextArea(GridPane pane){
        InlineCssTextArea area = new InlineCssTextArea();
        String text = "";
        for (TextDataSpanPrint print: writingData.getPrint(showType)){
            if (! text.isEmpty()){
                text += "\n";
            }
            text += print.getData().map(s -> s.getRaw()).orElse("");
        }
        area.replaceText(text);
        pane.add(initScrollBar(area), 0, 0);
        return area;
    }

    private TextFlow initPreviewArea(GridPane pane){
        TextFlow area = new TextFlow();
        pane.add(initScrollBar(area), 0, 1);
        return area;
    }

    private CheatsheetLabel[] initHintLabels(GridPane parent){
        CheatsheetLabel[] format = new CheatsheetLabel[4];
        int i = 0;
        for (FormatType type: FormatType.values()){
            format[i++] = CheatsheetLabel.getLabel(type);
        }
        CheatsheetLabel[] labels = new CheatsheetLabel[]{
            CheatsheetLabel.getLabel(AuxiliaryType.AGENDA),
            CheatsheetLabel.getLabel(AuxiliaryType.ESCAPE),
            CheatsheetLabel.getLabel(AuxiliaryType.REF_KEY),
            CheatsheetLabel.getLabel(AuxiliaryType.DIRECT_LINK),
            CheatsheetLabel.getLabel(AuxiliaryType.REF_LINK),
            format[0], format[1], format[2], format[3]
        };

        GridPane pane = new GridPane();
        i = 0;
        for (int row = 0; row < 3; row++){
            setPrecentWidth(pane, 33.333333);
            for (int col = 0; col < 3; col++){
                pane.add(labels[i++], row, col);
            }
        }
        parent.add(pane, 0, 2);
        return labels;
    }

    private ScrollPane initScrollBar(Region node){
        ScrollPane ans = new ScrollPane(node);
        node.setMinWidth(WIDTH);
        node.setMinHeight(WIDTH);

        ans.setPrefWidth(WIDTH);
        ans.setPrefHeight(AREA_HEIGHT);

        ans.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return ans;
    }

    /**
     * Set the next column by percent width. Helper method of
     * {@link #initHintsLabels()}.
     */
    private void setPrecentWidth(GridPane pane, double value){
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(value);
        pane.getColumnConstraints().add(column);
    }

    private void updateTextHints(){
        previewText.getChildren().clear();
        for (TextDataSpanPrint print: writingData.getPrint(showType)){
            TextFlowBuilder.loadFormatText(previewText, print.getData());
        }
    }
}
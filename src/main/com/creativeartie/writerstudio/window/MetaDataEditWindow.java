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

public class MetaDataEditWindow extends Stage{
    private final WritingData writingData;
    private final TextDataType.Area showType;

    private final InlineCssTextArea textArea;
    private final VBox previewText;
    private final TreeMap<Integer, ComboBox<TextDataType.Format>> alignChoices;
    protected static int WIDTH = 650;
    protected static int HEIGHT = 500;
    protected static int AREA_HEIGHT = (500 / 2) - 10;

    public MetaDataEditWindow(TextDataType.Area type, WritingData data){
        writingData = data;
        showType = type;
        alignChoices = new TreeMap<>();

        setTitle(WindowText.getString(type));
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        GridPane pane = new GridPane();
        setPrecentWidth(pane, 100);

        textArea = initTextArea(pane);
        previewText = initPreviewArea(pane);
        initHintLabels(pane);

        updatePreview(); /// assigns printSpans

        textArea.plainTextChanges().subscribe(value -> {
            String text = textArea.getText();
            try {
                writingData.setPrintText(showType, text);
            } catch (TextAreaLineException except){
                text = text.substring(0, AuxiliaryData.TOKEN_ESCAPE.length());
                // TODO better alert dialogue
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Text Setting Error");
                error.setHeaderText("Text cannot ends with a '\\'");
                error.setContentText("The text will be set as: " + text);
                try {
                    writingData.setPrintText(showType, text);
                } catch (TextAreaLineException ex){
                    throw new RuntimeException(ex);
                }
            }
            updatePreview();
        });

        pane.getStylesheets().add(FileResources.getPrintCss());
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
        setPrecentHeight(pane, 45);
        pane.add(area, 0, 0);
        return area;
    }

    private Node initCombobox(TextDataSpanPrint line){
        TextDataType.Format ans = line.getFormat();
        ComboBox<TextDataType.Format> box = new ComboBox<>();
        box.getItems().addAll(TextDataType.Format.listAligns());
        box.getSelectionModel().select(ans);
        box.getSelectionModel().selectedItemProperty().addListener((b, o, n) ->{
            line.setFormat(n);
            updatePreview();
        });
        return box;
    }

    private VBox initPreviewArea(GridPane pane){
        VBox area = new VBox();
        setPrecentHeight(pane, 45);
        pane.add(area, 0, 1);
        area.getStyleClass().add("border");
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
        pane.getStyleClass().add("border");
        i = 0;
        for (int row = 0; row < 3; row++){
            setPrecentWidth(pane, 33.333333);
            for (int col = 0; col < 3; col++){
                pane.add(labels[i++], row, col);
            }
        }
        setPrecentHeight(pane, 10);
        parent.add(pane, 0, 2);
        return labels;
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

    private void setPrecentHeight(GridPane pane, double value){
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(value);
        pane.getRowConstraints().add(row);
    }

    private void updatePreview(){
        previewText.getChildren().clear();
        for (TextDataSpanPrint print: writingData.getPrint(showType)){
            BorderPane line = new BorderPane();
            line.setLeft(initCombobox(print));
            line.setCenter(TextFlowBuilder.loadMetaText(print));
            previewText.getChildren().add(line);
        }
    }
}
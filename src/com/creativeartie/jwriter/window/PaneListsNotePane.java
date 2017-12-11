package com.creativeartie.jwriter.window;

import java.util.*;
import java.util.Optional;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.cell.*;
import javafx.geometry.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.window.PaneListsData.IdentityData;
import com.creativeartie.jwriter.property.window.*;

import com.google.common.base.*;
import com.google.common.collect.*;

public class PaneListsNotePane extends BorderPane{
    private TitledPane titlePane;

    PaneListsNotePane(TitledPane title){
        titlePane = title;
    }

    public void clearData(){
        clearBasicData();
        setCenter(newLabel(WindowText.NO_NOTE_SELECTED));
    }

    public void setEmptyData(){
        clearBasicData();
        setCenter(newLabel(WindowText.NO_NOTE_FOUND));
    }

    private void clearBasicData(){
        Text text = new Text(WindowText.NO_NOTE_TITLE.getText());
        text.setStyle(WindowStyle.EMPTY_TITLE.toCss());
        titlePane.setGraphic(text);
        setBottom(null);

    }

    public void setData(Optional<MainSpanNote> note){
        if (note.isPresent()){
            GridPane cite = new GridPane();
            setupColumnConstraints(20.0, cite);
            setupColumnConstraints(80.0, cite);

            TextFlow text = new TextFlow();
            boolean firstLine = true; /// First note is a heading

            TextFlow source = new TextFlow();
            boolean hasText = false; /// has source?
            boolean hasRef = false; /// has in-text or footnote?
            for (Span child: note.get()){
                if(child instanceof LinedSpanNote){
                    LinedSpanNote line = (LinedSpanNote)child;
                    if (firstLine){
                        titlePane.setGraphic(WindowSpanParser.parseDisplay(
                            line.getFormattedSpan().orElse(null)
                        ));
                    } else {
                        WindowSpanParser.parseDisplay(text, line
                            .getFormattedSpan().orElse(null));
                    }
                    text.getChildren().add(new Text("\n"));
                    firstLine = false;
                } else if (child instanceof LinedSpanCite){
                    LinedSpanCite line = (LinedSpanCite)child;
                    Optional<InfoDataSpan> data = line.getData();
                    if (line.getFieldType() == InfoFieldType.SOURCE){
                        if (! hasText){
                            cite.add(newLabel(WindowText.SOURCE_LABEL), 0, 0);
                            ScrollPane pane = new ScrollPane(source);
                            pane.setFitToWidth(true);
                            cite.add(pane, 1, 0);
                            hasText = true;
                        }
                        addSources(source, data);
                    } else if (line.getFieldType() == InfoFieldType.ERROR){
                    } else if (! hasRef){
                        if (line.getFieldType() == InfoFieldType.FOOTNOTE){
                            hasRef = addInText(cite, data, WindowText.FOOTNOTE_LABEL);
                        } else {
                            hasRef = addInText(cite, data, WindowText.IN_TEXT_LABEL);
                        }
                    }
                }
            }
            setCenter(new ScrollPane(text));
            setBottom(cite);
        } else {
            setEmptyData();
        }
    }

    private static Label newLabel(WindowText text){
        Label ans = new Label(text.getText());
        return ans;
    }

    private void addSources(TextFlow source, Optional<InfoDataSpan> data){
        data.ifPresent(span -> {
            FormatSpanMain found = ((InfoDataSpanFormatted)span)
                .getData();
            WindowSpanParser.parseDisplay(source, found);
        });
    }

    private boolean addInText(GridPane cite, Optional<InfoDataSpan> data,
        WindowText text)
    {
        if (data.isPresent()){
            ContentSpan found = ((InfoDataSpanText)data.get()).getData();
            cite.add(new TextFlow(new Text(found.getTrimmed())), 1, 1);
            cite.add(newLabel(text), 0, 1);
            return true;
        }
        return false;
    }

    private ColumnConstraints setupColumnConstraints(double precent,
        GridPane cite)
    {
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(precent);
        cite.getColumnConstraints().add(column);
        return column;
    }
}
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
import com.creativeartie.jwriter.window.NotesData.IdentityData;
import com.creativeartie.jwriter.property.window.*;

import com.google.common.base.*;
import com.google.common.collect.*;

class NotesDetailPane extends BorderPane{
    private TitledPane titlePane;
    private TextFlow noteContent;
    private GridPane citationPane;
    private boolean hasHeading;
    private boolean hasCitation;
    private boolean hasIntext;

    NotesDetailPane(TitledPane title){
        titlePane = title;
    }

    public void clearSelection(){
        clearBasicData();
        setCenter(new Label(WindowText.NO_NOTE_SELECTED.getText()));
    }

    public void setEmptyData(){
        clearBasicData();
        setCenter(new Label(WindowText.NO_NOTE_FOUND.getText()));
    }

    private void clearBasicData(){
        Text text = new Text(WindowText.NO_NOTE_TITLE.getText());
        text.setStyle(WindowStyle.EMPTY_TITLE.toCss());
        titlePane.setGraphic(text);
        setBottom(null);
        clearFields();
    }

    private void clearFields(){
        noteContent = null;
        citationPane = null;
        hasHeading = false;
        hasCitation = false;
        hasIntext = false;
    }

    public void setData(Optional<NoteCardSpan> note){
        if (note.isPresent()){
            clearFields();

            citationPane = new GridPane();
            setupColumnConstraints(20.0, citationPane);
            setupColumnConstraints(80.0, citationPane);

            noteContent = new TextFlow();
            for (Span child: note.get()){
                if (child instanceof LinedSpanNote){
                    fillContent((LinedSpanNote)child);
                } else if (child instanceof LinedSpanCite){
                    fillContent((LinedSpanCite)child);
                }
            }
            setCenter(new ScrollPane(noteContent));
            setBottom(citationPane);
        } else {
            clearBasicData();
        }
    }

    private void fillContent(LinedSpanNote line){
        if (hasHeading){
            /// Find content line:
            WindowSpanParser.parseDisplay(noteContent, line.getFormattedSpan()
                .orElse(null));
            noteContent.getChildren().add(new Text("\n"));
        } else {
            /// Find heading line:
            titlePane.setGraphic(WindowSpanParser.parseDisplay(
                line.getFormattedSpan().orElse(null)
            ));
            hasHeading = true;
        }
    }

    private void fillContent(LinedSpanCite line){
       Optional<InfoDataSpan> data = line.getData();
        if (line.getFieldType() == InfoFieldType.SOURCE){
            if (! hasCitation) {
                hasCitation = addSources(data);
            }
        // } else if (line.getFieldType() == InfoFieldType.ERROR){
        } else if (! hasIntext){
            WindowText text = line.getFieldType() == InfoFieldType.FOOTNOTE?
                WindowText.FOOTNOTE_LABEL: WindowText.IN_TEXT_LABEL;
            hasIntext = addInText(data, text);
        }
    }

    private boolean addSources(Optional<InfoDataSpan> data){
        if (data.isPresent()){
            TextFlow source = new TextFlow();
            citationPane.add(new Label(WindowText.SOURCE_LABEL.getText()), 0, 0);
            ScrollPane pane = new ScrollPane(source);
            pane.setFitToWidth(true);
            citationPane.add(pane, 1, 0);
            FormatSpanMain found = ((InfoDataSpanFormatted)data.get()).getData();
            WindowSpanParser.parseDisplay(source, found);
            return true;
        }
        return false;
    }

    private boolean addInText(Optional<InfoDataSpan> data, WindowText text) {
        if (data.isPresent()){
            ContentSpan found = ((InfoDataSpanText)data.get()).getData();
            citationPane.add(new TextFlow(new Text(found.getTrimmed())), 1, 1);
            citationPane.add(new Label(text.getText()), 0, 1);
            return true;
        }
        return false;
    }

    private ColumnConstraints setupColumnConstraints(double precent,
            GridPane cite) {
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(precent);
        cite.getColumnConstraints().add(column);
        return column;
    }
}
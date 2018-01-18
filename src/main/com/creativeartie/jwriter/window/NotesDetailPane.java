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

/**
 * A {@link Pane} that shows a {@link NoteCardSpan}.
 */
class NotesDetailPane extends BorderPane{
    /// Variables put here to allow easiler method break up.
    private final TitledPane titlePane;
    private TextFlow noteContent;
    private GridPane citationPane;
    private boolean hasHeading;
    private boolean hasCitation;
    private boolean hasIntext;

    NotesDetailPane(TitledPane title){
        titlePane = title;
    }

    /** Removes note selection. */
    public void clearSelection(){
        clearBasicData(new Text(WindowText.NO_NOTE_TITLE.getText()));
        setCenter(new Label(WindowText.NO_NOTE_SELECTED.getText()));
    }

    /**
     * Clear everything to be ready for new data. Helper method of
     * {@link #clearSelection()} and {@link #setCardNote(Optional)}.
     */
    private void clearBasicData(Text title){
        title.setStyle(WindowStyle.EMPTY_TITLE.toCss());
        titlePane.setGraphic(title);
        setBottom(null);
        noteContent = null;
        citationPane = null;
        hasHeading = false;
        hasCitation = false;
        hasIntext = false;
    }

    /** Set a {@link CardNote}. */
    public void setCardNote(Optional<NoteCardSpan> note){
        if (note.isPresent()){
            setPlaceHolders();

            citationPane = new GridPane();
            setupColumnConstraints(20.0);
            setupColumnConstraints(80.0);

            noteContent = new TextFlow();
            for (Span child: note.get()){
                if (child instanceof LinedSpanNote){
                    fillContent((LinedSpanNote)child);
                } else if (child instanceof LinedSpanCite){
                    fillContent((LinedSpanCite)child);
                }
            }
            setContent();
        } else {
            setPlaceHolders();
        }
    }

    /** Setup placeholders. Helper method of {@link #setCardNote(Optional)}. */
    private void setPlaceHolders(){
        clearBasicData(new Text(WindowText.NO_NOTE_PLACEHOLDER.getText()));
        setCenter(new Label(WindowText.NO_NOTE_FOUND.getText()));
    }

    /**
     * Replace placeholder with content, if found. Helper method of
     * {@link #setCardNote(Optional)}.
     */
    private void setContent(){
        if (! noteContent.getChildren().isEmpty()){
            setCenter(new ScrollPane(noteContent));
        }
        if (hasCitation || hasIntext){
            setBottom(citationPane);
        }
    }

    /**
     * Create columns for citationPane. Helper method of
     * {@link #setCardNote(Optional)}.
     */
    private ColumnConstraints setupColumnConstraints(double precent) {
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(precent);
        citationPane.getColumnConstraints().add(column);
        return column;
    }

    /**
     * Fill either heading or content. Helper method of
     * {@link #setCardNote(Optional)}.
     */
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

    /**
     * Fill the citationPane. Helper method of {@link #setCardNote(Optional)}.
     */
    private void fillContent(LinedSpanCite line){
       Optional<InfoDataSpan> data = line.getData();
        /// Where FieldType() == InforFieldType.SOURCE
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

    /**
     * Add data from {@link LineSpanCite} for source citation. Helper method of
     *  {@link #fillContent(LinedSpanCite)}.
     */
    private boolean addSources(Optional<InfoDataSpan> data){
        if (data.isPresent()){
            /// Add Label
            citationPane.add(new Label(WindowText.SOURCE_LABEL.getText()), 0, 0);

            /// create Node to store data
            TextFlow source = new TextFlow();
            FormatSpanMain found = ((InfoDataSpanFormatted)data.get())
                .getData();
            WindowSpanParser.parseDisplay(source, found);

            /// Put the source node into a ScrollPane
            ScrollPane pane = new ScrollPane(source);
            pane.setFitToWidth(true);
            citationPane.add(pane, 1, 0);
            return true;
        }
        return false;
    }

    /**
     * Add data from {@link LineSpanCite} for in-text citation. Helper method of
     * {@link #fillContent(LinedSpanCite)}.
     */
    private boolean addInText(Optional<InfoDataSpan> data, WindowText text) {
        if (data.isPresent()){
            /// Add Label
            citationPane.add(new Label(text.getText()), 0, 1);

            /// create Node to store data
            ContentSpan found = ((InfoDataSpanText)data.get()).getData();
            citationPane.add(new TextFlow(new Text(found.getTrimmed())), 1, 1);
            return true;
        }
        return false;
    }
}
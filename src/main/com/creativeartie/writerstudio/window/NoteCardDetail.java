package com.creativeartie.writerstudio.window;

import java.util.*;
import java.util.function.*;
import java.util.Optional;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.cell.*;
import javafx.geometry.*;

import com.creativeartie.writerstudio.main.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

import com.google.common.base.*;
import com.google.common.collect.*;

public class NoteCardDetail extends TitledPane{

    private final Label sourceLabel;
    private Optional<NoteCardSpan> targetSpan;
    private ReadOnlyIntegerWrapper locationChoosen;
    /// No listenerss for changes, NoteCardControl deselect and reselect it

    public NoteCardDetail(){
        setCollapsible(false);
        targetSpan = Optional.empty();
        sourceLabel = new Label(WindowText.NOTE_CARD_SOURCE.getText());
        locationChoosen = new ReadOnlyIntegerWrapper(this, "locationChoosen");
        clearContent();
    }
    
    public ReadOnlyIntegerProperty locationChoosenProperty(){
        return locationChoosen.getReadOnlyProperty();
    }
    
    public int getLocationChoose(){
        return locationChoosen.getValue();
    }

    public void clearContent(){
        Label placeholder = new Label(WindowText.NOTE_CARD_PLACEHOLDER_TITLE
            .getText());
        StyleClass.NOT_FOUND.addClass(placeholder);
        setGraphic(placeholder);
        placeholder = new Label(WindowText.NOTE_CARD_PLACHOLDER_DETAIL
            .getText());
        StyleClass.NOT_FOUND.addClass(placeholder);
        setContent(placeholder);
    }

    public void setNoteCard(NoteCardSpan span){
        if (span == null){
            clearContent();
            return;
        }
        /// Add the title
        Optional<FormatSpanMain> title = span.getTitle();
        if (title.isPresent()){
            setGraphic(TextFlowBuilder.loadFormatText(span.getTitle()));
        } else {
            Label label = new Label(WindowText.NOTE_CARD_EMPTY_TITLE.getText());
            StyleClass.NO_TEXT.addClass(label);
            setGraphic(label);
        }

        /// Add the content
        Node content;
        Collection<Optional<FormatSpanMain>> lines = span.getContent();
        if (lines.isEmpty()){
            content = new Label(WindowText.NOTE_CARD_EMTPY_DETAIL.getText());
        } else {
            TextFlow text = new TextFlow();
            for (Optional<FormatSpanMain> line: lines){
                TextFlowBuilder.loadFormatText(text, line);
                text.getChildren().add(new Text("\n"));
            }
            ScrollPane pane = new ScrollPane(text);
            pane.setFitToHeight(true);
            pane.setFitToWidth(true);
            content = pane;
        }

        /// Add citations
        GridPane bottom = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(30.0);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(70.0);
        bottom.getColumnConstraints().addAll(column1, column2);
        /// add source
        span.getSource().ifPresent(source -> {
            bottom.add(sourceLabel, 0, 0);
            ScrollPane pane = new ScrollPane(TextFlowBuilder
                .loadFormatText(Optional.of(source)));
            bottom.add(new ScrollPane(TextFlowBuilder.loadFormatText(
                    Optional.of(source)
                )), 1, 0);
        });

        span.getInTextLine().ifPresent(line -> {
            WindowText type = line.getFieldType() == InfoFieldType.FOOTNOTE?
                WindowText.NOTE_CARD_FOOTNOTE: WindowText.NOTE_CARD_IN_TEXT;
            Label label = new Label(type.getText());
            bottom.add(label, 0, 1);
            bottom.add(new ScrollPane(new Text(
                    ((ContentSpan)line.getData().get().getData()).getTrimmed()
                )), 1, 1);
        });

        bottom.add(new StackPane(new Label(span.getLookupText())), 0, 2);
        Button button = new Button(WindowText.NOTE_CARD_EDIT.getText());
        button.setOnAction(evt -> locationChoosen.setValue(span.getEnd()));
        bottom.add(new StackPane(button), 1, 2);

        BorderPane ans = new BorderPane();
        ans.setCenter(content);
        ans.setBottom(bottom);
        setContent(ans);
    }
}
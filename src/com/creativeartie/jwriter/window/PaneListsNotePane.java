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

import com.google.common.base.*;
import com.google.common.collect.*;

public class PaneListsNotePane extends BorderPane{

    public void clearData(){
        setBottom(null);
        setCenter(newLabel("ListView.SelectNone"));
    }

    public void setEmptyData(){
        setBottom(null);
        setCenter(newLabel("ListView.SelectEmpty"));
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
                        SpanBranchParser.parseDisplay(text, line
                            .getFormattedSpan().orElse(null), "Other.NoteHeading");
                    } else {
                        SpanBranchParser.parseDisplay(text, line
                            .getFormattedSpan().orElse(null));
                    }
                    text.getChildren().add(new Text("\n"));
                    firstLine = false;
                } else if (child instanceof LinedSpanCite){
                    LinedSpanCite line = (LinedSpanCite)child;
                    Optional<InfoDataSpan> data = line.getData();
                    if (line.getFieldType() == InfoFieldType.SOURCE){
                        if (! hasText){
                            cite.add(newLabel("ListView.Source"), 0, 0);
                            ScrollPane pane = new ScrollPane(source);
                            pane.setFitToWidth(true);
                            cite.add(pane, 1, 0);
                            hasText = true;
                        }
                        addSources(source, data);
                    } else if (line.getFieldType() == InfoFieldType.ERROR){
                    } else if (! hasRef){
                        if (line.getFieldType() == InfoFieldType.FOOTNOTE){
                            hasRef = addInText(cite, data, "ListView.Footnote");
                        } else {
                            hasRef = addInText(cite, data, "ListView.InText");
                        }
                    }
                }
            }
            setCenter(new ScrollPane(text));
            setBottom(cite);
        } else {
            setBottom(null);
            setCenter(new Label(Utilities.getString("ListView.SelectEmpty")));
        }
    }

    private static Label newLabel(String key){
        Label ans = new Label(Utilities.getString(key));
        return ans;
    }

    private void addSources(TextFlow source, Optional<InfoDataSpan> data){
        data.ifPresent(span -> {
            FormatSpanMain found = ((InfoDataSpanFormatted)span)
                .getData();
            SpanBranchParser.parseDisplay(source, found);
        });
    }

    private boolean addInText(GridPane cite, Optional<InfoDataSpan> data,
        String key)
    {
        if (data.isPresent()){
            ContentSpan found = ((InfoDataSpanText)data.get()).getData();
            cite.add(new TextFlow(new Text(found.getParsed())), 1, 1);
            cite.add(newLabel(key), 0, 1);
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
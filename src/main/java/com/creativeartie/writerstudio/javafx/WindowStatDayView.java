package com.creativeartie.writerstudio.javafx;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.scene.image.*;

import java.util.*;
import java.time.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.main.*;
import com.creativeartie.writerstudio.resource.*;


abstract class WindowStatDayView extends AnchorPane{
    /// %Part 1: Constructor and Class Fields
    private Label dayLabel;
    private Label statLabel;
    private Tooltip statsTip;

    private SimpleObjectProperty<StatSpanDay> dayRecord;
    private SimpleObjectProperty<LocalDate> showDate;

    WindowStatDayView(){
        setMaxWidth(WindowStatView.WIDTH / 7);
        setPrefHeight(60);
        getStyleClass().add("day-box");

        getChildren().addAll(buildDayLabel(), buildStatLabel());

        dayRecord = new SimpleObjectProperty<>();
        showDate = new SimpleObjectProperty<>();
    }

    /// %Part 2: Layout

    private Label buildDayLabel(){
        dayLabel = new Label();
        setTopAnchor(dayLabel, 0.0);
        setLeftAnchor(dayLabel, 0.0);

        return dayLabel;
    }

    private Label buildStatLabel(){
        statLabel = new Label();
        statsTip = new Tooltip();
        statLabel.setTooltip(statsTip);

        setTopAnchor(statLabel, 5.0);
        setRightAnchor(statLabel, 0.0);

        return statLabel;
    }

    /// %Part 3: Setup Properties

    public void setupProperties(WindowStatControl control){
        setupChildern(control);
    }

    protected abstract void setupChildern(WindowStatControl control);

    /// %Part 4: Properties

    public SimpleObjectProperty<LocalDate> showDateProperty(){
        return showDate;
    }

    public LocalDate getShowDay(){
        return showDate.getValue();
    }

    public void setShowDay(LocalDate value){
        showDate.setValue(value);
    }

    public SimpleObjectProperty<StatSpanDay> dayRecordProperty(){
        return dayRecord;
    }

    public StatSpanDay getDayRecord(){
        return dayRecord.getValue();
    }

    public void setDayRecord(StatSpanDay value){
        dayRecord.setValue(value);
    }

    /// %Part 5: Get Child Methods
    Label getDayLabel(){
        return dayLabel;
    }

    Label getStatLabel(){
        return statLabel;
    }

    Tooltip getStatsTip(){
        return statsTip;
    }

}

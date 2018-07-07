package com.creativeartie.writerstudio.javafx;

import java.time.*;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;

import com.creativeartie.writerstudio.lang.markup.*;

abstract class WindowStatDayView extends AnchorPane{
    /// %Part 1: Constructor and Class Fields
    private Label dayLabel;
    private Label statLabel;
    private Tooltip statTip;

    private SimpleObjectProperty<LocalDate> showDate;

    WindowStatDayView(){
        setMaxWidth(WindowStatView.WIDTH / 7);
        setPrefHeight(60);
        getStyleClass().add("day-box");

        getChildren().addAll(buildDayLabel(), buildStatLabel());

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
        statTip = new Tooltip();
        statLabel.setTooltip(statTip);

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

    /// %Part 5: Get Child Methods
    Label getDayLabel(){
        return dayLabel;
    }

    Label getStatLabel(){
        return statLabel;
    }

    Tooltip getStatTip(){
        return statTip;
    }

}

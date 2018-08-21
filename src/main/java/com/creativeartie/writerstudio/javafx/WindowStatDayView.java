package com.creativeartie.writerstudio.javafx;

import java.time.*;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;

import com.creativeartie.writerstudio.lang.markup.*;
import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    WindowStatChildContants.*;

abstract class WindowStatDayView extends AnchorPane{
    /// %Part 1: Constructor and Class Fields

    private SimpleObjectProperty<LocalDate> showDate;

    private Label dayLabel;
    private Label statLabel;
    private Tooltip statTip;

    WindowStatDayView(){
        setMaxWidth(COLUMN_WIDTH);
        setPrefHeight(CELL_HEIGHT);
        getStyleClass().add(DAY_BOX_STYLE);

        getChildren().addAll(buildDayLabel(), buildStatLabel());

        showDate = new SimpleObjectProperty<>();
    }

    /// %Part 2: Layout

    private Label buildDayLabel(){
        dayLabel = new Label();
        setTopAnchor(dayLabel, DAY_ANCHOR_TOP);
        setLeftAnchor(dayLabel, DAY_ANCHOR_LEFT);

        return dayLabel;
    }

    private Label buildStatLabel(){
        statLabel = new Label();
        statTip = new Tooltip();
        statLabel.setTooltip(statTip);

        setTopAnchor(statLabel, STAT_ANCHOR_TOP);
        setRightAnchor(statLabel, STAT_ANCHOR_RIGHT);

        return statLabel;
    }

    /// %Part 3: Setup Properties

    public void setupProperties(WindowStatControl control){
        bindChildren(control);
    }

    protected abstract void bindChildren(WindowStatControl control);

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

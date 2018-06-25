package com.creativeartie.writerstudio.stats;

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
import com.creativeartie.writerstudio.window.*;
import com.creativeartie.writerstudio.resource.*;


abstract class PaneMonthDayView extends AnchorPane{
    private Label localDay;
    private Label imageIcon;
    private Tooltip statsTip;
    private SimpleObjectProperty<StatSpanDay> storedRecord;
    private SimpleObjectProperty<LocalDate> readDay;

    public PaneMonthDayView(){
        setMaxWidth(SceneStatsView.WIDTH / 7);
        setPrefHeight(60);
        getStyleClass().add("day-box");

        localDay = new Label();
        imageIcon = new Label();
        statsTip = new Tooltip();

        layoutText();

        readDay = new SimpleObjectProperty<>(this, "readDay");
        readDay.addListener((data, oldValue, newValue) -> listenDate(newValue));
    }

    /// Getters
    protected Label getDayLabel(){
        return localDay;
    }

    protected Label getImageIcon(){
        return imageIcon;
    }

    protected Tooltip getWordLabel(){
        return statsTip;
    }

    /// Layout Node
    private void layoutText(){
        setTopAnchor(imageIcon, 5.0);
        setRightAnchor(imageIcon, 0.0);

        setTopAnchor(localDay, 0.0);
        setLeftAnchor(localDay, 0.0);

        getChildren().addAll(imageIcon, localDay);
        imageIcon.setTooltip(statsTip);
    }

    /// Node Properties
    public ObjectProperty<LocalDate> readDayProperty(){
        return readDay;
    }

    public LocalDate getReadDay(){
        return readDay.getValue();
    }

    public void setReadDay(LocalDate doc){
        readDay.setValue(doc);
    }

    void clearDay(){
        readDay.setValue(null);
        imageIcon.setGraphic(null);
        imageIcon.setTooltip(null);
    }

    void emptyDay(LocalDate day){
        readDay.setValue(day);
        imageIcon.setGraphic(null);
        imageIcon.setTooltip(null);
    }

    void noRecord(LocalDate day){
        readDay.setValue(day);
        imageIcon.setGraphic(setImage(ButtonIcon.GOAL_FAIL));
        statsTip.setText(WindowText.CALENDAR_NO_RECORD.getText());
        imageIcon.setTooltip(statsTip);
    }

    void withRecord(StatSpanDay record){
        readDay.setValue(record.getRecordDate());
        int written = record.getPublishWritten();
        int goal = record.getPublishGoal();
        Duration dur = record.getWriteTime();
        Duration timeGoal = record.getTimeGoal();
        boolean time = dur.toMinutes() >= record.getTimeGoal().toMinutes();
        imageIcon.setGraphic(setImage(written >= record.getPublishGoal()?
            (time? ButtonIcon.GOAL_ALL: ButtonIcon.GOAL_WORD) :
            (time? ButtonIcon.GOAL_TIME: ButtonIcon.GOAL_FAIL)));

        String tip = String.format("Written: %,d(%,d)\nTime: %s (%s)",
            written, goal, formatDuration(dur), formatDuration(timeGoal));
        statsTip.setText(tip);
        imageIcon.setTooltip(statsTip);
    }

    private ImageView setImage(ButtonIcon icon){
        ImageView image = icon.getIcon();
        image.setFitHeight(50);
        image.setFitWidth(50);
        return image;
    }

    private static String formatDuration(Duration duration){
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }
    /// Control Methods
    protected abstract void listenDate(LocalDate value);
}

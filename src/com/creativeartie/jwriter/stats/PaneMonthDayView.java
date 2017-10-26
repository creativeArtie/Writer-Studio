package com.creativeartie.jwriter.stats;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.scene.image.*;

import java.util.*;
import java.time.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.window.*;

import com.creativeartie.jwriter.file.*;

abstract class PaneMonthDayView extends AnchorPane{
    private Label localDay;
    private Label imageIcon;
    private Tooltip statsTip;
    private SimpleObjectProperty<Record> storedRecord;
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

    private static String getString(String key){
        return Utilities.getString("Calendar." + key);
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
        imageIcon.setGraphic(setImage(EditIcon.GOAL_FAIL));
        statsTip.setText(getString("NoRecord"));
        imageIcon.setTooltip(statsTip);
    }

    void withRecord(Record record){
        readDay.setValue(record.getRecordDate());
        int written = record.getPublishWritten();
        int goal = record.getPublishGoal();
        Duration dur = record.getWriteDuration();
        Duration timeGoal = record.getTimeGoal();
        boolean time = dur.toMinutes() >= record.getTimeGoal().toMinutes();
        imageIcon.setGraphic(setImage(written >= record.getPublishGoal()?
            (time? EditIcon.GOAL_ALL: EditIcon.GOAL_WORD) :
            (time? EditIcon.GOAL_TIME: EditIcon.GOAL_FAIL)));
        
        String tip = String.format("Written: %,d(%,d)\nTime: %s (%s)", 
            written, goal, Utilities.formatDuration(dur), 
            Utilities.formatDuration(timeGoal));
        statsTip.setText(tip);
        imageIcon.setTooltip(statsTip);
    }
    
    private ImageView setImage(EditIcon icon){
        ImageView image = icon.getIcon();
        image.setFitHeight(50);
        image.setFitWidth(50);
        return image;
    }
    /// Control Methods
    protected abstract void listenDate(LocalDate value);
}

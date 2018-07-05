package com.creativeartie.writerstudio.javafx;

import javafx.beans.property.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;

import java.util.*;
import java.time.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.main.*;

class WindowStatControl extends WindowStatView{
    private WritingStat writingStat;
    private StatSpanDay currentRecord;

    @Override
    protected void bindWritingStat(ReadOnlyObjectWrapper<WritingStat> stats,
            WriterSceneControl control){
        stats.bind(control.writingStatProperty());
    }

    @Override
    protected void setupChildern(WriterSceneControl control){
        writingStatProperty().addListener((d, o, n) -> setStat(n));

        getWordSpinner().valueProperty().addListener((d, o, n) -> editWords(n));
        getHourSpinner().valueProperty().addListener((d, o, n) -> editHours(n));
        getMinuteSpinner().valueProperty().addListener((d, o, n) -> editMinutes(n));

        getCalendarPane().setupProperties(this);
    }

    private void setStat(WritingStat stat){
        writingStat = stat;
        if (stat != null){
            stat.addDocEdited(s -> updateStat());
            updateStat();
        }
    }

    private void updateStat(){
        currentRecord = writingStat.getRecord();
        getWordSpinner().getValueFactory().setValue(
            currentRecord.getPublishGoal()
        );
        getHourSpinner().getValueFactory().setValue(
            (int)currentRecord.getTimeGoal().toHours()
        );
        getMinuteSpinner().getValueFactory().setValue(
            (int)currentRecord.getTimeGoal().toMinutes() % 60
        );
    }

    private void editWords(int count){
        currentRecord.setPublishGoal(count);
    }

    private void editHours(int hours){
        editTime(hours, getMinuteSpinner().getValue());
    }

    private void editMinutes(int minutes){
        editTime(getHourSpinner().getValue(), minutes);
    }

    private void editTime(int hours, int minutes){
        Duration goal = Duration.ofHours(hours).plusMinutes(minutes);
        currentRecord.setTimeGoal(goal);
    }
}

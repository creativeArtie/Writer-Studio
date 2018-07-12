package com.creativeartie.writerstudio.javafx;

import java.time.*;
import javafx.beans.property.*;

import com.creativeartie.writerstudio.lang.markup.*;

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
        getCalendarPane().setupProperties(this);

        writingStatProperty().addListener((d, o, n) -> loadStat(n));

        getWordSpinner().valueProperty().addListener((d, o, n) -> editWords(n));
        getHourSpinner().valueProperty().addListener((d, o, n) -> editHours(n));
        getMinuteSpinner().valueProperty().addListener((d, o, n) -> editMinutes(n));
    }

    private void loadStat(WritingStat stat){
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

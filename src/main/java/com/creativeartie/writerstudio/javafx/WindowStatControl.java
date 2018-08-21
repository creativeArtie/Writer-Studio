package com.creativeartie.writerstudio.javafx;

import java.time.*;
import javafx.beans.property.*;

import com.creativeartie.writerstudio.lang.markup.*;

class WindowStatControl extends WindowStatView{
    /// %Part 1: Private Fields and Constructor
    private WritingStat writingStat;
    private StatSpanDay currentRecord;

    /// %Part 2: Property Binding

    @Override
    protected void bindWritingStat(ReadOnlyObjectWrapper<WritingStat> prop,
            WriterSceneControl control){
        prop.bind(control.writingStatProperty());
    }

    /// %Part 3: Bind Children Properties

    @Override
    protected void bindChildren(WriterSceneControl control){
        getCalendarPane().setupProperties(this);

        writingStatProperty().addListener((d, o, n) -> listenStat(n));

        getWordSpinner().valueProperty().addListener((d, o, n) -> listenWords(n));
        getHourSpinner().valueProperty().addListener((d, o, n) -> listenHours(n));
        getMinuteSpinner().valueProperty().addListener((d, o, n) -> listenMinutes(n));
    }

    /// %Part 3.1: writingStatProperty()

    private void listenStat(WritingStat stat){
        writingStat = stat;
        if (stat != null){
            stat.addDocEdited(s -> listenStat());
            listenStat();
        }
    }

    private void listenStat(){
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

    /// %Part 3.2: getWordSpinner().valueProperty()

    private void listenWords(int count){
        currentRecord.setPublishGoal(count);
    }

    /// %Part 3.3: getHourSpinner().valueProperty()

    private void listenHours(int hours){
        editTime(hours, getMinuteSpinner().getValue());
    }

    /// %Part 3.4: getMinuteSpinner().valueProperty()

    private void listenMinutes(int minutes){
        editTime(getHourSpinner().getValue(), minutes);
    }

    /// %Part 4: Utilities

    private void editTime(int hours, int minutes){
        Duration goal = Duration.ofHours(hours).plusMinutes(minutes);
        currentRecord.setTimeGoal(goal);
    }
}

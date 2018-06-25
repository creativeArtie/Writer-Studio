package com.creativeartie.writerstudio.stats;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;

import java.util.*;
import java.time.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.main.*;

public class SceneStatsControl extends SceneStatsView{

    protected void listenTable(WritingStat table){
        StatSpanDay record = table.getRecord();
        getWordGoal().getValueFactory().setValue(record.getPublishGoal());
        getHourGoal().getValueFactory().setValue((int)record.getTimeGoal()
            .toHours());
        getMinuteGoal().getValueFactory().setValue((int)record.getTimeGoal()
            .toMinutes() % 60);
    }


    protected void listenWord(boolean foucsed){
        if (! foucsed){
            getStatTable().getRecord().setPublishGoal(getWordGoal().getValue());
        }
    }

    protected void listenHour(boolean foucsed){
        if (! foucsed){
            updateTimeGoal();
        }
    }

    protected void listenMinute(boolean foucsed){
        if (! foucsed){
            updateTimeGoal();
        }
    }

    private void updateTimeGoal(){
        int hours = getHourGoal().getValue();
        int minutes = getMinuteGoal().getValue();
        Duration duration = Duration.parse("PT" + hours + "H" + minutes + "M");
        getStatTable().getRecord().setTimeGoal(duration);
    }
}

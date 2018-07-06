package com.creativeartie.writerstudio.javafx;

import java.util.*;
import java.time.*;
import javafx.scene.image.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

public class WindowStatDayControl extends WindowStatDayView{

    private final ImageView passAll;
    private final ImageView passWord;
    private final ImageView passTime;
    private final ImageView failAll;
    private WritingStat writingStat;

    WindowStatDayControl(){
        passAll = buildImage(ButtonIcon.GOAL_ALL);
        passWord = buildImage(ButtonIcon.GOAL_WORD);
        passTime = buildImage(ButtonIcon.GOAL_TIME);
        failAll = buildImage(ButtonIcon.GOAL_FAIL);
    }

    @Override
    protected void setupChildern(WindowStatControl control){
        control.writingStatProperty().addListener((d, o, n) -> setStat(n));
        showDateProperty().addListener((d, o, n) -> setDay(n));
    }

    private void setStat(WritingStat stat){
        writingStat = stat;
        if (stat != null){
            stat.addDocEdited(s -> setDay(getShowDay()));
        }
        setDay(getShowDay());
    }

    private void setDay(LocalDate date){
        getDayLabel().setText(date == null? "": date.getDayOfMonth() + "");
        if (writingStat == null || date == null){
            getStatLabel().setGraphic(null);
            getStatTip().setText(null);
            return;
        }
        Optional<StatSpanDay> stat = writingStat.getRecord(date);
        if (! stat.isPresent()){
            if (date.isBefore(writingStat.getFirstRecord().getRecordDate()) ||
                date.isAfter(LocalDate.now())
            ){
                getStatLabel().setGraphic(null);
                getStatTip().setText(null);
                return;
            }

            getStatLabel().setGraphic(failAll);
            getStatTip().setText(WindowText.CALENDAR_NO_RECORD.getText());
            return;
        }

        StatSpanDay record = stat.get();
        int written = record.getPublishWritten();
        int goal = record.getPublishGoal();
        Duration dur = record.getWriteTime();
        Duration timeGoal = record.getTimeGoal();
        boolean time = dur.toMinutes() >= record.getTimeGoal().toMinutes();
        getStatLabel().setGraphic(written >= record.getPublishGoal()?
            (time? passAll: passWord) :
            (time? passTime: failAll)
        );

        String tip = String.format("Written: %,d(%,d)\nTime: %s (%s)",
            written, goal, formatDuration(dur), formatDuration(timeGoal));
        getStatTip().setText(tip);
    }

    private static ImageView buildImage(ButtonIcon icon){
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
}

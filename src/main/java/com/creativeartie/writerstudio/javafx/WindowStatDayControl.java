package com.creativeartie.writerstudio.javafx;

import java.util.*;
import java.time.*;
import javafx.scene.image.*;

import com.creativeartie.writerstudio.lang.markup.*;
// import com.creativeartie.writerstudio.resource.*;
import com.creativeartie.writerstudio.javafx.utils.*;
import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    WindowStatChildContants.*;

public class WindowStatDayControl extends WindowStatDayView{

    private final ImageView passAll;
    private final ImageView passWord;
    private final ImageView passTime;
    private final ImageView failAll;
    private WritingStat writingStat;

    WindowStatDayControl(){
        passAll = buildImage(ImageIcon.GOAL_ALL);
        passWord = buildImage(ImageIcon.GOAL_WORD);
        passTime = buildImage(ImageIcon.GOAL_TIME);
        failAll = buildImage(ImageIcon.GOAL_FAIL);
    }

    @Override
    protected void setupChildern(WindowStatControl control){
        control.writingStatProperty().addListener((d, o, n) -> loadStat(n));
        showDateProperty().addListener((d, o, n) -> showDay(n));
    }

    private void loadStat(WritingStat stat){
        writingStat = stat;
        if (stat != null){
            stat.addDocEdited(s -> showDay(getShowDay()));
        }
        showDay(getShowDay());
    }

    private void showDay(LocalDate date){
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
            getStatTip().setText(NO_RECORD);
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

        String tip = String.format(TIP_FORMAT, written, goal, formatDuration(dur),
            formatDuration(timeGoal));
        getStatTip().setText(tip);
    }

    private static ImageView buildImage(ImageIcon icon){
        ImageView image = icon.getIcon();
        image.setFitHeight(50);
        image.setFitWidth(50);
        return image;
    }

    private static String formatDuration(Duration duration){
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format(TIME_FORMAT, hours, minutes, seconds);
    }
}

package com.creativeartie.writerstudio.javafx;

import java.time.*;
import java.util.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

class WindowStatMonthControl extends WindowStatMonthView{
    private WritingStat writingStat;

    @Override
    protected void setupChildern(WindowStatControl control){
        control.writingStatProperty().addListener((d, o, n) -> loadStat(n));
        currentMonthProperty().addListener((d, o, n) -> showMonth(n));
        getFirstButton().setOnAction(e -> toFirstMonth());
        getPastButton().setOnAction(e -> toPastMonth());
        getNextButton().setOnAction(e -> toNextMonth());
        getEndButton().setOnAction(e -> toEndMonth());

        for (WindowStatDayControl day: getDayPanes()){
            day.setupProperties(control);
        }
    }

    private void loadStat(WritingStat stats){
        writingStat = stats;
        if (stats != null){
            setCurrentMonth(stats.getEndMonth());
        }
    }

    private void showMonth(YearMonth month){
        String header = WindowText.getText(month.getMonth());
        getYearMonthLabel().setText(header + " " + month.getYear());

        if (writingStat != null){
            boolean last = writingStat.getEndMonth().equals(month);
            getEndButton().setDisable(last);
            getNextButton().setDisable(last);

            boolean first = writingStat.getStartMonth().equals(month);
            getFirstButton().setDisable(first);
            getPastButton().setDisable(first);
        } else {
            return;
        }

        LocalDate date = month.atDay(1);
        Iterator<WindowStatDayControl> panes = getDayPanes().iterator();
        for (DayOfWeek day: DayOfWeek.values()){
            if (day == date.getDayOfWeek()){
                break;
            }
            panes.next().setShowDay(null);
        }

        while (date.getMonth() == month.getMonth() &&
                date.getYear() == month.getYear()){
            panes.next().setShowDay(date);
            date = date.plusDays(1);
        }

        while (panes.hasNext()){
            panes.next().setShowDay(null);
        }


    }

    private void toFirstMonth(){
        if (writingStat != null){
            setCurrentMonth(writingStat.getStartMonth());
        }
    }

    private void toPastMonth(){
        if (writingStat != null){
            setCurrentMonth(getCurrentMonth().minusMonths(1));
        }
    }

    private void toNextMonth(){
        if (writingStat != null){
            setCurrentMonth(getCurrentMonth().plusMonths(1));
        }
    }

    private void toEndMonth(){
        if (writingStat != null){
            setCurrentMonth(writingStat.getEndMonth());
        }
    }
}

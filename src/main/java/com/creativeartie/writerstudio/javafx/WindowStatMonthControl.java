package com.creativeartie.writerstudio.javafx;

import java.time.*;
import java.time.format.*;
import java.util.*;

import com.creativeartie.writerstudio.lang.markup.*;
import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    WindowStatChildContants.*;

class WindowStatMonthControl extends WindowStatMonthView{
    /// %Part 1: Private Fields and Constructor

    private WritingStat writingStat;

    WindowStatMonthControl(){}

    /// %Part 2: Property Binding
    /// %Part 3: Bind Children Properties

    @Override
    protected void bindChildren(WindowStatControl control){
        for (WindowStatDayControl day: getDayPanes()){
            day.postLoad(control);
        }

        control.writingStatProperty().addListener((d, o, n) -> listenStats(n));
        currentMonthProperty().addListener((d, o, n) -> listenMonth(n));

        getFirstButton().setOnAction(e -> listenFirstButton());
        getPastButton().setOnAction(e -> listenPastButton());
        getNextButton().setOnAction(e -> listenNextButton());
        getEndButton().setOnAction(e -> listenEndButton());
    }

    /// %Part 3.1: control.writingStatProperty()

    private void listenStats(WritingStat stats){
        writingStat = stats;
        if (stats != null){
            setCurrentMonth(stats.getEndMonth());
        }
    }

    /// %Part 3.2: currentMonthProperty()

    private void listenMonth(YearMonth month){
        getYearMonthLabel().setText(month.format(DateTimeFormatter
            .ofPattern(YEAR_MONTH_FORMAT)
        ));

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

    /// %Part 3.3: getFirstButton().setOnAction(...)

    private void listenFirstButton(){
        if (writingStat != null){
            setCurrentMonth(writingStat.getStartMonth());
        }
    }

    /// %Part 3.4: getPastButton().setOnAction(...)

    private void listenPastButton(){
        if (writingStat != null){
            setCurrentMonth(getCurrentMonth().minusMonths(1));
        }
    }

    /// %Part 3.5: getNextButton().setOnAction(...)

    private void listenNextButton(){
        if (writingStat != null){
            setCurrentMonth(getCurrentMonth().plusMonths(1));
        }
    }

    /// %Part 3.6: getEndButton().setOnAction(...)

    private void listenEndButton(){
        if (writingStat != null){
            setCurrentMonth(writingStat.getEndMonth());
        }
    }
}

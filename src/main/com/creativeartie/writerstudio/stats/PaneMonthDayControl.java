package com.creativeartie.writerstudio.stats;

import java.time.*;

public class PaneMonthDayControl extends PaneMonthDayView{
    protected void listenDate(LocalDate value){
        if (value == null){
            getDayLabel().setText("");
        } else {
            getDayLabel().setText(value.getDayOfMonth() + "");
        }
    }
}
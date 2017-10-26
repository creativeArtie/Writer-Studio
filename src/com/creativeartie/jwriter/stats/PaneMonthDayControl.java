package com.creativeartie.jwriter.stats;

import java.time.*;

import com.creativeartie.jwriter.property.*;

public class PaneMonthDayControl extends PaneMonthDayView{
    protected void listenDate(LocalDate value){
        if (value == null){
            getDayLabel().setText("");
        } else {
            getDayLabel().setText(value.getDayOfMonth() + "");
        }
    }
}
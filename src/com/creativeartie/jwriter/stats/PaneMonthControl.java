package com.creativeartie.jwriter.stats;

import java.time.*;
import java.util.*;
import java.util.function.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.file.*;
import com.creativeartie.jwriter.property.*;

public class PaneMonthControl extends PaneMonthView{

    protected void listenRecords(RecordTable value){
        List<YearMonth> months = value.getYearMonths();
        updateStartEnd(value.getStartMonth(), value.getEndMonth());
        setYearMonth(months.get(months.size() - 1));
    }

    protected void listenMonth(YearMonth value){
        String month = getString(Utilities.enumToKey(value.getMonth().name()));
        getHeadLabel().setText(month + " " + value.getYear());
        LocalDate day = value.atDay(1);

        int ptr = day.getDayOfWeek().getValue()  - 1;
        for (int i = 0; i < ptr; i++){
            getDayPane(i).clearDay();
        }

        Iterator<Record> it = getRecordTable().getMonth(value);
        YearMonth check = getRecordTable().getStartMonth();
        BiConsumer<Integer, LocalDate> noRecord = (i, date) -> 
            getDayPane(i).noRecord(date);
        BiConsumer<Integer, LocalDate> emptyDay = (i, date) -> 
            getDayPane(i).emptyDay(date);
        // System.out.println(ptr); /// Print start of month
        ptr = getRecordTable().getStartMonth().equals(value)?
            (getRecordTable().getEndMonth().equals(value)?
                loadRecord(it, emptyDay, emptyDay, day, ptr):
                loadRecord(it, emptyDay, noRecord, day, ptr)):
            (getRecordTable().getEndMonth().equals(value)?
                loadRecord(it, noRecord, emptyDay, day, ptr):
                loadRecord(it, noRecord, noRecord, day, ptr)); 

        for(;ptr < getDayPaneLength(); ptr++){
            getDayPane(ptr).clearDay();
        }
    }
    
    private int loadRecord(Iterator<Record> it, 
        BiConsumer<Integer, LocalDate> before, 
        BiConsumer<Integer, LocalDate> after, LocalDate day, int ptr)
    {
        /// code comment out to show different sub stages
        Month check = day.getMonth();
        if (it.hasNext()){
            Record record = it.next();
            
            /// Before first record
            while (! day.equals(record.getRecordDate())){
                // System.out.println(day + " start");
                before.accept(ptr++, day);
                day = day.plusDays(1);
            }
            
            /// Load the first record
            // System.out.println(day + " first " + record);
            getDayPane(ptr++).withRecord(record);
            day = day.plusDays(1);
            
            /// interate the rest
            while(it.hasNext()){
                record = it.next();
                while (! day.equals(record.getRecordDate())){
                    // System.out.println(day + " false " + record);
                    getDayPane(ptr++).noRecord(day);
                    day = day.plusDays(1);
                }
                // System.out.println(day + " true  " + record);
                getDayPane(ptr++).withRecord(record);
                day = day.plusDays(1);
            }
        }
        
        ///After the last record
        while (day.getMonth() == check){
            // System.out.println(day + " end");
            after.accept(ptr++, day);
            day = day.plusDays(1);
        }
        return ptr;
    }
}

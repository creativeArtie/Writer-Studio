package com.creativeartie.jwriter.file;

import java.util.*;
import java.time.*;
import java.io.*;
import java.util.function.*;

import com.google.common.collect.*;

/**
 * A list of {@link Record} and methods to save and edit with today's Record.
 */
public class RecordList extends ForwardingList<Record>{

    @Deprecated
    public static RecordList build(File file) throws IOException{
        return new RecordList(file);
    }

    private final ArrayList<Record> recordList;

    RecordList(){
        recordList = new ArrayList<>();
        recordList.add(Record.firstRecord());
    }

    RecordList(String text){
        recordList = new ArrayList<>();
        fillData(new Scanner(text));
    }

    @Deprecated
    RecordList(File file) throws IOException{
        recordList = new ArrayList<>();
        try (Scanner data = new Scanner(file)){
            fillData(data);
        }
    }

    private void fillData(Scanner data){
        int written = 0;
        Record current = null;
        while (data.hasNextInt()){
            current = Record.builder(current)
                .setRecordDate(LocalDate.ofYearDay(
                    data.nextInt(), data.nextInt())
                )
                .setPublishTotal(data.nextInt())
                .setNoteTotal(data.nextInt())
                .setWriteDuration(Duration.parse(data.next()))
                .setPublishGoal(data.nextInt())
                .setTimeGoal(Duration.parse(data.next()))
                .build();
            recordList.add(current);
        }
        updateRecord();
    }

    public String getSaveText(){
        StringBuilder ans = new StringBuilder();
        for (Record out: this){
            ans.append(out.getRecordDate().getYear()).append(" ");
            ans.append(out.getRecordDate().getDayOfYear()).append(" ");
            ans.append(out.getPublishTotal()).append(" ");
            ans.append(out.getNoteTotal()).append(" ");
            ans.append(out.getWriteTime()).append(" ");
            ans.append(out.getPublishGoal()).append(" ");
            ans.append(out.getTimeGoal()).append("\n");
        }
        return ans.toString();
    }

    public void saveRecords(File file) throws Exception{
        try (PrintWriter output = new PrintWriter(file)){
            output.println(getSaveText());
        }
    }

    public Record getRecord(){
        assert ! recordList.isEmpty() : "Record list should be empty";
        return recordList.get(recordList.size() - 1);
    }

    public void startWriting(int publish, int note){
        updateRecord();
        getRecord().startWriting(publish, note);
    }

    public void stopWriting(int publish, int note){
        updateRecord();
        getRecord().stopWriting(publish, note);
    }

    private void updateRecord(){
        Record record = getRecord();
        if (!record.getRecordDate().equals(LocalDate.now())){
            recordList.add(Record.newRecord(record));
        }
    }

    public Iterator<Record> getMonth(YearMonth month){
        if (getStartMonth().isAfter(month) || getEndMonth().isBefore(month)){
            return new AbstractIterator<Record>(){
                protected Record computeNext(){
                    return endOfData();
                }
            };
        }

        return new AbstractIterator<Record>(){
            private int ptr = findMonth(month);

            protected Record computeNext(){
                if (ptr == size()){
                    return endOfData();
                }
                Record record = get(ptr);
                LocalDate date = record.getRecordDate();
                if (date.getYear() == month.getYear() &&
                    date.getMonth() == month.getMonth()){
                    ptr++;
                    return record;
                }
                return endOfData();
            }
        };
    }

    private int findMonth(YearMonth month){
        int ptr = 0;
        for(Record record: this){
            LocalDate date = record.getRecordDate();
            if (date.getYear() == month.getYear() &&
                date.getMonth() == month.getMonth()){
                return ptr;
            }
            ptr++;
        }
        return size();
    }

    public YearMonth getStartMonth(){
        LocalDate start = recordList.get(0).getRecordDate();
        return YearMonth.of(start.getYear(), start.getMonth());
    }

    public YearMonth getEndMonth(){
        LocalDate end = recordList.get(size() - 1).getRecordDate();
        return YearMonth.of(end.getYear(), end.getMonth());
    }

    public List<YearMonth> getYearMonths(){
        ImmutableList.Builder<YearMonth> builder = ImmutableList.builder();
        YearMonth ptr = getStartMonth();
        YearMonth last = getEndMonth();
        builder.add(ptr);
        while(! ptr.equals(last)){
            ptr = ptr.plusMonths(1);
            builder.add(ptr);
        }
        return builder.build();
    }

    @Override
    protected List<Record> delegate(){
        return ImmutableList.copyOf(recordList);
    }
}
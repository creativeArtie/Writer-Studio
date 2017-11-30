package com.creativeartie.jwriter.file;

import java.util.*;
import java.time.*;

import com.google.common.collect.*;
import static com.google.common.base.Preconditions.*;

/**
 * A list of {@link Record} and methods to save and edit with today's Record.
 */
public final class RecordList extends ForwardingList<Record>{

    @Deprecated
    public static RecordList build(java.io.File file)
            throws java.io.IOException{
        return new RecordList(file);
    }

    private final ArrayList<Record> recordList;

    /** Creates a new {@link RecordList}.*/
    RecordList(){
        recordList = new ArrayList<>();
        recordList.add(Record.firstRecord());
    }

    /** Loads the text from a String.*/
    RecordList(String text){
        checkNotNull(text, "Record text cannot be null.");

        recordList = new ArrayList<>();
        fillData(new Scanner(text));
    }

    /** Loads the text from a test file.*/
    @Deprecated
    RecordList(java.io.File file) throws java.io.IOException{
        checkNotNull(file, "Record text cannot be null.");

        recordList = new ArrayList<>();
        try (Scanner data = new Scanner(file)){
            fillData(data);
        }
    }

    /**
     * Fill record list from a {@linkplain Scanner}. Helper method of
     * {@link #RecordList(String)} and {@link #RecordList(File)}.
     */
    private void fillData(Scanner data){
        assert data != null: "Null data";

        int written = 0;
        Record current = null;
        while (data.hasNextInt()){
            /// For each line:
            current = Record.builder(current, LocalDate.ofYearDay(
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

        // Add today record, if neeeded.
        updateRecord(current.getPublishTotal(), current.getNoteTotal());
    }

    /** Get the save text to put into a file. */
    String getSaveText(){
        StringBuilder ans = new StringBuilder();
        for (Record out: this){
            /// For each record:
            ans.append(out.getRecordDate().getYear())     .append(" ");
            ans.append(out.getRecordDate().getDayOfYear()).append(" ");
            ans.append(out.getPublishTotal())             .append(" ");
            ans.append(out.getNoteTotal())                .append(" ");
            ans.append(out.getWriteTime())                .append(" ");
            ans.append(out.getPublishGoal())              .append(" ");
            ans.append(out.getTimeGoal())                 .append("\n");
        }
        return ans.toString();
    }

    /** Get the current record. */
    public Record getRecord(){
        assert ! recordList.isEmpty() : "empty recordList";
        return recordList.get(recordList.size() - 1);
    }

    /**
     * Start the record timer for the record. If the date have change,
     * create a new record.
     */
    public void startWriting(int publish, int note){
        updateRecord(publish, note);
        getRecord().startWriting(publish, note);
    }

    /**
     * Stop the record timer for the record. If the date have change,
     * create a new record.
     */
    public void stopWriting(int publish, int note){
        updateRecord(publish, note);
        getRecord().stopWriting(publish, note);
    }

    /**
     * Check if last record is today. If it is not, stop the record's
     * timer, and creat a new record.
     */
    private void updateRecord(int publish, int note){
        Record record = getRecord();
        if (!record.getRecordDate().equals(LocalDate.now())){
            record.stopWriting(publish, note);
            recordList.add(Record.newRecord(record));
        }
    }

    /** Get all the found records in a {@link YearMonth month}. */
    public Iterator<Record> getMonth(YearMonth month){
        checkNotNull(month, "Month cannot be null.");

        /// Finds no {@link Record records}.
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
                /// This is the last record.
                if (ptr >= size()){
                    return endOfData();
                }

                /// Get and check the next record
                Record record = get(ptr);
                LocalDate date = record.getRecordDate();
                if (date.getYear()  == month.getYear() &&
                    date.getMonth() == month.getMonth())
                {
                    ptr++;
                    return record;
                }
                return endOfData();
            }
        };
    }

    /**
     * Find the index of the first record in a month. Helper Method of
     * {@link #getMonth(YearMonth)}'s second
     * {@link AbstractIterator#computeNext}.
     */
    private int findMonth(YearMonth month){
        assert month != null: "Null Month";
        int ptr = 0;
        for(Record record: this){
            LocalDate date = record.getRecordDate();
            if (date.getYear()  == month.getYear() &&
                date.getMonth() == month.getMonth())
            {
                return ptr;
            }
            ptr++;
        }
        return size();
    }

    /* Get the {@link YearMonth} of the first record. */
    public YearMonth getStartMonth(){
        LocalDate start = recordList.get(0).getRecordDate();
        return YearMonth.of(start.getYear(), start.getMonth());
    }

    /* Get the {@link YearMonth} of the last record. */
    public YearMonth getEndMonth(){
        LocalDate end = recordList.get(size() - 1).getRecordDate();
        return YearMonth.of(end.getYear(), end.getMonth());
    }

    @Override
    protected List<Record> delegate(){
        return ImmutableList.copyOf(recordList);
    }
}
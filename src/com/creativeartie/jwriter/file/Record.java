package com.creativeartie.jwriter.file;

import java.util.*;
import java.util.Optional;
import java.time.*;

import com.google.common.base.*;
import static com.google.common.base.Preconditions.*;

/**
 * A single line of record with date, with word counts and goals. Instances of
 * this class is created through {@link RecordList}.
 */
public final class Record{

    static Record firstRecord(){
        return new Record().new Builder().build();
    }

    static Record newRecord(Record last){
        return new Record().new Builder(last)
            .setTimeGoal(last.timeGoal)
            .setPublishGoal(last.publishGoal)
            .setPublishTotal(last.publishTotal)
            .setNoteTotal(last.noteTotal)
            .build();
    }

    static Builder builder(Record last){
        return new Record().new Builder(last);
    }

    private LocalDate recordDate;

    private Duration writeTime;
    private Duration timeGoal;
    private Optional<LocalDateTime> timeStarted;

    private int publishGoal;
    private int publishTotal;
    private int noteTotal;

    private Optional<Record> lastRecord;

    /**
     * Builder class for a record.
     */
    final class Builder{
        private Builder(){
            this (null, LocalDate.now());
        }

        private Builder(Record record){
            this (record, LocalDate.now());
        }

        private Builder(Record record, LocalDate date){
            assert date != null: "Null date.";

            lastRecord = Optional.ofNullable(record);
            recordDate = LocalDate.now();

            timeGoal = Duration.ofMinutes(30);
            publishGoal = 50;
            publishTotal = 0;
            noteTotal = 0;
            writeTime = Duration.ZERO;
            timeStarted = Optional.empty();
        }

        Builder setRecordDate(LocalDate date){
            recordDate = checkNotNull(date, "Date cannot be null.");
            return this;
        }

        Builder setTimeGoal(Duration duration){
            timeGoal = checkNotNull(duration, "Duration cannot be null.");;
            return this;
        }

        Builder setPublishGoal(int goal){
            publishGoal = checkNotNull(goal, "Goal cannot be null.");;
            return this;
        }

        Builder setPublishTotal(int total){
            publishTotal = checkNotNull(total, "Total cannot be null.");
            return this;
        }

        Builder setNoteTotal(int total){
            noteTotal = checkNotNull(total, "Total cannot be null.");;
            return this;
        }

        public Builder setWriteDuration(Duration time){
            writeTime = checkNotNull(time, "Time cannot be null.");
            return this;
        }

        public Record build(){
            return Record.this;
        }
    }

    private Record(){}

    public LocalDate getRecordDate(){
        return recordDate;
    }

    /** Returns the total words target for publishing. */
    public int getPublishTotal(){
        return publishTotal;
    }

    /** Returns the daily words target for publishing. */
    public int getPublishGoal(){
        return publishGoal;
    }

    /** Set the daily words target for publishing. */
    public void setPublishGoal(int goal){
        publishGoal = goal;
    }

    /** Get the day tally words written for publishing.*/
    public int getPublishWritten(){
        return lastRecord.map(record -> publishTotal - record.publishTotal)
            .orElse(publishTotal);
    }

    /** Returns the total words target for planning and research. */
    public int getNoteTotal(){
        return noteTotal;
    }

    /** Returns the total word count for everything */
    public int getTotalCount(){
        return publishTotal + noteTotal;
    }

    public Duration getWriteTime(){
        return writeTime;
    }

    public Duration getTimeGoal(){
        return timeGoal;
    }

    public void setTimeGoal(Duration goal){
        timeGoal = goal;
    }

    void startWriting(int publish, int note){
        if (! timeStarted.isPresent()){
            timeStarted = Optional.of(LocalDateTime.now());
        }
        updateRecord(publish, note);
    }

    void stopWriting(int publish, int note){
        timeStarted.ifPresent(time ->
            writeTime = writeTime.plus(Duration.between(time,
                LocalDateTime.now())));
        timeStarted = Optional.empty();
        updateRecord(publish, note);
    }

    private void updateRecord(int publish, int note){
        publishTotal = publish;
        noteTotal = note;
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
            .addValue(recordDate)
            .add("publish", publishTotal)
            .add("note", noteTotal)
            .add("total", getTotalCount())
            .add("timer start", timeStarted)
            .add("duration", writeTime)
            .add("duration goal", timeGoal)
            .toString();
    }
}
package com.creativeartie.jwriter.file;

import java.util.Optional;
import java.time.*;
import com.google.common.base.*;
import java.util.*;

/**
 * A single line of record with date, publish count, and note count.
 */
public final class Record{
    private LocalDate recordDate;
    private Duration timeGoal;
    private int publishGoal;
    private int publishCount;
    private int noteCount;
    private Optional<Record> lastRecord;
    private Duration writeDuration;
    private Optional<LocalDateTime> markedTime;

    class Builder{
        private Builder(){
            this (null, LocalDate.now());
        }

        private Builder(Record record){
            this (record, LocalDate.now());
        }

        private Builder(Record record, LocalDate date){
            recordDate = LocalDate.now();
            timeGoal = Duration.ofMinutes(30);
            publishGoal = 50;
            publishCount = 0;
            noteCount = 0;
            lastRecord = Optional.ofNullable(record);
            writeDuration = Duration.ZERO;
            markedTime = Optional.empty();
        }

        public Builder setRecordDate(LocalDate date){
            recordDate = date;
            return this;
        }

        public Builder setTimeGoal(Duration duration){
            timeGoal = duration;
            return this;
        }

        public Builder setPublishGoal(int goal){
            publishGoal = goal;
            return this;
        }

        public Builder setPublishCount(int count){
            publishCount = count;
            return this;
        }

        public Builder setNoteCount(int count){
            noteCount = count;
            return this;
        }

        public Builder setWriteDuration(Duration time){
            writeDuration = time;
            return this;
        }

        public Record build(){
            return Record.this;
        }
    }

    public static Record firstRecord(){
        return new Record().new Builder().build();
    }

    public static Record nextRecord(Record last){
        return new Record().new Builder(last)
            .setTimeGoal(last.timeGoal)
            .setPublishGoal(last.publishGoal)
            .setPublishCount(last.publishCount)
            .setNoteCount(last.noteCount)
            .build();
    }

    public static Builder builder(Record last){
        return new Record().new Builder(last);
    }

    private Record(){}

    public LocalDate getRecordDate(){
        return recordDate;
    }

    public int getPublishCount(){
        return publishCount;
    }

    public int getPublishGoal(){
        return publishGoal;
    }

    public int getNoteCount(){
        return noteCount;
    }

    public int getTotalCount(){
        return publishCount + noteCount;
    }

    public Duration getWriteDuration(){
        return writeDuration;
    }

    public long getWriteMinutes(){
        return writeDuration.toMinutes();
    }

    void startWriting(int publish, int note){
        if (! markedTime.isPresent()){
            markedTime = Optional.of(LocalDateTime.now());
        }
        updateRecord(publish, note);
    }

    void stopWriting(int publish, int note){
        markedTime.ifPresent(time ->
            writeDuration = writeDuration.plus(Duration.between(time,
                LocalDateTime.now())));
        markedTime = Optional.empty();
        updateRecord(publish, note);
    }

    private void updateRecord(int publish, int note){
        publishCount = publish;
        noteCount = note;
    }

    public int getPublishWritten(){
        return lastRecord.map(record -> publishCount - record.publishCount)
            .orElse(publishCount);
    }

    public void setPublishGoal(int goal){
        publishGoal = goal;
    }

    public Duration getTimeGoal(){
        return timeGoal;
    }

    public void setTimeGoal(Duration goal){
        timeGoal = goal;
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
            .addValue(recordDate)
            .add("publish", publishCount)
            .add("note", noteCount)
            .add("total", getTotalCount())
            .add("timer start", markedTime)
            .add("duration", writeDuration)
            .add("duration goal", timeGoal)
            .toString();
    }
}
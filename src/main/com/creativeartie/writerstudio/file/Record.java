package com.creativeartie.writerstudio.file;

import java.util.Optional; // Optional
import java.time.*; // Duration, LocalDate, LocalDateTime

import com.google.common.base.*; // MoreObjects

import static com.creativeartie.writerstudio.main.Checker.*;

/**
 * A single line of record with date, with word counts and goals. Instances of
 * this class is created through {@link RecordList}.
 */
public final class Record{

    /** Creates the first {@link Record} of a {@link RecordList}. */
    static Record firstRecord(){
        return new Record(LocalDate.now()).new Builder(null).build();
    }

    /** Creates the next {@link Record} of a {@link RecordList}. */
    static Record newRecord(Record last){
        return new Record(LocalDate.now())
            .new Builder(last)
            .setTimeGoal(last.timeGoal)
            .setPublishGoal(last.publishGoal)
            .setPublishTotal(last.publishTotal)
            .setNoteTotal(last.noteTotal)
            .build();
    }

    /** Creates a {@link Builder} to load {@link Record} from file. */
    static Builder builder(Record last, LocalDate date){
        return new Record(checkNotNull(date, "date"))
            .new Builder(last);
    }

    private final LocalDate recordDate;

    /// Goal and counting writing time
    private Duration writeTime;
    private Duration timeGoal;
    private Optional<LocalDateTime> timeStarted;

    /// Goal and counting words
    private int publishGoal;
    private int publishTotal;
    private int noteTotal;

    /// Used to calulate the day's count.
    private Optional<Record> lastRecord;

    /**
     * Builder class to create a {@link Record}.
     */
    final class Builder{

        /** {@linkplain Record.Builder}'s constructor.*/
        private Builder(Record record){
            // nullable record

            lastRecord = Optional.ofNullable(record);

            timeGoal = Duration.ofMinutes(30);
            publishGoal = 50;
            publishTotal = 0;
            noteTotal = 0;
            writeTime = Duration.ZERO;
            timeStarted = Optional.empty();
        }

        Builder setTimeGoal(Duration duration){
            timeGoal = checkNotNull(duration, "duration");;
            return this;
        }

        Builder setPublishGoal(int goal){
            publishGoal = checkGreater(goal, "goal", 0, true);;
            return this;
        }

        Builder setPublishTotal(int total){
            publishTotal = checkGreater(total, "total", 0, true);
            return this;
        }

        Builder setNoteTotal(int total){
            noteTotal = checkGreater(total, "total", 0, true);
            return this;
        }

        public Builder setWriteDuration(Duration time){
            writeTime = checkNotNull(time, "time");
            return this;
        }

        public Record build(){
            return Record.this;
        }
    }

    /** {@linkplain Record}'s constructor.*/
    private Record(LocalDate date){
        assert date != null: "Null date.";
        recordDate = date;
    }

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
        publishGoal = checkGreater(goal, "goal", 0, true);
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
        checkNotNull(goal, "goal");
        timeGoal = goal;
    }

    /**
     * Start the record timer for the record. If the time has already started,
     * only update the publish and note word count.
     */
    void startWriting(int publish, int note){
        checkGreater(publish, "publish", 0, true);
        checkGreater(note, "note", 0, true);
        if (! timeStarted.isPresent()){
            timeStarted = Optional.of(LocalDateTime.now());
        }
        updateRecord(publish, note);
    }

    /**
     * Stop the record timer for the record. If the time has already stop,
     * only update the publish and note word count.
     */
    void stopWriting(int publish, int note){
        checkGreater(publish, "publish", 0, true);
        checkGreater(note, "note", 0, true);
        timeStarted.ifPresent(time ->
            writeTime = writeTime.plus(
                Duration.between(time, LocalDateTime.now())
            )
        );
        timeStarted = Optional.empty();
        updateRecord(publish, note);
    }

    /// Helper method of {@link startWriting(int, int)} and
    /// {@link stopWriting(int, int)}
    private void updateRecord(int publish, int note){
        assert publish >= 0: "Off range publish";
        assert note >= 0: "Off range note";
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
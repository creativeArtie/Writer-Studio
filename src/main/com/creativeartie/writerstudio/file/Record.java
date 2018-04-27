package com.creativeartie.writerstudio.file;

import java.time.*; // Duration, LocalDate, LocalDateTime;
import java.util.Optional;

import com.google.common.base.*; // MoreObjects;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A single record with data, word count and goals.
 *
 * Purpose:
 * <ul>
 * <li> Assoicate data with a date </li>
 * <li> Calculate duration with start and stop methods </li>
 * </ul>
 */
public final class Record{

    /// %Part 1: Constructors and Fields #######################################
    /// %Part 1.1: Package access API ==========================================

    /** Creates the first {@link Record}.
     * @return answer
     * @see RecordList#RecordList()
     */
    static Record firstRecord(){
        return new Record(LocalDate.now()).new Builder(null).build();
    }

    /** Creates the next {@link Record}.
     * @param last
     *      the last record
     * @return answer
     * @see #updateRecord(int, int)
     */
    static Record newRecord(Record last){
        argumentNotNull(last, "last");

        return new Record(LocalDate.now())
            .new Builder(last)
            .setTimeGoal(last.timeGoal)
            .setPublishGoal(last.publishGoal)
            .setPublishTotal(last.publishTotal)
            .setNoteTotal(last.noteTotal)
            .build();
    }

    /** Creates a {@link Builder} to load a {@link Record}.
     *
     * @param last
     *      last record in {@link RecordList}
     * @param date
     *      the date of the record
     * @see RecordList#fillData(Scanner)
     */
    static Builder builder(Record last, LocalDate date){
        argumentNotNull(date, "date");
        return new Record(date).new Builder(last);
    }


    /** Builder class to create a {@link Record}.
     * @see RecordList
     */
    final class Builder{

        /** Creates {@linkplain Record.Builder} with a previous record.
         *
         * @param record
         *      nullable last record
         * @see #firstRecord()
         * @see #newRecord(Record)
         * @see #builder(Record, LocalDate)
         */
        private Builder(Record record){
            timeGoal = Duration.ofMinutes(30);
            timeTotal = Duration.ZERO;
            timeStarted = Optional.empty();

            publishGoal = 50;
            publishTotal = 0;
            noteTotal = 0;

            lastRecord = Optional.ofNullable(record);
        }

        /** Set the time goal.
         * @param duration
         *      write duration goal
         * @return self
         * @see RecordList#fillData(Scanner)
         * @see #newRecord(Record)
         */
        Builder setTimeGoal(Duration duration){
            timeGoal = argumentNotNull(duration, "duration");;
            return this;
        }

        /** Set the publish goal.
         *
         * @param goal
         *      publish goal
         * @return self
         * @see RecordList#fillData(Scanner)
         * @see #newRecord(Record)
         */
        Builder setPublishGoal(int goal){
            publishGoal = argumentAtLeast(goal, "goal", 0);
            return this;
        }

        /** Set the publish total.
         *
         * @param total
         *      publish total
         * @return self
         * @see RecordList#fillData(Scanner)
         * @see #newRecord(Record)
         */
        Builder setPublishTotal(int total){
            publishTotal = argumentAtLeast(total, "total", 0);
            return this;
        }

        /** Set the note total.
         *
         * @param total
         *      note total
         * @return self
         * @see RecordList#fillData(Scanner)
         * @see #newRecord(Record)
         */
        Builder setNoteTotal(int total){
            noteTotal = argumentAtLeast(total, "total", 0);
            return this;
        }

        /** Set the writing duration.
         *
         * @param time
         *      writing duration
         * @return self
         * @see RecordList#fillData(Scanner)
         * @see #newRecord(Record)
         */
        Builder setWriteDuration(Duration time){
            timeTotal = argumentNotNull(time, "time");
            return this;
        }

        /** Creates the {@link Record}.
         *
         * @return answer
         * @see RecordList#fillData(Scanner)
         * @see #newRecord(Record)
         */
        Record build(){
            return Record.this;
        }
    }

    /// %Part 1.2: Instance Setup ==============================================
    private final LocalDate recordDate;

    /// Goal and total writing time
    private Duration timeGoal;
    private Duration timeTotal;
    private Optional<LocalDateTime> timeStarted;

    /// Goal and total words
    private int publishGoal;
    private int publishTotal;
    private int noteTotal;

    /// Used to calulate the day's count.
    private Optional<Record> lastRecord;

    /** Creates a {@linkplain Record} with a date
     *
     * @param date
     *      record date
     * @see #firstRecord()
     * @see #newRecord(Record)
     * @see #builder(Record, LocalDate)
     */
    private Record(LocalDate date){
        assert date != null: "Null date.";
        recordDate = date;
    }

    /// %Part 2: Gets and Sets #################################################

    /// %Part 2.1: Record Date =================================================

    /** Gets the record date
     *
     * @return answer
     */
    public LocalDate getRecordDate(){
        return recordDate;
    }

    /// %Part 2.2: Publish Word Count Goal =====================================

    /** Gets the publishing goal word count.
     *
     * @return answer
     */
    public int getPublishGoal(){
        return publishGoal;
    }

    /** Sets the publishing goal word count.
     *
     * @param goal
     *      publishing goal count
     */
    public void setPublishGoal(int goal){
        publishGoal = argumentAtLeast(goal, "goal", 0);
    }

    /// %Part 2.3: Duration Goals ==============================================

    /** Gets the writing time goal.
     *
     * @return answer
     */
    public Duration getTimeGoal(){
        return timeGoal;
    }

    /** Sets the wrting time goal.
     *
     * @param goal
     *      writing time goal
     * @return answer
     */
    public void setTimeGoal(Duration goal){
        timeGoal = argumentNotNull(goal, "goal");
    }

    /// %Part 2.4: Current Word Count and Duation ==============================

    /// %Part 2.4.1: Get Methods -----------------------------------------------

    /** Get the record's publishing count.
     *
     * @return answer
     */
    public int getPublishTotal(){
        return publishTotal;
    }

    /** Get the record's note count.
     *
     * @return answer
     */
    public int getNoteTotal(){
        return noteTotal;
    }

    /** Gets the total word count.
     *
     * @return answer
     */
    public int getTotalCount(){
        return publishTotal + noteTotal;
    }

    /** Gets the write time.
     *
     * @return answer
     */
    public Duration getWriteTime(){
        return timeTotal;
    }

    /** Get the record's publishing count.
     *
     * @return answer
     */
    public int getPublishWritten(){
        return lastRecord.map(record -> publishTotal - record.publishTotal)
            .orElse(publishTotal);
    }


    /// %Part 2.4.2: Set Methods -----------------------------------------------

    /** Starts the record time (as needed) and update counts.
     *
     * @param publish
     *      the publish word count
     * @param note
     *      the note word count
     * @see RecordList#startWriting(WritingText)
     */
    void startWriting(int publish, int note){
        argumentAtLeast(publish, "publish", 0);
        argumentAtLeast(note, "note", 0);

        if (! timeStarted.isPresent()){
            timeStarted = Optional.of(LocalDateTime.now());
        }
        updateRecord(publish, note);
    }

    /** Stops the record time (as needed) and update counts.
     *
     * @param publish
     *      the publish word count
     * @param note
     *      the note word count
     * @see RecordList#stopWriting(WritingText)
     */
    void stopWriting(int publish, int note){
        argumentAtLeast(publish, "publish", 0);
        argumentAtLeast(note, "note", 0);

        timeStarted.ifPresent(time ->
            timeTotal = timeTotal.plus(
                Duration.between(time, LocalDateTime.now())
            )
        );
        timeStarted = Optional.empty();
        updateRecord(publish, note);
    }

    /** Updates the record with publish and note count
     *
     * @param publish
     *      the publish word count
     * @param note
     *      the note word count
     * @see #startWriting(WritingText)
     * @see #stopWriting(WritingText)
     */
    private void updateRecord(int publish, int note){
        assert publish >= 0: "Off range publish";
        assert note >= 0: "Off range note";

        publishTotal = publish;
        noteTotal = note;
    }

    /// %Part 5: Overrides Methods =============================================

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
            .addValue(recordDate)
            .add("publish", publishTotal)
            .add("note", noteTotal)
            .add("total", getTotalCount())
            .add("timer start", timeStarted)
            .add("duration", timeTotal)
            .add("duration goal", timeGoal)
            .toString();
    }
}
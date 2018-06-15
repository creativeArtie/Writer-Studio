package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.time.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** Stores a day's record. */
public final class StatSpanRecord extends SpanBranch{
    private final CacheKeyMain<LocalDate> cacheDate;
    private final CacheKeyMain<Integer> cachePublishGoal;
    private final CacheKeyMain<Integer> cachePublishTotal;
    private final CacheKeyMain<Integer> cacheNoteTotal;
    private final CacheKeyMain<Integer> cacheGrandTotal;
    private final CacheKeyMain<Integer> cachePublishWritten;
    private final CacheKeyMain<Duration> cacheTimeGoal;
    private final CacheKeyMain<Duration> cacheTimeTotal;
    private Optional<LocalTime> timeStarted;

    public StatSpanRecord(List<Span> children){
        super(children);
        cacheDate = new CacheKeyMain<>(LocalDate.class);
        cachePublishGoal = CacheKeyMain.integerKey();
        cachePublishTotal = CacheKeyMain.integerKey();
        cacheNoteTotal = CacheKeyMain.integerKey();
        cacheGrandTotal = CacheKeyMain.integerKey();
        cachePublishWritten = CacheKeyMain.integerKey();
        cacheTimeGoal = new CacheKeyMain<>(Duration.class);
        cacheTimeTotal = new CacheKeyMain<>(Duration.class);
        timeStarted = Optional.empty();
    }

    public LocalDate getRecordDate(){
        return getLocalCache(cacheDate, () -> leafFromFirst(SpanLeafStyle.DATA)
            .map(s -> LocalDate.parse(s.getRaw(), STAT_DATE))
            .orElseThrow(() -> new IllegalStateException("Corrupted date."))
        );
    }

    /// %Part 2.3: Duration Goals ==============================================

    /** Gets the writing time goal.
     *
     * @return answer
     */
    public Duration getTimeGoal(){
        return getLocalCache(cacheTimeGoal, () ->
            Duration.parse(getData(StatTypeKey.TIME_GOAL));
    }

    /** Gets the write time.
     *
     * @return answer
     */
    public Duration getWriteTime(){
        return getLocalCache(cacheTimeTotal, () ->
            Duration.parse(getData(StatTypeKey.TIME_TOTAL));
    }

    /// %Part 2.4: Current Word Count and Duation ==============================

    /// %Part 2.4.1: Get Methods -----------------------------------------------

    /** Gets the publishing goal word count.
     *
     * @return answer
     */
    public int getPublishGoal(){
        return getLocalCache(cachePublishGoal, () ->
            Integer.parse(getData(StatTypeKey.PUBLISH_GOAL));
    }


    /** Get the record's publishing count.
     *
     * @return answer
     */
    public int getPublishTotal(){
        return getLocalCache(cachePublishTotal, () ->
            Integer.parse(getData(StatTypeKey.PUBLISH_TOTAL));
    }

    /** Get the record's note count.
     *
     * @return answer
     */
    public int getNoteTotal(){
        return getLocalCache(cacheNoteTotal, () ->
            Integer.parse(getData(StatTypeKey.NOTE_TOTAL));
    }

    /** Gets the total word count.
     *
     * @return answer
     */
    public int getTotalCount(){
        return getLocalCache(cacheGrandTotal, () ->
            getPublishTotal() + getNoteTotal());
    }

    /** Get the record's publishing count.
     *
     * @return answer
     */
    public int getPublishWritten(){
        return getLocalCache(cachePublishWritten, () -> getPublishTotal() -
            spanBefore(StatSpanRecord.class).map(s -> s.getPublishTotal())
            .orElse(0));
    }

    private String getData(StatTypeKey type){
        List<StatSpanField> spans = getChildren(StatSpanField.class);
        for (StatSpanField span: spans){
            if (span.getDataType() == type){
                return span.getData();
            }
        }
        return "0";
    }



    /// %Part 2.4.2: Set Methods -----------------------------------------------

    /** Sets the wrting time goal.
     *
     * @param goal
     *      writing time goal
     */
    public synchronized void setTimeGoal(Duration goal){
        setData(StatTypeKey.TIME_GOAL, goal);
    }

    /** Sets the publishing goal word count.
     *
     * @param goal
     *      publishing goal count
     */
    public synchronized void setPublishGoal(int goal){
        setData(StatTypeKey.PUBLISH_GOAL, goal);
    }

    private void setData(StatTypeKey type, Object data){
        for (StatSpanField child: getChildren(StatSpanField.class)){
            if (child.getDataType() == type){
                child.setData(data);
                return;
            }
        }
        String symbol = StatParseField.values()[type.ordinal()].getSymbol();
        /// (getRaw() - "\n") "|" + symbol + ":" + data + "\n"
        runCommand(() -> getRaw().substring(
                0, getRaw().length() - SPEC_ROW_END.length()
            ) +
            SPEC_SEPARATOR + symbol + SPEC_KEY_DATA + data +
            SPEC_ROW_END);
    }

    /** Starts the record time (as needed) and update counts.
     *
     * @param publish
     *      the publish word count
     * @param note
     *      the note word count
     * @see WritingStat#startWriting(WritingText)
     */
    synchronized void startWriting(int publish, int note){
        argumentAtLeast(publish, "publish", 0);
        argumentAtLeast(note, "note", 0);
        setFireReady(false);

        if (! timeStarted.isPresent()){
            timeStarted = Optional.of(LocalTime.now());
        }
        updateRecord(publish, note);
        setFireReady(true);
    }

    /** Stops the record time (as needed) and update counts.
     *
     * @param publish
     *      the publish word count
     * @param note
     *      the note word count
     * @see WritingStat#stopWriting(WritingText)
     */
    synchronized void stopWriting(int publish, int note){
        argumentAtLeast(publish, "publish", 0);
        argumentAtLeast(note, "note", 0);
        setFireReady(false);

        timeStarted.ifPresent(time ->
            setData(StatTypeKey.TIME_TOTAL,
                getWriteTime().plus(Duration.between(time, LocalTime.now()))
            )
        );
        timeStarted = Optional.empty();
        updateRecord(publish, note);
        setFireReady(true);
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

        setData(StatTypeKey.PUBLISH_TOTAL, publish);
        setData(StatTypeKey.NOTE_TOTAL, note);
    }

    @Override
    protected final SetupParser getParser(String text){
        return StatParseRecord.PARSER;
    }
}

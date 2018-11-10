package com.creativeartie.writerstudio.lang.markup;

import java.util.*;
import java.time.*;

import com.google.common.collect.*;

import com.creativeartie.writerstudio.lang.*;

import static com.creativeartie.writerstudio.util.ParameterChecker.*;
import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;

/** Stores a day's record. */
public final class StatSpanDay extends SpanBranch{
    private final CacheKeyMain<LocalDate> cacheDate;
    private final CacheKeyMain<Integer> cachePublishGoal;
    private final CacheKeyMain<Integer> cachePublishTotal;
    private final CacheKeyMain<Integer> cacheNoteTotal;
    private final CacheKeyMain<Integer> cacheGrandTotal;
    private final CacheKeyMain<Integer> cachePublishWritten;
    private final CacheKeyMain<Duration> cacheTimeGoal;
    private final CacheKeyMain<Duration> cacheTimeTotal;
    private Optional<LocalTime> timeStarted;

    public StatSpanDay(List<Span> children){
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
            getDurationData(StatTypeData.TIME_GOAL));
    }

    /** Gets the write time.
     *
     * @return answer
     */
    public Duration getWriteTime(){
        return getLocalCache(cacheTimeTotal, () ->
            getDurationData(StatTypeData.TIME_TOTAL));
    }

    private Duration getDurationData(StatTypeData type){
        List<StatSpanDataTime> spans = getChildren(StatSpanDataTime.class);
        for (StatSpanDataTime span: spans){
            if (span.getDataType() == type){
                return span.getData();
            }
        }
        return Duration.ofSeconds(0);
    }

    /// %Part 2.4: Current Word Count and Duation ==============================

    /// %Part 2.4.1: Get Methods -----------------------------------------------

    /** Gets the publishing goal word count.
     *
     * @return answer
     */
    public int getPublishGoal(){
        return getLocalCache(cachePublishGoal, () ->
            getIntData(StatTypeData.PUBLISH_GOAL));
    }


    /** Get the record's publishing count.
     *
     * @return answer
     */
    public int getPublishTotal(){
        return getLocalCache(cachePublishTotal, () ->
            getIntData(StatTypeData.PUBLISH_TOTAL));
    }

    /** Get the record's note count.
     *
     * @return answer
     */
    public int getNoteTotal(){
        return getLocalCache(cacheNoteTotal, () ->
            getIntData(StatTypeData.NOTE_TOTAL));
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
            spanBefore(StatSpanDay.class).map(s -> s.getPublishTotal())
            .orElse(0));
    }

    private int getIntData(StatTypeData type){
        List<StatSpanDataInt> spans = getChildren(StatSpanDataInt.class);
        for (StatSpanDataInt span: spans){
            if (span.getDataType() == type){
                return span.getData();
            }
        }
        return 0;
    }



    /// %Part 2.4.2: Set Methods -----------------------------------------------

    /** Sets the wrting time goal.
     *
     * @param goal
     *      writing time goal
     */
    public void setTimeGoal(Duration goal){
        setTimeData(StatTypeData.TIME_GOAL, goal);
    }

    /** Sets the publishing goal word count.
     *
     * @param goal
     *      publishing goal count
     */
    public void setPublishGoal(int goal){
        setIntegerData(StatTypeData.PUBLISH_GOAL, goal);
    }

    private void setTimeData(StatTypeData type, Duration data){
        for (StatSpanDataTime child: getChildren(StatSpanDataTime.class)){
            if (child.getDataType() == type){
                child.setData(data);
                return;
            }
        }
        addNewColumn(type, data);
    }

    private void setIntegerData(StatTypeData type, int data){
        for (StatSpanDataInt child: getChildren(StatSpanDataInt.class)){
            if (child.getDataType() == type){
                child.setData(data);
                return;
            }
        }
        addNewColumn(type, data);
    }

    private void addNewColumn(StatTypeData type, Object data){
        String symbol = StatParseData.values()[type.ordinal()].getSymbol();
        ///symbol + ":" + data + "|"
        addChild(type.getParser(), symbol + STAT_KEY_DATA + data +
            STAT_SEPARATOR, size() - 1);
    }

    /** Starts the record time (as needed) and update counts.
     *
     * @param publish
     *      the publish word count
     * @param note
     *      the note word count
     * @see WritingStat#startWriting(WritingText)
     */
    void startWriting(int publish, int note){
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
    void stopWriting(int publish, int note){
        argumentAtLeast(publish, "publish", 0);
        argumentAtLeast(note, "note", 0);
        setFireReady(false);

        timeStarted.ifPresent(time ->
            setTimeData(StatTypeData.TIME_TOTAL,
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

        setIntegerData(StatTypeData.PUBLISH_TOTAL, publish);
        setIntegerData(StatTypeData.NOTE_TOTAL, note);
    }

    @Override
    protected final SetupParser getParser(String text){
        return StatParseDay.PARSER;
    }
}

package com.creativeartie.writerstudio.lang.markup;

import com.creativeartie.writerstudio.lang.*;

import java.util.*;
import java.time.*;

import com.google.common.collect.*;

import static com.creativeartie.writerstudio.lang.markup.AuxiliaryData.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Main document that contain the content.
 *
 * Impelments the rule {@code design/ebnf.txt Data}.
 */
public class WritingStat extends Document{

    private static String createNewDay(StatSpanRecord last){
        LocalDate today = LocalDate.now();

        String count = SPEC_SEPARATOR + StatParseField.PUBLISH_GOAL.getSymbol() +
            SPEC_KEY_DATA + (last != null? last.getPublishGoal(): "50");

        String time =  SPEC_SEPARATOR + StatParseField.TIME_GOAL.getSymbol() +
            SPEC_KEY_DATA +
            (last != null? last.getTimeGoal(): Duration.ofMinutes(30));

        return today.format(STAT_DATE) + count + time +
            SPEC_ROW_END;
    }

    private final CacheKeyMain<YearMonth> cacheStart;
    private final CacheKeyMain<YearMonth> cacheEnd;
    private final Map<YearMonth, CacheKeyList<StatSpanRecord>> cacheMonths;
    private final CacheKeyMain<StatSpanRecord> cacheToday;

    /** Creates an empty  {@linkplain WritingStat}.
     *
     * @see WritingFile#newFile()
     */
    WritingStat(){
        this(createNewDay(null));
    }

    /** Creates a {@linkplain WritingText}.
     *
     * @param text
     *      raw content text
     * @see WritingFile#open(File)
     */
    public WritingStat(String text){
        super(text, StatParseRecord.PARSER);
        cacheStart = new CacheKeyMain<>(YearMonth.class);
        cacheEnd = new CacheKeyMain<>(YearMonth.class);
        cacheMonths = new TreeMap<>();
        cacheToday = new CacheKeyMain<>(StatSpanRecord.class);

        checkDay(getRecord().getPublishTotal(), getRecord().getNoteTotal());
    }

    /** Add record if this is not today.
     *
     * @see #WritingStat(String)
     */
    private void checkDay(int publish, int note){
        StatSpanRecord last = getRecord();
        if (! LocalDate.now().equals(last.getRecordDate())){
            System.out.println(last.getTimeGoal());
            last.stopWriting(publish, note);
            System.out.println(last.getTimeGoal());
            runCommand(() -> getRaw() + createNewDay(last));
        }
    }

    public YearMonth getStartMonth(){
        return getLocalCache(cacheStart, () -> getYearMonth(
            spanFromFirst(StatSpanRecord.class)));
    }

    public YearMonth getEndMonth(){
        return getLocalCache(cacheEnd, () -> getYearMonth(
            spanFromLast(StatSpanRecord.class)));
    }

    /** Get the {@linkplain YearMonth} from a {@link StatSpanRecord}.
     *
     * @see #getStartMonth()
     * @see #getEndMonth()
     */
    private YearMonth getYearMonth(Optional<StatSpanRecord> span){
        return  span/// s = StatSpanRecord
            .map(s -> s.getRecordDate())
            /// d = LocalDate
            .map( d -> YearMonth.of(d.getYear(), d.getMonth()) )
            .orElseThrow(() -> new IllegalStateException(
                "Unexpected null date for: " + span));
    }

    public List<StatSpanRecord> getMonth(YearMonth month){
        CacheKeyList<StatSpanRecord> key;
        if (! cacheMonths.containsKey(month)){
            key = new CacheKeyList<StatSpanRecord>(StatSpanRecord.class);
            cacheMonths.put(month, key);
        } else {
            key = cacheMonths.get(month);
        }
        return getLocalCache(key, () -> ImmutableList.copyOf(
            getChildren(StatSpanRecord.class)
                .stream()
                .filter(s -> month.equals(getYearMonth(Optional.of(s))))
                .iterator()
            ));
    }

    public StatSpanRecord getRecord(){
        return getLocalCache(cacheToday, () -> spanFromLast(StatSpanRecord.class)
            .orElseThrow(() ->
                new IllegalStateException("Unexpected empty file.")
            )
        );
    }

    public void startWriting(WritingText text){
        setFireReady(false);
        int publish = text.getPublishTotal();
        int note = text.getNoteTotal();
        checkDay(publish, note);
        getRecord().startWriting(publish, note);
        setFireReady(true);
    }

    public void stopWriting(WritingText text){
        setFireReady(false);
        int publish = text.getPublishTotal();
        int note = text.getNoteTotal();
        checkDay(publish, note);
        getRecord().stopWriting(publish, note);
        setFireReady(true);
    }
}


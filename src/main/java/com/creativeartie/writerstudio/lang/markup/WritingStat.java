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

    private static String createNewDay(){
        LocalDate today = LocalDate.now();

        return today.getYear() + STAT_DATA_SEP + today.getMonth().getValue() +
            STAT_DATA_SEP + today.getDayOfMonth() + STAT_SEPARATOR +
            STAT_DATE_END;
    }

    private final CacheKeyMain<YearMonth> cacheStart;
    private final CacheKeyMain<YearMonth> cacheEnd;
    private final Map<YearMonth, CacheKeyList<StatSpanDay>> cacheMonths;
    private final CacheKeyMain<StatSpanDay> cacheToday;

    /** Creates an empty  {@linkplain WritingStat}.
     *
     * @see WritingFile#newFile()
     */
    WritingStat(){
        this(createNewDay());
    }

    /** Creates a {@linkplain WritingText}.
     *
     * @param text
     *      raw content text
     * @see WritingFile#open(File)
     */
    public WritingStat(String text){
        super(text, StatParseDay.PARSER);
        cacheStart = new CacheKeyMain<>(YearMonth.class);
        cacheEnd = new CacheKeyMain<>(YearMonth.class);
        cacheMonths = new TreeMap<>();
        cacheToday = new CacheKeyMain<>(StatSpanDay.class);

        checkDay();
    }

    /** Add record if this is not today.
     *
     * @see #WritingStat(String)
     */
    private void checkDay(){
        Optional<LocalDate> date = spanFromLast(StatSpanDay.class)
            .map(s -> s.getRecordDate())
            .filter(d -> LocalDate.now().equals(d));
        if (! date.isPresent()){
            runCommand(() -> getRaw() + createNewDay());
        }
    }

    public YearMonth getStartMonth(){
        return getLocalCache(cacheStart, () -> getYearMonth(
            spanFromFirst(StatSpanDay.class)));
    }

    public YearMonth getEndMonth(){
        return getLocalCache(cacheEnd, () -> getYearMonth(
            spanFromLast(StatSpanDay.class)));
    }

    /** Get the {@linkplain YearMonth} from a {@link StatSpanDay}.
     *
     * @see #getStartMonth()
     * @see #getEndMonth()
     */
    private YearMonth getYearMonth(Optional<StatSpanDay> span){
        return  span/// s = StatSpanDay
            .map(s -> s.getRecordDate())
            /// d = LocalDate
            .map( d -> YearMonth.of(d.getYear(), d.getMonth()) )
            .orElseThrow(() -> new IllegalStateException(
                "Unexcepted null date for: " + span));
    }

    public List<StatSpanDay> getMonth(YearMonth month){
        CacheKeyList<StatSpanDay> key;
        if (! cacheMonths.containsKey(month)){
            key = new CacheKeyList<StatSpanDay>(StatSpanDay.class);
            cacheMonths.put(month, key);
        } else {
            key = cacheMonths.get(month);
        }
        return getLocalCache(key, () -> ImmutableList.copyOf(
            getChildren(StatSpanDay.class)
                .stream()
                .filter(s -> month.equals(getYearMonth(Optional.of(s))))
                .iterator()
            ));
     }
}


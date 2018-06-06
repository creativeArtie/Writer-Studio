package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import java.time.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.BranchStatsAsserts.*;
import com.creativeartie.writerstudio.lang.markup.BranchSectionAsserts.*;

import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

public class StatsTest {

    private static final SetupParser PARSER = StatParseDay.PARSER;
    private String getDate(){
        return getDate(LocalDate.now());
    }

    private String getDate(LocalDate time){
        return time.getYear() + "-" + time.getMonth().getValue() + "-" +
            time.getDayOfMonth();
    }


    @Test@Disabled
    public void basicPublishTotal(){
        String raw = getDate() + "|publish-count:2|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        StatMainAssert date = new StatMainAssert(doc)
            .setPublishTotal(2);

         IntStatAssert publish = new IntStatAssert(doc)
            .setType(StatTypeData.PUBLISH_TOTAL)
            .setData(2);

        date.test(8,    raw,       0);
        publish.test(4, "publish-count:2|", 0, 6);
    }

    @Test@Disabled
    @DisplayName("StatSpanDay#setPublishGoal() by inserting")
    public void addPublishGoal(){
        String raw = getDate() + "\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.setPublishGoal(20),
            0);
        commonPublishGoal(doc);
    }

    @Test@Disabled
    @DisplayName("StatSpanDay#setPublishGoal() by updating")
    public void editPublishGoal(){
        String raw = getDate() + "|publish-goal:12|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.setPublishGoal(20),
            0, 6);
        commonPublishGoal(doc);
    }

    @Test@Disabled
    public void basicPublishGoal(){
        String raw = getDate() + "|publish-goal:20|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        commonPublishGoal(doc);
    }

    public void commonPublishGoal(DocumentAssert doc){
        String raw = getDate() + "|publish-goal:20|\n";
        doc.assertDoc(1, raw);

        StatMainAssert date = new StatMainAssert(doc)
            .setPublishGoal(20);

        IntStatAssert publish = new IntStatAssert(doc)
            .setType(StatTypeData.PUBLISH_GOAL)
            .setData(20);

        date.test(8,    raw,        0);
        publish.test(4, "publish-goal:20|", 0, 6);
    }

    @Test@Disabled
    @DisplayName("StatSpanDataInt#setData(int)")
    public void setIntData(){
        String raw = getDate() + "|note-count:2|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.call(StatSpanDataInt.class, s -> s.setData(20), 0, 6);
        commonNote(doc);
    }

    @Test@Disabled
    public void basicNoteTotal(){
        String raw = getDate() + "|note-count:20|\n";
        commonNote(assertDoc(1, raw, PARSER));
    }


    private void commonNote(DocumentAssert doc){
        String raw = getDate() + "|note-count:20|\n";
        doc.assertDoc(1, raw);

        StatMainAssert date = new StatMainAssert(doc)
            .setNoteTotal(20);

        IntStatAssert note = new IntStatAssert(doc)
            .setType(StatTypeData.NOTE_TOTAL)
            .setData(20);

        date.test(8, raw,       0);
        note.test(4, "note-count:20|", 0, 6);
    }

    @Test@Disabled
    public void basicUnknown(){
        String raw = getDate() + "|note-goal:20|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        StatMainAssert date = new StatMainAssert(doc)
            .setNoteTotal(20);

        StringStatAssert note = new StringStatAssert(doc)
            .setType(StatTypeData.UNKNOWN)
            .setData("20");

        date.test(8, raw,       0);
        note.test(4, "note-goal:20|", 0, 6);
    }

    @Test@Disabled
    public void basicTimeTotal(){
        String raw = getDate() + "|time-count:PT20S|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        StatMainAssert date = new StatMainAssert(doc)
            .setWriteTime(Duration.ofSeconds(20));

        TimeStatAssert time = new TimeStatAssert(doc)
            .setType(StatTypeData.TIME_TOTAL)
            .setData(Duration.ofSeconds(20));

        date.test(8, raw,       0);
        time.test(4, "time-count:PT20S|", 0, 6);
    }

    @Test@Disabled
    @DisplayName("StatSpanDay#setTimeGoal() by inserting")
    public void addTimeGoal(){
        String raw = getDate() + "\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.setTimeGoal(Duration.ofSeconds(20)),
            0);
        commonTimeGoal(doc);
    }

    @Test@Disabled
    @DisplayName("StatSpanDay#setTimeGoal() by updating")
    public void editTimeGoal(){
        String raw = getDate() + "|time-goal:PT2S|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.setTimeGoal(Duration.ofSeconds(20)),
            0, 6);
        commonTimeGoal(doc);
    }

    @Test@Disabled
    @DisplayName("StatSpanDataTime#setData(Duration)")
    public void setTimeData(){
        String raw = getDate() + "|time-goal:PT2S|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.call(StatSpanDataTime.class,s -> s.setData(Duration.ofSeconds(20)),
            0, 6);
        commonTimeGoal(doc);
    }

    @Test@Disabled
    public void basicTimeGoal(){
        String raw = getDate() + "|time-goal:PT20S|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        commonTimeGoal(doc);
    }

    private void commonTimeGoal(DocumentAssert doc){
        String raw = getDate() + "|time-goal:PT20S|\n";
        doc.assertDoc(1, raw);

        StatMainAssert date = new StatMainAssert(doc)
            .setTimeGoal(Duration.ofSeconds(20));

        TimeStatAssert time = new TimeStatAssert(doc)
            .setType(StatTypeData.TIME_GOAL)
            .setData(Duration.ofSeconds(20));
        date.test(8, raw,       0);
        time.test(4, "time-goal:PT20S|", 0, 6);
    }

    @Test@Disabled
    public void basicDoubleData(){
        String raw = getDate() + "|time-goal:PT20S|note-count:20|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        StatMainAssert date = new StatMainAssert(doc)
            .setTimeGoal(Duration.ofSeconds(20))
            .setNoteTotal(20);

        TimeStatAssert time = new TimeStatAssert(doc)
            .setType(StatTypeData.TIME_GOAL)
            .setData(Duration.ofSeconds(20));
        IntStatAssert note = new IntStatAssert(doc)
            .setType(StatTypeData.NOTE_TOTAL)
            .setData(20);

        date.test(9, raw,         0);
        time.test(4, "time-goal:PT20S|", 0, 6);
        note.test(4, "note-count:20|",  0, 7);
    }

    @Test@Disabled
    public void basicTripleData(){
        String raw = getDate() + "|time-count:PT20S|note-count:20|publish-count:20|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        commonWordCounter(doc);
    }

    @Test
    public void editText() throws Exception{
        String raw = getDate() + "|time-count:PT18S|note-count:2|publish-count:20|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.call(true, () -> doc.getChild(StatSpanDay.class, 0),
            s -> s.startWriting(20, 2), () -> new SpanNode<?>[]{
                doc.getChild(StatSpanDataInt.class, 0, 7),
                doc.getChild(StatSpanDataInt.class, 0, 8)
        });
        doc.printDocument();
        Thread.sleep(2000);
        doc.call(true, () -> doc.getChild(StatSpanDay.class, 0),
            s -> s.stopWriting(20, 20), () -> new SpanNode<?>[]{
                doc.getChild(StatSpanDataTime.class, 0, 6),
                doc.getChild(StatSpanDataInt.class, 0, 7),
                doc.getChild(StatSpanDataInt.class, 0, 8)
        });
        commonWordCounter(doc);

    }

    private void commonWordCounter(DocumentAssert doc){
        String raw = getDate() + "|time-count:PT20S|note-count:20|publish-count:20|\n";
        doc.assertDoc(1, raw);

        StatMainAssert date = new StatMainAssert(doc)
            .setTimeGoal(Duration.ofSeconds(20))
            .setNoteTotal(20).setPublishTotal(20);

        TimeStatAssert time = new TimeStatAssert(doc)
            .setType(StatTypeData.TIME_TOTAL)
            .setData(Duration.ofSeconds(20));
        IntStatAssert note = new IntStatAssert(doc)
            .setType(StatTypeData.NOTE_TOTAL)
            .setData(20);
        IntStatAssert out = new IntStatAssert(doc)
            .setType(StatTypeData.PUBLISH_TOTAL)
            .setData(20);

        date.test(10, raw,                 0);
        time.test(4, "time-count:PT20S|",  0, 6);
        note.test(4, "note-count:20|",    0, 7);
        out.test( 4, "publish-count:20|", 0, 8);
    }
}

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

    @Test
    public void basicDay(){
        String raw = getDate() + "|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        StatMainAssert data = new StatMainAssert(doc);
        data.test(7, raw, 0);
    }


    @Test
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

    @Test
    @DisplayName("StatSpanDay#setPublishGoal() by inserting")
    public void addPublishGoal(){
        String raw = getDate() + "|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.setPublishGoal(20),
            0);
        commonPublishGoal(doc);
    }

    @Test
    @DisplayName("StatSpanDay#setPublishGoal() by updating")
    public void editPublishGoal(){
        String raw = getDate() + "|publish-goal:12|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.setPublishGoal(20),
            0, 6);
        commonPublishGoal(doc);
    }

    @Test
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

    @Test
    @DisplayName("SpecStatDataInt#setData(int)")
    public void setIntData(){
        String raw = getDate() + "|note-count:2|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.call(SpecStatDataInt.class, s -> s.setData(20), 0, 6);
        commonNote(doc);
    }

    @Test
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

    @Test
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

    @Test
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

    @Test
    @DisplayName("StatSpanDay#setTimeGoal() by inserting")
    public void addTimeGoal(){
        String raw = getDate() + "|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.setTimeGoal(Duration.ofSeconds(20)),
            0);
        commonTimeGoal(doc);
    }

    @Test
    @DisplayName("StatSpanDay#setTimeGoal() by updating")
    public void editTimeGoal(){
        String raw = getDate() + "|time-goal:PT2S|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.setTimeGoal(Duration.ofSeconds(20)),
            0, 6);
        commonTimeGoal(doc);
    }

    @Test
    @DisplayName("SpecStatDataTime#setData(Duration)")
    public void setTimeData(){
        String raw = getDate() + "|time-goal:PT2S|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.call(SpecStatDataTime.class,s -> s.setData(Duration.ofSeconds(20)),
            0, 6);
        commonTimeGoal(doc);
    }

    @Test
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

    @Test
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

    @Test
    public void basicTripleData(){
        String raw = getDate() + "|time-count:PT20S|publish-count:20|note-count:20|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        commonWordCounter(doc);
    }

    @Test
    @Tag("timed")
    public void editText1() throws Exception{
        String raw = getDate() + "|time-count:PT18S|publish-count:20|note-count:2|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.startWriting(20, 2), () -> new SpanNode<?>[]{
                doc.getChild(SpecStatDataInt.class, 0, 7),
                doc.getChild(SpecStatDataInt.class, 0, 8)
        });
        Thread.sleep(2000);
        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.stopWriting(20, 20), () -> new SpanNode<?>[]{
                doc.getChild(SpecStatDataTime.class, 0, 6),
                doc.getChild(SpecStatDataInt.class, 0, 7),
                doc.getChild(SpecStatDataInt.class, 0, 8)
        });
        commonWordCounter(doc);
    }

    @Test
    @Tag("timed")
    public void editText2() throws Exception{
        String raw = getDate() + "|time-count:PT18S|publish-count:2|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.call(true, () -> doc.getChild(StatSpanDay.class, 0),
            s -> s.startWriting(20, 2), () -> new SpanNode<?>[]{
                doc.getChild(SpecStatDataInt.class, 0, 7),
                doc.getChild(StatSpanDay.class, 0)
        });
        Thread.sleep(2000);
        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.stopWriting(20, 20), () -> new SpanNode<?>[]{
                doc.getChild(SpecStatDataTime.class, 0, 6),
                doc.getChild(SpecStatDataInt.class, 0, 7),
                doc.getChild(SpecStatDataInt.class, 0, 8)
        });
        commonWordCounter(doc);
    }

    @Test
    @Tag("timed")
    @Tag("heavy")
    @DisplayName("Add all data")
    public void editAllNew1() throws Exception{
        String base = getDate() + "|";
        String raw = base + "\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.startWriting(20, 20), () -> new SpanNode<?>[]{
                doc.getChild(StatSpanDay.class, 0),
        });

        Thread.sleep(20000);
        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.stopWriting(20, 20), () -> new SpanNode<?>[]{
                doc.getChild(StatSpanDay.class, 0),
                doc.getChild(SpecStatDataInt.class, 0, 6),
                doc.getChild(SpecStatDataInt.class, 0, 7)
        });

        raw = base + "publish-count:20|note-count:20|time-count:PT20S|\n";
        doc.assertDoc(1, raw);

        StatMainAssert date = new StatMainAssert(doc)
            .setTimeGoal(Duration.ofSeconds(20))
            .setNoteTotal(20).setPublishTotal(20);

        IntStatAssert out = new IntStatAssert(doc)
            .setType(StatTypeData.PUBLISH_TOTAL)
            .setData(20);
        IntStatAssert note = new IntStatAssert(doc)
            .setType(StatTypeData.NOTE_TOTAL)
            .setData(20);
        TimeStatAssert time = new TimeStatAssert(doc)
            .setType(StatTypeData.TIME_TOTAL)
            .setData(Duration.ofSeconds(20));

        date.test(10, raw,                 0);
        out.test( 4, "publish-count:20|",  0, 6);
        note.test(4, "note-count:20|",     0, 7);
        time.test(4, "time-count:PT20S|",  0, 8);
    }

    @Test
    @DisplayName("Add all data")
    public void editAllNew2() throws Exception{
        String raw = getDate() + "|time-count:PT20S|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.stopWriting(20, 20), () -> new SpanNode<?>[]{
                doc.getChild(StatSpanDay.class, 0),
        });
        commonWordCounter(doc);
    }

    @Test
    @DisplayName("Update & add data.")
    public void editStopCall() throws Exception{
        String raw = getDate() + "|time-count:PT20S|publish-count:2|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.stopWriting(20, 20), () -> new SpanNode<?>[]{
                doc.getChild(StatSpanDay.class, 0),
                doc.getChild(SpecStatDataInt.class, 0, 7)
        });
        commonWordCounter(doc);
    }

    @Test
    @DisplayName("Update all data.")
    public void editStartCall() throws Exception{
        String raw = getDate() + "|time-count:PT20S|publish-count:20|note-count:2|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.startWriting(20, 20), () -> new SpanNode<?>[]{
                doc.getChild(SpecStatDataInt.class, 0, 7),
                doc.getChild(SpecStatDataInt.class, 0, 8),
        });
        commonWordCounter(doc);
    }

    @Test
    public void editBothAdded() throws Exception{
        String raw = getDate() + "|time-count:PT20S|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        doc.call(() -> doc.getChild(StatSpanDay.class, 0),
            s -> s.startWriting(20, 20), () -> new SpanNode<?>[]{
                doc.getChild(StatSpanDay.class, 0)
        });
        commonWordCounter(doc);
    }

    private void commonWordCounter(DocumentAssert doc){
        String raw = getDate() + "|time-count:PT20S|publish-count:20|note-count:20|\n";
        doc.assertDoc(1, raw);

        StatMainAssert date = new StatMainAssert(doc)
            .setTimeGoal(Duration.ofSeconds(20))
            .setNoteTotal(20).setPublishTotal(20);

        TimeStatAssert time = new TimeStatAssert(doc)
            .setType(StatTypeData.TIME_TOTAL)
            .setData(Duration.ofSeconds(20));
        IntStatAssert out = new IntStatAssert(doc)
            .setType(StatTypeData.PUBLISH_TOTAL)
            .setData(20);
        IntStatAssert note = new IntStatAssert(doc)
            .setType(StatTypeData.NOTE_TOTAL)
            .setData(20);

        date.test(10, raw,                 0);
        time.test(4, "time-count:PT20S|",  0, 6);
        out.test( 4, "publish-count:20|",  0, 7);
        note.test(4, "note-count:20|",     0, 8);
    }
}

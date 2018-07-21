package com.creativeartie.writerstudio.lang.markup;

import org.junit.jupiter.api.*;

import java.time.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.BranchStatsAsserts.*;

import static com.creativeartie.writerstudio.lang.DocumentAssert.*;

public class StatsTest {

    private static final SetupParser PARSER = StatParseDay.PARSER;
    private String getDate(){
        return getDate(LocalDate.now());
    }

    private String getDate(LocalDate time){
        return time.format(AuxiliaryData.STAT_DATE);
    }

    @Test
    public void basicDay(){
        String raw = getDate() + "|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        StatMainAssert data = new StatMainAssert(doc);
        data.test(3, raw, 0);
        doc.assertRest();
    }


    @Test
    public void basicPublishTotal(){
        String raw = getDate() + "|publish-count:2|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        StatMainAssert date = new StatMainAssert(doc)
            .setPublishTotal(2).setGrand(2).setWritten(2);

         IntStatAssert publish = new IntStatAssert(doc)
            .setType(StatTypeData.PUBLISH_TOTAL)
            .setData(2);

        date.test(4,    raw,       0);
        publish.test(4, "publish-count:2|", 0, 2);
        doc.assertRest();
    }

    @Test
    @DisplayName("StatSpanDay#setPublishGoal() by inserting")
    public void addPublishGoal(){
        String raw = getDate() + "|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.setListenTester(ListenerAssert
            .builder(s -> s.setPublishGoal(20), doc, StatSpanDay.class, 0)
            .setEdited(0)
        );
        commonPublishGoal(doc);
    }

    @Test
    @DisplayName("StatSpanDay#setPublishGoal() by updating")
    public void editPublishGoal(){
        String raw = getDate() + "|publish-goal:12|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.setListenTester(ListenerAssert
            .builder(s -> s.setPublishGoal(20), doc, StatSpanDay.class, 0)
            .setEdited(0, 2));
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

        date.test(4,    raw,                0);
        publish.test(4, "publish-goal:20|", 0, 2);
        doc.assertRest();
    }

    @Test
    @DisplayName("StatSpanDataInt#setData(int)")
    public void setIntData(){
        String raw = getDate() + "|note-count:2|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.setListenTester(ListenerAssert
            .builder(s -> s.setData(20), doc, StatSpanDataInt.class, 0, 2)
            .setEdited(0, 2)
        );
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
            .setNoteTotal(20).setGrand(20);

        IntStatAssert note = new IntStatAssert(doc)
            .setType(StatTypeData.NOTE_TOTAL)
            .setData(20);

        date.test(4, raw,              0);
        note.test(4, "note-count:20|", 0, 2);
        doc.assertRest();
    }

    @Test
    public void basicUnknown(){
        String raw = getDate() + "|note-goal:20|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        StatMainAssert date = new StatMainAssert(doc);

        StringStatAssert note = new StringStatAssert(doc)
            .setType(StatTypeData.UNKNOWN)
            .setData("20");

        date.test(4, raw,       0);
        note.test(4, "note-goal:20|", 0, 2);
        doc.assertRest();
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

        date.test(4, raw,       0);
        time.test(4, "time-count:PT20S|", 0, 2);
        doc.assertRest();
    }

    @Test
    @DisplayName("StatSpanDay#setTimeGoal() by inserting")
    public void addTimeGoal(){
        String raw = getDate() + "|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.setListenTester(ListenerAssert
            .builder(s -> s.setTimeGoal(Duration.ofSeconds(20)), doc,
                StatSpanDay.class, 0)
            .setEdited(0)
        );
        commonTimeGoal(doc);
    }

    @Test
    @DisplayName("StatSpanDay#setTimeGoal() by updating")
    public void editTimeGoal(){
        String raw = getDate() + "|time-goal:PT2S|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.setListenTester(ListenerAssert
            .builder(s -> s.setTimeGoal(Duration.ofSeconds(20)), doc,
                StatSpanDay.class, 0)
            .setEdited(0, 2)
        );
        commonTimeGoal(doc);
    }

    @Test
    @DisplayName("StatSpanDataTime#setData(Duration)")
    public void setTimeData(){
        String raw = getDate() + "|time-goal:PT2S|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        doc.setListenTester(ListenerAssert
            .builder(s -> s.setData(Duration.ofSeconds(20)), doc,
                StatSpanDataTime.class, 0, 2)
            .setEdited(0, 2)
        );
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
        date.test(4, raw,                0);
        time.test(4, "time-goal:PT20S|", 0, 2);
        doc.assertRest();
    }

    @Test
    public void basicDoubleData(){
        String raw = getDate() + "|time-goal:PT20S|note-count:20|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        StatMainAssert date = new StatMainAssert(doc)
            .setTimeGoal(Duration.ofSeconds(20))
            .setNoteTotal(20).setGrand(20);

        TimeStatAssert time = new TimeStatAssert(doc)
            .setType(StatTypeData.TIME_GOAL)
            .setData(Duration.ofSeconds(20));
        IntStatAssert note = new IntStatAssert(doc)
            .setType(StatTypeData.NOTE_TOTAL)
            .setData(20);

        date.test(5, raw,         0);
        time.test(4, "time-goal:PT20S|", 0, 2);
        note.test(4, "note-count:20|",  0, 3);
        doc.assertRest();
    }

    @Test
    public void basicTripleData(){
        String raw = getDate() + "|publish-count:20|note-count:20|time-count:PT2S|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);
        commonWordCounter(doc, false);
    }

    @Test
    @Tag("timed")
    public void editText1() throws Exception{
        String raw = getDate() + "|publish-count:20|note-count:2|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        doc.getChild(StatSpanDay.class, 0).startWriting(20, 2);
        Thread.sleep(2000);
        doc.setListenTester(ListenerAssert
            .builder(s -> s.stopWriting(20, 20), doc, StatSpanDay.class, 0)
            .setEdited(0)
            .setEdited(0, 2)
            .setEdited(0, 3)
        );
        commonWordCounter(doc, false);
    }

    @Test
    @Tag("timed")
    public void editText2() throws Exception{
        String raw = getDate() + "|time-count:PT1S|publish-count:2|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        doc.getChild(StatSpanDay.class, 0).startWriting(20, 2);
        Thread.sleep(1000);
        doc.setListenTester(ListenerAssert
            .builder(s -> s.stopWriting(20, 20), doc, StatSpanDay.class, 0)
            .setEdited(0, 2)
            .setEdited(0, 3)
            .setEdited(0, 4)
        );
        commonWordCounter(doc, true);
    }

    @Test
    @Tag("timed")
    @Tag("heavy")
    @DisplayName("Add from nothing")
    public void editAllNew1() throws Exception{
        String base = getDate() + "|";
        String raw = base + "\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        doc.getChild(StatSpanDay.class, 0).startWriting(20, 20);
        Thread.sleep(2000);
        doc.setListenTester(ListenerAssert
            .builder(s -> s.stopWriting(20, 20), doc, StatSpanDay.class, 0)
            .setEdited(0)
        );

        commonWordCounter(doc, false);
    }

    @Test
    @DisplayName("Add all data")
    public void editAllNew2() throws Exception{
        String raw = getDate() + "|time-count:PT2S|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        doc.setListenTester(ListenerAssert
            .builder(s -> s.stopWriting(20, 20), doc,
                StatSpanDay.class, 0)
            .setEdited(0)
        );
        commonWordCounter(doc, true);
    }

    @Test
    public void editStopCall() throws Exception{
        String raw = getDate() + "|publish-count:2|note-count:2|time-count:PT2S|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        doc.setListenTester(ListenerAssert
            .builder(s -> s.stopWriting(20, 20), doc, StatSpanDay.class, 0)
            .setEdited(0, 2)
            .setEdited(0, 3)
        );
        commonWordCounter(doc, false);
    }

    @Test
    public void editStartCall() throws Exception{
        String raw = getDate() + "|publish-count:20|note-count:2|time-count:PT2S|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        doc.setListenTester(ListenerAssert
            .builder(s -> s.startWriting(20, 20), doc, StatSpanDay.class, 0)
            .setEdited(0, 2)
            .setEdited(0, 3)
        );
        commonWordCounter(doc, false);
    }

    @Test
    public void editBothAdded() throws Exception{
        String raw = getDate() + "|time-count:PT2S|\n";
        DocumentAssert doc = assertDoc(1, raw, PARSER);

        doc.setListenTester(ListenerAssert
            .builder(s -> s.startWriting(20, 20), doc, StatSpanDay.class, 0)
            .setEdited(0)
        );
        commonWordCounter(doc, true);
    }

    private void commonWordCounter(DocumentAssert doc, boolean has){
        String in = "time-count:PT2S|";
        String data = (has? in: "") +
            "publish-count:20|note-count:20|" +
            (has? "": in);
        String raw = getDate() + "|" + data + "\n";
        doc.assertDoc(1, raw);

        StatMainAssert date = new StatMainAssert(doc)
            .setWriteTime(Duration.ofSeconds(2))
            .setNoteTotal(20).setPublishTotal(20)
            .setGrand(40).setWritten(20);

        TimeStatAssert time = new TimeStatAssert(doc)
            .setType(StatTypeData.TIME_TOTAL)
            .setData(Duration.ofSeconds(2));
        IntStatAssert out = new IntStatAssert(doc)
            .setType(StatTypeData.PUBLISH_TOTAL)
            .setData(20);
        IntStatAssert note = new IntStatAssert(doc)
            .setType(StatTypeData.NOTE_TOTAL)
            .setData(20);

        date.test(6, raw,                 0);
        out.test( 4, "publish-count:20|", 0, (has? 3: 2));
        note.test(4, "note-count:20|",    0, (has? 4: 3));
        time.test(4, "time-count:PT2S|",  0, (has? 2: 4));
        doc.assertRest();
    }
}

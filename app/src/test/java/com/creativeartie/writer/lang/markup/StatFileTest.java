package com.creativeartie.writer.lang.markup;

import org.junit.jupiter.api.*;

import java.util.*;
import java.time.*;

import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.BranchStatsAsserts.*;
import static com.creativeartie.writer.lang.DocumentAssert.*;
public class StatFileTest {

    private class StatAssert{

        private LocalDate recordDate;
        private int publishGoal;
        private boolean publishSet = false;
        private int publishTotal;
        private boolean noteSet = false;
        private int noteTotal;
        private Duration timeGoal;
        private boolean timeSet = false;
        private Duration timeTotal;

        private StatAssert(LocalDate date){
            recordDate = date;
            publishGoal = 50;
            publishTotal = 0;
            noteTotal = 0;
            timeGoal = Duration.ofMinutes(30);
            timeTotal = Duration.ofSeconds(0);
        }

        private StatAssert setPublishGoal(int count){
            publishGoal = count;
            return this;
        }

        private StatAssert setPublishTotal(int count){
            publishTotal = count;
            publishSet = true;
            return this;
        }

        private StatAssert setNoteTotal(int count){
            noteTotal = count;
            noteSet = true;
            return this;
        }

        private StatAssert setTimeGoal(int time){
            timeGoal = Duration.ofMinutes(time);
            return this;
        }

        private StatAssert setTimeTotal(int time){
            timeTotal = Duration.ofSeconds(time);
            timeSet = true;
            return this;
        }

        private int getGrandTotal(){
            return publishTotal + noteTotal;
        }

        private int getPublishWritten(StatAssert last){
            return publishTotal - (last == null? 0: last.publishTotal);
        }

        String getRaw(){
            return recordDate.format(AuxiliaryData.STAT_DATE) + "|" +
                AuxiliaryData.STAT_PUBLISH_GOAL + ":" + publishGoal + "|" +
                AuxiliaryData.STAT_TIME_GOAL + ":" + timeGoal + "|" +
                (! publishSet? "":
                    AuxiliaryData.STAT_PUBLISH_COUNT + ":" + publishTotal + "|"
                ) +
                (! noteSet? "":
                    AuxiliaryData.STAT_NOTE_COUNT + ":" + noteTotal + "|"
                ) +
                (! timeSet? "":
                    AuxiliaryData.STAT_TIME_COUNT + ":" + timeTotal + "|"
                ) + "\n";
        }

        void test(DocumentAssert doc, StatAssert last, int ... indexes){
            int size = 5;
            if (noteSet) size++;
            if (publishSet) size++;
            if (timeSet) size++;
            new StatMainAssert(doc)
                .setPublishTotal(publishTotal).setNoteTotal(noteTotal)
                .setPublishGoal(publishGoal).setRecordDate(recordDate)
                .setTimeGoal(timeGoal).setWritten(getPublishWritten(last))
                .setWriteTime(timeTotal).setGrand(getGrandTotal())
                .test(size, getRaw(), indexes);

        }
    }

    private String buildRaw(List<StatAssert> stats){
        StringBuilder raw = new StringBuilder();
        stats.stream().forEach(s -> raw.append(s.getRaw()));
        return raw.toString();
    }

    private void assertAll(List<StatAssert> expects, WritingStat test){
        DocumentAssert doc = assertDoc(expects.size(), buildRaw(expects), test);
        StatAssert last = null;
        int index = 0;
        for (StatAssert expect: expects){
            expect.test(doc, last, index);
            last = expect;
            index++;
        }
    }

    @Test
    @DisplayName("Create new")
    public void assertNew(){
        WritingStat test = new WritingStat();
        List<StatAssert> expect = Arrays.asList(new StatAssert(LocalDate.now()));
        assertAll(expect, test);
    }

    @Test
    @DisplayName("Reopen file")
    public void reopen(){
        List<StatAssert> expect = Arrays.asList(new StatAssert(LocalDate.now()));
        WritingStat test = new WritingStat(buildRaw(expect));
        assertAll(expect, test);
    }

    @Test
    @DisplayName("Set goal")
    public void setGoal(){
        WritingStat test = new WritingStat();
        test.getRecord().setPublishGoal(1000);
        test.getRecord().setTimeGoal(Duration.ofMinutes(45));

        List<StatAssert> expect = Arrays.asList(new StatAssert(LocalDate.now())
            .setPublishGoal(1000).setTimeGoal(45));
        assertAll(expect, test);
    }

    @Test
    @DisplayName("Old file")
    public void openOld(){
        ArrayList<StatAssert> expect = new ArrayList<>();
        StatAssert first = new StatAssert(LocalDate.now().minusDays(3));
        expect.add(first);
        WritingStat test = new WritingStat(buildRaw(expect));
        first.setPublishTotal(0).setNoteTotal(0);
        expect.add(new StatAssert(LocalDate.now()));
        assertAll(expect, test);
    }

    @Test
    @DisplayName("Old goal file")
    public void openOldGoal(){
        ArrayList<StatAssert> expect = new ArrayList<>();
        StatAssert first = new StatAssert(LocalDate.now().minusDays(3));
        expect.add(first.setPublishGoal(20).setTimeGoal(45));
        WritingStat test = new WritingStat(buildRaw(expect));
        first.setPublishTotal(0).setNoteTotal(0);
        expect.add(new StatAssert(LocalDate.now())
            .setPublishGoal(20).setTimeGoal(45));
        assertAll(expect, test);
    }

    /// situations: new, from yesterday, open today
    /// goal: defaults, edited, changed
    /// writing: new, over night, same day

    @Test
    @Tag("timed")
    @DisplayName("Update today count")
    public void updateDay() throws InterruptedException{
        ArrayList<StatAssert> expect = new ArrayList<>();
        StatAssert today = new StatAssert(LocalDate.now())
            .setPublishTotal(2).setNoteTotal(0).setTimeTotal(19);
        expect.add(today);
        WritingStat test = new WritingStat(buildRaw(expect));
        assertAll(expect, test);

        WritingText before = new WritingText("Hello World!");
        WritingText after = new WritingText("Hello World! One two Three");

        test.startWriting(before);
        Thread.currentThread().sleep(1000);
        test.stopWriting(after);

        today.setPublishTotal(5).setTimeTotal(20);

        assertAll(expect, test);
    }

    @Test
    @Disabled("Test not ready")
    @DisplayName("Started yesterday")
    public void startedYesterday() throws InterruptedException{}

    @Test
    @Tag("timed")
    @DisplayName("Pass over today.")
    public void returningToday() throws InterruptedException {

        ArrayList<StatAssert> expect = new ArrayList<>();
        expect.add(new StatAssert(LocalDate.now().minusDays(1))
            .setPublishTotal(2).setNoteTotal(0).setTimeTotal(20));
        StatAssert today = new StatAssert(LocalDate.now())
            .setPublishTotal(0).setNoteTotal(0).setTimeTotal(0);
        expect.add(today);
        WritingStat test = new WritingStat(buildRaw(expect));
        assertAll(expect, test);

        WritingText before = new WritingText("");
        WritingText after = new WritingText("Hello World! One two Three");

        test.startWriting(before);
        Thread.currentThread().sleep(1000);
        test.stopWriting(after);

        today.setPublishTotal(5).setTimeTotal(1);
        assertAll(expect, test);
    }

    @Test
    @DisplayName("mutiple startWriting calls")
    public void mulitpleStart() throws InterruptedException{
        ArrayList<StatAssert> expect = new ArrayList<>();
        StatAssert today = new StatAssert(LocalDate.now())
            .setPublishTotal(0).setNoteTotal(0).setTimeTotal(0);
        expect.add(today);
        WritingStat test = new WritingStat(buildRaw(expect));
        assertAll(expect, test);

        WritingText before = new WritingText("");
        WritingText after = new WritingText("Hello World! One two Three");

        test.startWriting(before);
        Thread.currentThread().sleep(1000);
        test.startWriting(after);

        today.setPublishTotal(5);
        assertAll(expect, test);
    }

    @Test
    @DisplayName("mutiple stopWriting calls")
    public void mulitpleEnd() throws InterruptedException{
        ArrayList<StatAssert> expect = new ArrayList<>();
        StatAssert today = new StatAssert(LocalDate.now())
            .setPublishTotal(0).setNoteTotal(0).setTimeTotal(0);
        expect.add(today);
        WritingStat test = new WritingStat(buildRaw(expect));
        assertAll(expect, test);

        WritingText before = new WritingText("");
        WritingText after = new WritingText("Hello World! One two Three");

        test.stopWriting(before);
        Thread.currentThread().sleep(1000);
        test.stopWriting(after);

        today.setPublishTotal(5);
        assertAll(expect, test);
    }
}

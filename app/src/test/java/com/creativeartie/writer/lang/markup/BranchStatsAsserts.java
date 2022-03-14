package com.creativeartie.writer.lang.markup;

import org.junit.jupiter.api.function.*;

import java.util.*;
import java.time.*;

import com.creativeartie.writer.lang.*;

import static org.junit.jupiter.api.Assertions.*;

public class BranchStatsAsserts {

    private abstract static class StatAssert<T extends StatAssert<?, ?>, U> extends
            SpanBranchAssert<T>{
        private StatTypeData statType;
        private U statData;

        public StatAssert(Class<T> cast, DocumentAssert doc){
            super(cast, doc);
            statType = StatTypeData.TIME_TOTAL;
        }

        public final T setType(StatTypeData type){
            statType = type;
            return cast();
        }

        public final T setData(U data){
            statData = data;
            return cast();
        }

        @Override
        public void setup(){
            moreSetup();
        }

        protected abstract void moreSetup();

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            StatSpanData<?> test = moreTest(span);
            tests.add(() -> assertEquals(statType, test.getDataType()));
            tests.add(() -> assertEquals(statData, test.getData()));
        }

        protected abstract StatSpanData<?> moreTest(SpanBranch span);
    }

    public static final class IntStatAssert
            extends StatAssert<IntStatAssert, Integer>{

        public IntStatAssert(DocumentAssert doc){
            super(IntStatAssert.class, doc);
        }

        @Override
        protected void moreSetup(){}

        @Override
        protected StatSpanData<?> moreTest(SpanBranch span){
            return assertClass(StatSpanDataInt.class);
        }
    }

    public static final class TimeStatAssert extends StatAssert<TimeStatAssert,
            Duration>{

        public TimeStatAssert(DocumentAssert doc){
            super(TimeStatAssert.class, doc);
        }

        @Override
        protected void moreSetup(){}

        @Override
        protected StatSpanData<?> moreTest(SpanBranch span){
            return assertClass(StatSpanDataTime.class);
        }
    }

    public static final class StringStatAssert
            extends StatAssert<StringStatAssert, String>{

        public StringStatAssert(DocumentAssert doc){
            super(StringStatAssert.class, doc);
        }

        @Override
        protected void moreSetup(){}

        @Override
        protected StatSpanData<?> moreTest(SpanBranch span){
            return assertClass(StatSpanDataString.class);
        }
    }

    public static final class StatMainAssert
            extends SpanBranchAssert<StatMainAssert>{
        private LocalDate recordDate;
        private Duration timeGoal;
        private Duration timeTotal;
        private int publishGoal;
        private int publishTotal;
        private int noteTotal;
        private int grandTotal;
        private int publishWritten;

        public StatMainAssert(DocumentAssert doc){
            super(StatMainAssert.class, doc);
            recordDate = LocalDate.now();
            timeGoal = Duration.ofSeconds(0);
            timeTotal = Duration.ofSeconds(0);
            publishGoal = 0;
            publishTotal = 0;
            noteTotal = 0;
            grandTotal = 0;
            publishWritten = 0;
        }

        public StatMainAssert setRecordDate(LocalDate date){
            recordDate = date;
            return this;
        }

        public StatMainAssert setTimeGoal(Duration value){
            timeGoal = value;
            return this;
        }

        public StatMainAssert setWriteTime(Duration value){
            timeTotal = value;
            return this;
        }

        public StatMainAssert setPublishGoal(int value){
            publishGoal = value;
            return this;
        }

        public StatMainAssert setPublishTotal(int value){
            publishTotal = value;
            return this;
        }

        public StatMainAssert setNoteTotal(int value){
            noteTotal = value;
            return this;
        }

        public StatMainAssert setGrand(int value){
            grandTotal = value;
            return this;
        }

        public StatMainAssert setWritten(int value){
            publishWritten = value;
            return this;
        }

        @Override
        public void setup(){}

        @Override
        public void test(SpanBranch span, ArrayList<Executable> tests){
            StatSpanDay test = assertClass(StatSpanDay.class);
            tests.add(() -> assertEquals(recordDate, test.getRecordDate(),
                "getRecordDate()"));
            tests.add(() -> assertEquals(timeGoal, test.getTimeGoal(),
                "getTimeGoal()"));
            tests.add(() -> assertEquals(timeTotal, test.getWriteTime(),
                "getWriteTime()"));
            tests.add(() -> assertEquals(publishGoal, test.getPublishGoal(),
                "getPublishGoal()"));
            tests.add(() -> assertEquals(publishTotal, test.getPublishTotal(),
                "getPublishTotal()"));
            tests.add(() -> assertEquals(noteTotal, test.getNoteTotal(),
                "getNoteTotal()"));
            tests.add(() -> assertEquals(grandTotal, test.getTotalCount(),
                "getTotalCount()"));
            tests.add(() -> assertEquals(publishWritten, test.getPublishWritten(),
                "getPublishWritten()"));
        }
    }

}

package com.creativeartie.humming.files;

import java.io.*;
import java.time.*;
import java.util.*;

import com.google.common.collect.*;

/**
 * A log of writing progress.
 */
public final class Log extends ForwardingList<Log.Entry> implements Serializable {
    private static final long serialVersionUID = -6902261600169770008L;

    /**
     * A entry in the log.
     */
    public class Entry implements Serializable {
        private static final long serialVersionUID = 3858758562130562079L;
        private LocalDate createdDate;
        private Optional<LocalTime> startTime;
        private Duration timeSpent;
        private int writtenCount;
        private int outlineCount;
        private int arrayIndex;

        /**
         * get the created date (log date)
         *
         * @return created date
         */
        public LocalDate getCreatedDate() {
            return createdDate;
        }

        /**
         * get the time spent
         *
         * @return time spent on writing
         *
         * @see #start()
         * @see #end(int, int)
         */
        public Duration getTimeSpent() {
            return timeSpent;
        }

        /**
         * get the writing word count
         *
         * @return time spent on writing
         *
         * @see #start()
         * @see #end(int, int)
         * @see #getTodayWritten()
         */
        public int getTotalWritten() {
            return writtenCount;
        }

        /**
         * Get today written word count
         *
         * @return today count
         *
         * @see #start()
         * @see #end(int, int)
         * @see #getTotalWritten()
         */
        public int getTodayWritten() {
            if (arrayIndex == 0) {
                return writtenCount;
            }
            return logEntries.get(arrayIndex - 1).writtenCount - writtenCount;
        }

        /**
         * get the outline count count
         *
         * @return time spent on writing
         *
         * @see #start()
         * @see #end(int, int)
         */
        public int getTotalOutline() {
            return outlineCount;
        }

        /**
         * Get today written word count
         *
         * @return today count
         *
         * @see #start()
         * @see #end(int, int)
         * @see #getTotalWritten()
         */
        public int getTodayOutline() {
            if (arrayIndex == 0) {
                return outlineCount;
            }
            return logEntries.get(arrayIndex - 1).outlineCount - outlineCount;
        }

        private Entry(LocalDate created, Duration time, int written, int outline, int index) {
            this.createdDate = created;
            this.timeSpent = time;
            this.writtenCount = written;
            this.outlineCount = outline;
            arrayIndex = index;
        }

        /**
         * Start writing
         *
         * @return {@code true} if timer starts.
         */
        public boolean start() {
            if (startTime.isEmpty()) {
                startTime = Optional.of(LocalTime.now());
                return true;
            }
            return false;
        }

        /**
         * Ending writing
         *
         * @param written
         *        total written text
         * @param outline
         *        total outline text
         *
         * @return {@code true} if timer ends.
         */
        public boolean end(int written, int outline) {
            writtenCount = written;
            outlineCount = outline;
            if (startTime.isPresent()) {
                timeSpent = timeSpent.plus(Duration.between(startTime.get(), LocalTime.now()));
                startTime = Optional.empty();
                return true;
            }
            return false;
        }

        /**
         * Get time worked, excluding researching
         *
         * @return {@code time worked}
         */
        public Duration getDuration() {
            if (startTime.isPresent()) {
                return timeSpent.plus(Duration.between(startTime.get(), LocalTime.now()));
            }
            return timeSpent;
        }
    }

    private ArrayList<Entry> logEntries;

    Log() {
        logEntries = new ArrayList<>();
    }

    @Override
    public boolean add(Entry e) {
        return logEntries.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends Entry> c) {
        return logEntries.addAll(c);
    }

    @Override
    protected List<Entry> delegate() {
        return logEntries;
    }

    /**
     * Get the current entry
     *
     * @return today entry
     */
    public Entry getCurrent() {
        Entry entry;
        LocalDate today = LocalDate.now();

        if (logEntries.isEmpty()) {
            entry = new Entry(today, Duration.ZERO, 0, 0, 0);
            logEntries.add(entry);
            return entry;
        }

        entry = logEntries.get(logEntries.size() - 1);
        if (entry.createdDate == today) {
            return entry;
        }

        entry = new Entry(today, Duration.ZERO, entry.writtenCount, entry.outlineCount, logEntries.size());
        logEntries.add(entry);
        return entry;
    }
}

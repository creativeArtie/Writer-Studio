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
         */
        public Duration getTimeSpent() {
            return timeSpent;
        }

        /**
         * get the writing word count
         *
         * @return time spent on writing
         *
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
         * @see #getTotalWritten()
         */
        public int getTodayWritten() {
            if (arrayIndex == 0) {
                return writtenCount;
            }
            return logEntries.get(arrayIndex - 1).writtenCount - writtenCount;
        }

        /**
         * get the outline word count
         *
         * @return time spent on outline
         *
         * @see #getTodayOutline()
         */
        public int getTotalOutline() {
            return outlineCount;
        }

        /**
         * Get today outline word count
         *
         * @return today outline
         *
         * @see #getTotalOutline()
         */
        public int getTodayOutline() {
            if (arrayIndex == 0) {
                return outlineCount;
            }
            return logEntries.get(arrayIndex - 1).outlineCount - outlineCount;
        }

        private Entry(LocalDate created, Duration time, int written, int index) {
            this.createdDate = created;
            this.timeSpent = time;
            this.writtenCount = written;
            arrayIndex = index;
        }

        void setWrittenCount(int wordCount) {
            writtenCount = wordCount;
        }

        void setOutlineCount(int wordCount) {
            outlineCount = wordCount;
        }

        void addTime(Duration duration) {
            timeSpent = duration.plus(timeSpent);
        }

        /**
         * Dose it meet the duration target
         *
         * @return {@code false} if goal not met or no goal set
         */
        public boolean meetDurationTarget() {
            return getTargetDuration().map((duration) -> timeSpent.compareTo(duration) > -1).orElseGet(() -> false);
        }

        /**
         * Dose it meet the word count target
         *
         * @return {@code false} if goal not met or no goal set
         */
        public boolean meetWordTarget() {
            return getTargetWordCount().map((goal) -> goal <= writtenCount).orElseGet(() -> false);
        }

        /**
         * Dose it meet the outline count target
         *
         * @return {@code false} if goal not met or no goal set
         */
        public boolean meetOutlineTarget() {
            return getTargetOutlineCount().map((goal) -> goal <= writtenCount).orElseGet(() -> false);
        }
    }

    private Duration targetDuration;
    private Integer targetWordCount;
    private Integer targetOutlineCount;
    private ArrayList<Entry> logEntries;

    Log() {
        logEntries = new ArrayList<>();
        targetDuration = null;
        targetWordCount = null;
        targetOutlineCount = null;
    }

    /**
     * Get the target duration.
     *
     * @return target duration
     */
    public Optional<Duration> getTargetDuration() {
        return Optional.ofNullable(targetDuration);
    }

    /**
     * Get the target word count.
     *
     * @return target word count
     */
    public Optional<Integer> getTargetWordCount() {
        return Optional.ofNullable(targetWordCount);
    }

    /**
     * Get the target word count.
     *
     * @return target word count
     */
    public Optional<Integer> getTargetOutlineCount() {
        return Optional.ofNullable(targetOutlineCount);
    }

    /**
     * Set the target duration.
     *
     * @param duration
     *        new target duration
     */
    public void setTargetDuration(Duration duration) {
        targetDuration = duration;
    }

    /**
     * Set the target word count.
     *
     * @param wordCount
     *        new target word count
     */
    public void setTargetWordCount(Integer wordCount) {
        targetWordCount = wordCount;
    }

    /**
     * Set the target word count.
     *
     * @param wordCount
     *        new target word count
     */
    public void setTargetOutlineCount(Integer wordCount) {
        targetOutlineCount = wordCount;
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
            entry = new Entry(today, Duration.ZERO, 0, 0);
            logEntries.add(entry);
            return entry;
        }

        entry = logEntries.get(logEntries.size() - 1);
        if (entry.createdDate == today) {
            return entry;
        }

        entry = new Entry(today, Duration.ZERO, entry.writtenCount, logEntries.size());
        logEntries.add(entry);
        return entry;
    }
}

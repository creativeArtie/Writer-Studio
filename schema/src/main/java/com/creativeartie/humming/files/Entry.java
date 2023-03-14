package com.creativeartie.humming.files;

import java.io.*;
import java.time.*;
import java.util.*;

public class Entry implements Serializable {
    private static final long serialVersionUID = 3858758562130562079L;
    private LocalDate createdDate;
    private Optional<LocalTime> startTime;
    private Duration timeSpent;
    private int writtenCount;
    private int outlineCount;

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public Duration getTimeSpent() {
        return timeSpent;
    }

    public int getWrittenCount() {
        return writtenCount;
    }

    public int getOutlineCount() {
        return outlineCount;
    }

    public int getTotalCount() {
        return writtenCount + outlineCount;
    }

    protected static Entry newEntry() {
        return new Entry(LocalDate.now(), Duration.ZERO, 0, 0);
    }

    private Entry(LocalDate created, Duration time, int written, int outline) {
        this.createdDate = created;
        this.timeSpent = time;
        this.writtenCount = written;
        this.outlineCount = outline;
    }

    public boolean start() {
        if (startTime.isEmpty()) {
            startTime = Optional.of(LocalTime.now());
            return true;
        }
        return false;
    }

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

    public Duration getDuration() {
        if (startTime.isPresent()) {
            return timeSpent.plus(Duration.between(startTime.get(), LocalTime.now()));
        }
        return timeSpent;
    }
}

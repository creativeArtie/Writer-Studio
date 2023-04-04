package com.creativeartie.humming.files;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

public final class Log extends ForwardingList<Entry> implements Serializable {
    private static final long serialVersionUID = -6902261600169770008L;
    private ArrayList<Entry> logEntries;
    private Entry currentEntry;

    Log() {
        logEntries = new ArrayList<>();
        currentEntry = Entry.newEntry();
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

    public Entry getCurrent() {
        return currentEntry;
    }
}

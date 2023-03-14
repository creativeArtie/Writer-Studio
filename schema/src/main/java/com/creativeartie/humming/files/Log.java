package com.creativeartie.humming.files;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;

public class Log extends ForwardingList<Entry> implements Serializable {
    private static final long serialVersionUID = -6902261600169770008L;
    private ArrayList<Entry> logEntries;
    private Entry currentEntry;

    public Log() {
        logEntries = new ArrayList<>();
        currentEntry = Entry.newEntry();
    }

    public void writeToFile(String path) throws FileNotFoundException, IOException {
        try (ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(path))) {
            file.writeObject(this);
        }
    }

    public static Log loadFromFile(String path) throws FileNotFoundException, IOException, ClassNotFoundException {
        try (ObjectInputStream file = new ObjectInputStream(new FileInputStream(path))) {
            return (Log) file.readObject();
        }
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

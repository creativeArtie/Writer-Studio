package com.creativeartie.humming.main;

import org.junit.jupiter.api.*;

import javafx.beans.property.*;

public class PropertyBindingDemo {
    @SuppressWarnings("nls")
    @Test
    void tester() {
        IntegerProperty i = new SimpleIntegerProperty(null, "i", 1024);
        LongProperty l = new SimpleLongProperty(null, "l", 0L);

        System.out.println("Initial");
        System.out.println("i.get() = " + i.get());
        System.out.println("l.get() = " + l.get());

        System.out.println("Bind l");
        l.bind(i);

        System.out.println("Set i");
        i.set(2048);

        System.out.println("i.get() = " + i.get());
        System.out.println("l.get() = " + l.get());

        System.out.println("Set l");
        try {
            l.set(233);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        System.out.println("i.get() = " + i.get());
        System.out.println("l.get() = " + l.get());

        System.out.println("Unbind l");
        l.unbind();
        System.out.println("i.get() = " + i.get());
        System.out.println("l.get() = " + l.get());

        System.out.println("Set i");
        i.set(64);
        System.out.println("i.get() = " + i.get());
        System.out.println("l.get() = " + l.get());

        System.out.println("Set l");
        l.set(128);
        System.out.println("i.get() = " + i.get());
        System.out.println("l.get() = " + l.get());
        System.out.println();
    }

    @SuppressWarnings("nls")
    @Test
    void testBindTwice() {
        IntegerProperty i = new SimpleIntegerProperty(null, "i", 1024);
        LongProperty l = new SimpleLongProperty(null, "l", 0L);
        LongProperty k = new SimpleLongProperty(null, "l", 24L);

        System.out.println("Initial");
        System.out.println("i.get() = " + i.get());
        System.out.println("l.get() = " + l.get());
        System.out.println("k.get() = " + k.get());
        i.bind(l);
        i.bind(k);
        l.set(12L);
        System.out.println("After bind + set l");
        System.out.println("i.get() = " + i.get());
        System.out.println("l.get() = " + l.get());
        System.out.println("k.get() = " + k.get());
        System.out.println("After bind + set k");
        k.setValue(30L);
        System.out.println("i.get() = " + i.get());
        System.out.println("l.get() = " + l.get());
        System.out.println("k.get() = " + k.get());
        System.out.println();
    }
}

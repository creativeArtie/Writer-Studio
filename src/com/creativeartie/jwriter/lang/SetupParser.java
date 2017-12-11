package com.creativeartie.jwriter.lang;

import java.util.*;

import static com.creativeartie.jwriter.main.Checker.*;

/**
 * Creator for the {@link Span}.
 */
public interface SetupParser{

    /** Put two {@linkplain String} list together. */
    public static String[] combine(String[] list1, String[] list2){
        checkNotNull(list1, "list1");
        checkNotNull(list2, "list2");

        String[] res = new String[list1.length + list2.length];
        System.arraycopy(list1, 0, res, 0, list1.length);
        System.arraycopy(list2, 0, res, list1.length, list2.length);
        return res;
    }

    /** Put two {@link SetupParser} list together. */
    public static  SetupParser[] combine(SetupParser[] list1,
            SetupParser[] list2) {
        checkNotNull(list1, "list1");
        checkNotNull(list2, "list2");
        SetupParser[] res = new SetupParser[list1.length + list2.length];
        System.arraycopy(list1, 0, res, 0, list1.length);
        System.arraycopy(list2, 0, res, list1.length, list2.length);
        return res;
    }

    /**
     * Creates a {@link Span} from text text in the {@link SetupPointer} and add
     * to the children list if found.
     */
    public default boolean parse(List<Span> children, SetupPointer pointer){
        checkNotNull(children, "children");
        checkNotNull(pointer, "pointer");

        Optional<SpanBranch> child = parse(pointer);
        child.ifPresent(c -> children.add(c));
        return child.isPresent();
    }

    /**
     * Creates a {@link Span} from the text in the {@link SetupPointer}. If
     * nothing is created the {@link SetupPointer} shouldn't change it's
     * pointer, and return {@link Optional#empty()}.
     */
    public Optional<SpanBranch> parse(SetupPointer pointer);
}

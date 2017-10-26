package com.creativeartie.jwriter.lang;

import java.util.List;
import java.util.Optional;

/**
 * Creator for the {@link Span}.
 */
public interface SetupParser{
    
    /** Put two {@linkplain String} list together. */
    public static String[] combine(String[] list1, String[] list2){
        String[] res = new String[list1.length + list2.length];
        System.arraycopy(list1, 0, res, 0, list1.length);
        System.arraycopy(list2, 0, res, list1.length, list2.length);
        return res;
    }
    
    /** Put two {@link SetupParser} list together. */
    public static  SetupParser[] combine(SetupParser[] list1, 
        SetupParser[] list2)
    {
        SetupParser[] res = new SetupParser[list1.length + list2.length];
        System.arraycopy(list1, 0, res, 0, list1.length);
        System.arraycopy(list2, 0, res, list1.length, list2.length);
        return res;
    }
    
    public default boolean parse(List<Span> children, SetupPointer pointer){
        Optional<SpanBranch> child = parse(pointer);
        child.ifPresent(c -> children.add(c));
        return child.isPresent();
    }
    
    /**
     * Creates a {@link Span} base on the Parsers. If the span does not exist, it
     * should <em> not </em> move the {@link SetupPointer} and returns 
     * {@code Optional.empty()}.
     */
    public Optional<SpanBranch> parse(SetupPointer pointer);
}

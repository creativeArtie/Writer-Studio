package com.creativeartie.writerstudio.lang;

import java.util.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Parse a text to create a {@link SpanBranch}.
 *
 * Purpose
 * <ul>
 * <li>Provides helper methods to combine list</li>
 * <li>Parse text into {@link SpanBranch}, if possible.</li>
 */
public interface SetupParser{

    /** Put two {@linkplain String} list together.
     *
     * @param list1
     *      first list
     * @param list2
     *      second list
     * @return answer
     */
    public static String[] combine(String[] list1, String ... list2){
        argumentNotNull(list1, "list1");
        argumentNotNull(list2, "list2");

        String[] res = new String[list1.length + list2.length];
        System.arraycopy(list1, 0, res, 0, list1.length);
        System.arraycopy(list2, 0, res, list1.length, list2.length);
        return res;
    }

    /** Put two {@link SetupParser} list together.
     *
     * @param list1
     *      first list
     * @param list2
     *      second list
     * @return answer
     */
    public static  SetupParser[] combine(SetupParser[] list1,
            SetupParser ... list2) {
        argumentNotNull(list1, "list1");
        argumentNotNull(list2, "list2");
        SetupParser[] res = new SetupParser[list1.length + list2.length];
        System.arraycopy(list1, 0, res, 0, list1.length);
        System.arraycopy(list2, 0, res, list1.length, list2.length);
        return res;
    }

    /** Parse a {@link SpanBranch} and add to a {@linkplain List}, if possible.
     *
     * @param children
     *      list of Span children
     * @param pointer
     *      text pointer
     */
    public default boolean parse(SetupPointer pointer, List<Span> children){
        argumentNotNull(pointer, "pointer");
        argumentNotNull(children, "children");

        Optional<SpanBranch> child = parse(pointer);
        child.ifPresent(c -> children.add(c));
        return child.isPresent();
    }

    /** Parse a {@link SpanBranch} and return empty or {@link SpanBranch}.
     *
     * @param pointer
     *      text pointer
     */
    public Optional<SpanBranch> parse(SetupPointer pointer);
}

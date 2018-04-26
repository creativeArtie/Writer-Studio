package com.creativeartie.writerstudio.lang;

import java.util.*; // Comparator;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** A key to use for the caches in {@link SpanNode}.
 *
 * Purpose:
 * <ul>
 * <li>Unique idenifly from each other.</li>
 * <li>Cast the value to the actual type.</li>
 * </ul>
 * @param
 *      the value type
 */
public abstract class CacheKey<T> implements Comparable<CacheKey<?>>{

    private static int id = Integer.MIN_VALUE;

    /** Get a unique hash id number.
     *
     * @return answer
     * @see #CacheKey()
     */
    private static int getHashId(){
        int ans = id++;
        return ans;
    }

    private int hashId;

    /** Creates a {@linkplain CacheKey} */
    CacheKey(){
        hashId = getHashId();
    }

    /** Cast a value
     *
     * @param value
     *      found value
     * @return answer
     * @see SpanNode#getDocCache(CacheKeyMain, java.util.concurrent.Callable)
     * @see SpanNode#getLocalCache(CacheKeyMain, java.util.concurrent.Callable)
     * @see SpanNode#getDocCache(CacheKeyOptional, java.util.concurrent.Callable)
     * @see SpanNode#getLocalCache(CacheKeyOptional, java.util.concurrent.Callable)
     * @see SpanNode#getDocCache(CacheKeyList, java.util.concurrent.Callable)
     * @see SpanNode#getLocalCache(CacheKeyList, java.util.concurrent.Callable)
     */
    abstract T cast(T value);

    @Override
    public int compareTo(CacheKey<?> that){
        Comparator<CacheKey<?>> compare = Comparator.comparingInt(k -> k.hashId);
        return compare.compare(this, that);
    }

    @Override
    public int hashCode(){
        return hashId;
    }

    @Override
    public boolean equals(Object obj){
        return obj instanceof CacheKey? ((CacheKey) obj).hashId == hashId:
            false;
    }

}
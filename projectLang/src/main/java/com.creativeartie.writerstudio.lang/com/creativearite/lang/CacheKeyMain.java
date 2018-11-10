package com.creativeartie.writerstudio.lang;

import static com.creativeartie.writerstudio.util.ParameterChecker.*;

/** A {@link CacheKey} for a single value.
 *
 * @param T
 *      list value type
 */
public final class CacheKeyMain<T> extends CacheKey<T>{

    /** Creates a string {@linkplain CacheKey}. */
    public static CacheKeyMain<String> stringKey(){
        return new CacheKeyMain<>(String.class);
    }

    /** Creates a boolean {@linkplain CacheKey}. */
    public static CacheKeyMain<Boolean> booleanKey(){
        return new CacheKeyMain<>(Boolean.class);
    }

    /** Creates a integer {@linkplain CacheKey}. */
    public static CacheKeyMain<Integer> integerKey(){
        return new CacheKeyMain<>(Integer.class);
    }

    private Class<T> valueCaster;

    /** Creates a {@linkplain CacheKey}.
     *
     * @param caster
     *      value caster
     */
    public CacheKeyMain(Class<T> caster){
        valueCaster = argumentNotNull(caster, "caster");
    }

    @Override
    T cast(Object value){
        return valueCaster.cast(value);
    }
}

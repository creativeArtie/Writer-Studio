package com.creativeartie.writer.lang;

import java.util.*;

import static com.creativeartie.writer.main.ParameterChecker.*;

/** A {@link CacheKey} for {@linkplain List}.
 *
 * @param T
 *      list value type
 */
public final class CacheKeyList<T> extends CacheKey<List<?>>{

    private Class<T> valueCaster;

    /** Creates a {@linkplain CacheKey}.
     *
     * @param caster
     *      value caster
     */
    public CacheKeyList(Class<T> caster){
        valueCaster = caster;
    }

    @Override
    List<T> cast(List<?> value){
        ArrayList<T> ans = new ArrayList<>();
        int i = 0;

        for (Object item : value){
            ans.add(argumentClass(item, "value.get(" + i + ")",
                valueCaster));
            i++;
        }
        return ans;
    }
}

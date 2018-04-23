package com.creativeartie.writerstudio.lang;

import java.util.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

public class CacheKey implements Comparable<CacheKey>{
    public static CacheKeyMain<String> stringKey(){
        return new CacheKeyMain<>(String.class);
    }

    public static CacheKeyMain<Boolean> booleanKey(){
        return new CacheKeyMain<>(Boolean.class);
    }

    public static CacheKeyMain<Integer> integerKey(){
        return new CacheKeyMain<>(Integer.class);
    }

    private static int id = Integer.MIN_VALUE;

    private static int getHashId(){
        int ans = id++;
        return ans;
    }

    private int hashId;

    CacheKey(){
        hashId = getHashId();
    }

    @Override
    public int compareTo(CacheKey that){
        Comparator<CacheKey> compare = Comparator.comparingInt(k -> k.hashId);
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
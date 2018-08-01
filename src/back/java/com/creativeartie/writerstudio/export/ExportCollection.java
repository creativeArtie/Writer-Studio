package com.creativeartie.writerstudio.export;

import java.util.*;

import com.google.common.collect.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Export a span of text */
abstract class ExportCollection<T extends Number, U> extends ForwardingList<U> {

    @Override
    protected final List<U> delegate(){
        return ImmutableList.copyOf(delegateRaw());
    }

    abstract List<U> delegateRaw();
}

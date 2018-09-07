package com.creativeartie.writerstudio.export;

import java.util.*;

import com.google.common.collect.*;

abstract class ExportCollection<T> extends ForwardingList<T>{

    @Override
    protected List<T> delegate(){
        return ImmutableList.copyOf(getChildren());
    }

    protected abstract List<T> getChildren();
}

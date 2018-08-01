package com.creativeartie.writerstudio.export;

import java.util.*;

import com.google.common.collect.*;

abstract class ExportCollection<T> extends ForwardingList<T>{
    private Optional<ImmutableList<T>> outputContent;

    ExportCollection(){
        outputContent = Optional.empty();
    }

    @Override
    protected List<T> delegate(){
        if (! outputContent.isPresent()){
            List<T> children = getChildren();
            if (children.isEmpty()){
                return ImmutableList.of();
            }
            outputContent = Optional.of(ImmutableList.copyOf(children));
        }
        return outputContent.get();
    }

    protected abstract List<T> getChildren();
}

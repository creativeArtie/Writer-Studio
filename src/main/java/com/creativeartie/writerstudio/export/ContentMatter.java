package com.creativeartie.writerstudio.export;

import java.util.*;

public interface ContentMatter extends Iterable<ContentLine>{
    // public OutputPageInfo getPageInfo;

    @Override
    public default Iterator<ContentLine> iterator(){
        return iterator(0);
    }

    public Iterator<ContentLine> iterator(int from);
}

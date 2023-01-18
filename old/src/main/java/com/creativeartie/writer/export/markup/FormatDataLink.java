package com.creativeartie.writer.export.markup;

import java.util.*;
import com.creativeartie.writer.export.*;
import com.creativeartie.writer.lang.*;
import com.creativeartie.writer.lang.markup.*;

public final class FormatDataLink extends FormatData<FormatSpanLink> {

    FormatDataLink(FormatSpanLink span){
        super(span);
    }

    public boolean isFootnote(){
        return false;
    }

    public void updatePageInfo(OutputPageInfo info){}

    public String getText(){
        return getTargetSpan().getText();
    }

    public Optional<String> getLinkPath(){
        if (getTargetSpan() instanceof FormatSpanLinkDirect){
            String path = ((FormatSpanLinkDirect) getTargetSpan()).getPath();
            if (! path.isEmpty()){
                return Optional.of(path);
            }
        } else if (getTargetSpan() instanceof FormatSpanLinkRef){
            Optional<SpanBranch> path =
                ((FormatSpanLinkRef) getTargetSpan()).getPathSpan();
            if (path.isPresent()){
                SpanBranch found = path.get();
                if (found instanceof LinedSpanPointLink){
                    return Optional.of(((LinedSpanPointLink)found).getPath())
                        .filter(p -> ! p.isEmpty());
                } else if (found instanceof LinedSpanLevelSection){
                    return ((LinedSpanLevelSection)found)
                        .spanFromFirst(DirectorySpan.class)
                        .map(s -> s.getLookupText());
                }
            }
        }
        return Optional.empty();
    }
}

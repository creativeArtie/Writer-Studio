package com.creativeartie.jwriter.window;

import java.util.*;

import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.lang.*;

import com.google.common.collect.*;

final class EditUpdated extends ForwardingList<Span>{

    private List<Span> spans;
    private Optional<FormatSpan> formatted;
    private boolean disableFormatters;
    private boolean contentSpan;

    public EditUpdated(ManuscriptDocument document, int position){
        spans = ImmutableList.copyOf(document.spansAt(position));
        formatted = findBranch(FormatSpan.class);
        disableFormatters = (! findBranch(FormatSpanMain.class).isPresent()) ||
            findBranch(FormatSpanAgenda.class).isPresent();
        contentSpan =! findBranch(FormatSpanContent.class).isPresent();
    }

    @Override
    protected List<Span> delegate() {
        return spans;
    }

    public <T extends SpanBranch> Optional<T> findBranch(Class<T> find){
        for (Span span: this){
            if (find.isInstance(span)){
                return Optional.of(find.cast(span));
            }
        }
        return Optional.empty();
    }

    public SetupLeafStyle getLeafStyle(){
        return ((SpanLeaf)get(size() - 1)).getLeafStyle();
    }

    public Optional<FormatSpan> getFormatSpan(){
        return formatted;
    }

    public boolean hasContentSpan(){
        return contentSpan;
    }
}
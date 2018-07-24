package com.creativeartie.writerstudio.export;

import java.util.*;

import com.creativeartie.writerstudio.lang.markup.*;

/** Draws contents on the page */
abstract class DivisionText<T extends Number> extends Division<T>
    implements Iterable<DivisionLine<T>>
{

    private final ArrayList<DivisionLine<T>> childLines;
    private final LinedSpan renderSpan;

    DivisionText(RenderDivision<T> renderer, LinedSpan span){
        super(renderer);
        renderSpan = span;
        childLines = new ArrayList<>();
        fillChildren(getContent());
    }

    private final void fillChildren(List<FormatSpan> children){
        DivisionLine<T> line = new DivisionLine<>(getDivisionRenderer());
        childLines.add(line);
        for (FormatSpan child: children){
            Optional<ContentText<T>> leftover = line.append(child);
            while (leftover.isPresent()){
                line = new DivisionLine<>(getDivisionRenderer());
                childLines.add(line);
                leftover = line.append(child);
            }
        }
    }

    @Override
    public Iterator<DivisionLine<T>> iterator(){
        return childLines.iterator();
    }

    abstract List<FormatSpan> getContent();
}

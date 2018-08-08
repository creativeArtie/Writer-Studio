package com.creativeartie.writerstudio.export;

import java.util.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

public class ExportLineData<T extends Number>
    extends ExportCollection<ExportData<T>>
{
    private RenderLine<T> renderExporter;
    private ArrayList<ExportData<T>> outputContent;
    private final boolean isBegin;
    private T fillWidth;

    ExportLineData(boolean begin, RenderLine<T> render){
        isBegin = begin;
        renderExporter = render;
        outputContent = new ArrayList<>();
        fillWidth = null;
    }

    private ExportLineData(ExportLineData<T> self){
        this(false, self.renderExporter);
    }

    Optional<ExportLineData<T>> append(ExportData<T> data){
        T space = renderExporter.getWidthSpace(fillWidth, isBegin);
        Optional<ExportData<T>> overflow = data.split(space);
        if (data.isFilled()) outputContent.add(data);
        if (overflow.isPresent()){
            ExportLineData<T> ans = new ExportLineData<>(this);
            ans.outputContent.add(overflow.get());
            return Optional.of(ans);
        }
        return Optional.empty();
    }

    Optional<ExportLineData<T>> render(){
        stateCheck(outputContent.size() == 1,
            "content is not contains only 1 data.");
        ExportData<T> data = outputContent.get(0);
        outputContent.clear();
        return append(data);
    }

    T getAllHeight(List<ExportLineMain<T>> current){
        T cur = getFillHeight();
        for(ExportLineMain<T> note: getFootnotes(current)){
            cur = renderExporter.addHeight(cur, note.getFillHeight());
        }
        return cur;
    }

    T getFillHeight(){
        T cur = null;
        for (ExportData<T> content: outputContent){
            cur = renderExporter.compareHeight(cur, content.getFillHeight());
        }
        return cur;
    }

    List<ExportLineMain<T>> getFootnotes(List<ExportLineMain<T>> current){
        ArrayList<ExportLineMain<T>> notes = new ArrayList<>();
        for (ExportData<T> data: outputContent){
            Optional<ExportLineMain<T>> found = data.getFootnote();
            found.filter(l -> ! current.contains(l))
                .ifPresent(l -> notes.add(l));
        }
        return notes;
    }

    List<ExportData<T>> updateContent(OutputPageInfo info){
        for(ExportData<T> data: outputContent){
            data.updateContent(info);
        }
        return outputContent;
    }

    @Override
    protected List<ExportData<T>> getChildren(){
        return outputContent;
    }
}

package com.creativeartie.writerstudio.export;

import java.util.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

public final class ExportLineMain<T extends Number>
    extends ExportCollection<ExportLineData<T>>
    implements Comparable<ExportLineMain<T>>
{

    private final ContentLine inputContent;
    private final RenderLine<T> renderExporter;
    private T fillHeight;
    private final ArrayList<ExportLineData<T>> outputContent;
    private final boolean isBegin;

    public ExportLineMain(ContentLine input, RenderLine<T> renderer){
        inputContent = input;
        renderExporter = renderer;
        outputContent = new ArrayList<>();
        isBegin = true;
    }

    private ExportLineMain(ExportLineMain<T> self,
        ArrayList<ExportLineData<T>> output
    ){
        inputContent = self.inputContent;
        renderExporter = self.renderExporter;

        fillHeight = null;
        outputContent = new ArrayList<>(output);
        isBegin = false;
    }

    void render(){
        ArrayList<ExportData<T>> data = new ArrayList<>();
        DataLineType type = inputContent.getLineType();
        for (ContentData content: inputContent){
            data.add(new ExportData<>(content, type, renderExporter.newData()));
        }
        render(data);
    }

    private void render(List<ExportData<T>> data){
        assert outputContent.isEmpty(): "output not Empty";
        fillHeight = null;
        ExportLineData<T> cur = new ExportLineData<T>(isBegin, renderExporter);
        outputContent.add(cur);

        int i = 0;
        for (ExportData<T> content: data){
            if (data.get(i + 1).isKeepLast()){
                content.addKeepNext(data.get(i + 1).getFillWidth());
            }
            Optional<ExportLineData<T>> overflow = cur.append(content,
                inputContent.getLineType());
            while (overflow.isPresent()){
                cur = overflow.get();
                outputContent.add(cur);
                overflow = cur.render();
            }
            i++;
        }
        if (cur.isEmpty()){
            outputContent.remove(cur);
        }
    }

    ExportLineMain<T> splitAt(int at){
        argumentCloseOpen(at, "at", 0, size());
        ArrayList<ExportLineData<T>> start = new ArrayList<>(
            outputContent.subList(0, at)
        );
        ArrayList<ExportLineData<T>> end = new ArrayList<>(
            outputContent.subList(at, outputContent.size())
        );
        clearContent();
        outputContent.clear();
        outputContent.addAll(start);
        return new ExportLineMain<>(this, end);
    }

    T getFillHeight(){
        if (fillHeight == null){
            for(ExportLineData<T> data: outputContent){
                fillHeight = renderExporter.addSize(fillHeight,
                    data.getFillHeight());
            }
        }
        return fillHeight;
    }

    void updatePageInfo(OutputPageInfo info){
        inputContent.updatePageInfo(info);
        ArrayList<ExportData<T>> updated = new ArrayList<>();
        for(ExportLineData<T> line: outputContent){
            updated.addAll(line.updateContent(info));
        }
        outputContent.clear();
        clearContent();
        render(updated);
    }

    @Override
    public int compareTo(ExportLineMain<T> compare){
        return inputContent.compareTo(compare.inputContent);
    }

    @Override
    protected List<ExportLineData<T>> getChildren(){
        return outputContent;
    }

    @Override
    public String toString(){
        String text = "[\n";
        for (ExportLineData<T> line: this){
            text += "\t" + line + "\n";
        }
        return text + "]";
    }
}

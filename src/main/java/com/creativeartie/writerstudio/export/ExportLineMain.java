package com.creativeartie.writerstudio.export;

import java.util.*;

public final class ExportLineMain<T extends Number>
    extends ExportCollection<ExportLineData<T>>
{

    private final ContentLine inputContent;
    private final RenderLine<T> renderExporter;
    private T fillHeight;
    private final ArrayList<ExportLineData<T>> outputContent;

    public ExportLineMain(ContentLine input, RenderLine<T> renderer){
        inputContent = input;
        renderExporter = renderer;
        outputContent = new ArrayList<>();
    }

    void render(){
    }
    
    private void render(ExportData<T> data){
	}
    
    ExportLineMain<T>[] splitAt(int at){
		ArrayList<ExportLineData<T>> keep = new ArrayList<>();
		ArrayList<ExportLineData<T>> overflow = new ArrayList<>();
		int i = 0;
		for (ExportLineData<T> line: outputContent){
			if(i < at){
				keep.add(line);
			} else {
				overflow.add(line);
			}
		}
		ExportLineMain ans;
		return new ExportLineMain[]{this, ans};
	}

    T getFillHeight(){
        return fillHeight;
    }
    
    void setPageInfo(OutputPageInfo info){
		inputContent.setPageInfo(info);
		ArrayList<ContentData> updated = new ArrayList<>();
		for(ExportLineData<T> line: outputContent){
			updated.addAll(line.updateContent(info));
		}
		render(updated);
	}
 
    @Override
    protected List<ExportLineData<T>> getChildren(){
        return outputContent;
    }
}

package com.creativeartie.writerstudio.export;

import java.util.*;

public final class ExportMatterRunning<T extends Number> extends ExportMatter<T>{
    private final ContentMatter inputContent;

    public ExportMatterRunning(ContentMatter input, RenderMatter<T> render){
        super(render);
        inputContent = input;
    }


    void render(){
        for(ContentLine input: inputContent){
            RenderLine<T> renderer = getRenderer().newLine();
            ExportLineMain<T> output = new ExportLineMain<>(input, renderer);
            output.render();
            updateHeight(output);
        }
    }
}

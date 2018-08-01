package com.creativeartie.writerstudio.export;

import java.util.*;

public final class ExportMatterFootnote<T extends Number> extends ExportMatter<T>{

    public ExportMatterFootnote(RenderMatter<T> render){
        super(render);
    }

    void addNotes(List<ExportLineMain<T>> notes){
        for(ExportLineMain<T> note: notes){
            getChildren().add(note);
            addFillHeight(note.getFillHeight());
        }
    }
}

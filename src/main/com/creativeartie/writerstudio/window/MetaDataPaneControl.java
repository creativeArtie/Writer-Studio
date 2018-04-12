package com.creativeartie.writerstudio.window;

import java.util.*;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.creativeartie.writerstudio.lang.markup.*;

class MetaDataPaneControl extends MetaDataPaneView{

    @Override
    protected void loadMetaData(WritingData data){
        for (TextDataType.Meta meta: TextDataType.Meta.values()){
            getTextField(meta).setText(data.getMetaRaw(meta));
        }
    }

    @Override
    protected void updateMeta(TextDataType.Meta meta, String text){
        if (isDocumentLoaded()){
            getMetaData().setMetaText(meta, text);
        }
    }

    @Override
    protected void updateArea(TextDataType.Area area){
        System.out.println(area);

    }
}
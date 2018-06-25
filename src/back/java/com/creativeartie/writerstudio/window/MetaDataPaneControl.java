package com.creativeartie.writerstudio.window;

import java.util.*;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.creativeartie.writerstudio.lang.markup.*;

class MetaDataPaneControl extends MetaDataPaneView{

    @Override
    protected void loadMetaData(WritingData data){
        for (TextTypeInfo meta: TextTypeInfo.values()){
            getTextField(meta).setText(data.getInfo(meta));
        }
    }

    @Override
    protected void updateMeta(TextTypeInfo meta, String text){
        if (isDocumentLoaded()){
            getMetaData().setInfo(meta, text);
        }
    }

    @Override
    protected void updateArea(TextTypeMatter area){
        new MetaDataEditWindow(area, getMetaData()).show();
    }
}

package com.creativeartie.writerstudio.javafx;

import com.creativeartie.writerstudio.lang.markup.*;

class MetaDataPaneControl extends MetaDataPaneView{

    private WindowMatterControl matterWindow;
    private WritingData writingData;

    MetaDataPaneControl(){
        matterWindow = new WindowMatterControl();
    }

    @Override
    protected void setupChildern(WriterSceneControl control){
        matterWindow.setupProperties(control);
        control.writingDataProperty().addListener((d, o, n) -> loadData(n));
        for (TextTypeMatter matter: TextTypeMatter.values()){
            getMatterButton(matter).setOnAction(evt -> editMatter(matter));
        }
    }

    private void loadData(WritingData data){
        for (TextTypeInfo meta: TextTypeInfo.values()){
            getInfoField(meta).setText(data.getInfo(meta));
        }
        writingData = data;
    }

    private void editMatter(TextTypeMatter matter){
        matterWindow.setShowMatter(matter);
        matterWindow.show();
    }
}

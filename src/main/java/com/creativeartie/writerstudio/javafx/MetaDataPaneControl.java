package com.creativeartie.writerstudio.javafx;

import com.creativeartie.writerstudio.lang.markup.*;

class MetaDataPaneControl extends MetaDataPaneView{

    /// %Part 1: Private Fields and Constructor

    private WindowMatterControl matterWindow;
    private WritingData writingData;

    MetaDataPaneControl(){
        matterWindow = new WindowMatterControl();
    }

    /// %Part 2: Property Binding
    /// %Part 3: Bind Children Properties

    @Override
    protected void bindChildren(WriterSceneControl control){
        matterWindow.postLoad(control);
        control.writingDataProperty().addListener(
            (d, o, n) -> listenWritingData(n)
        );
        for (TextTypeInfo info: TextTypeInfo.values()){
            getInfoField(info).setOnAction(e -> listenInfo(info));
        }
        for (TextTypeMatter matter: TextTypeMatter.values()){
            getMatterButton(matter).setOnAction(evt -> listenMatter(matter));
        }
    }

    /// %Part 3.1: control.writingDataProperty()

    private void listenWritingData(WritingData data){
        writingData = data;
        if (data != null){
            writingData.addDocEdited(s -> listenWritingData());
            listenWritingData();
        }
    }

    private void listenWritingData(){
        if (writingData == null) return;
        for (TextTypeInfo info: TextTypeInfo.values()){
            getInfoField(info).setText(writingData.getInfo(info));
        }
    }

    /// %Part 3.2: getInfoField(type).setOnAction(...)

    private void listenInfo(TextTypeInfo info){
        writingData.setInfo(info, getInfoField(info).getText());
    }

    /// %Part 3.3: getMatterButton(type).setOnAction(...)

    private void listenMatter(TextTypeMatter matter){
        matterWindow.setShowMatter(matter);
        matterWindow.show();
    }
}

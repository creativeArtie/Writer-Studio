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
        for (TextTypeInfo info: TextTypeInfo.values()){
			getInfoField(info).setOnAction(e -> editInfo(info));
		}
        for (TextTypeMatter matter: TextTypeMatter.values()){
            getMatterButton(matter).setOnAction(evt -> editMatter(matter));
        }
    }

    private void loadData(WritingData data){
        writingData = data;
        if (data != null){
			writingData.addDocEdited(s -> updateData());
			updateData();
		}
    }
    
    private void updateData(){
		if (writingData == null) return;
		for (TextTypeInfo info: TextTypeInfo.values()){
            getInfoField(info).setText(writingData.getInfo(meta));
        }
	}
    
    private void editInfo(TextTypeInfo info){
		writingData.setInfo(info, getInfoField(info).getText());
	}

    private void editMatter(TextTypeMatter matter){
        matterWindow.setShowMatter(matter);
        matterWindow.show();
    }
}

package com.creativeartie.writerstudio.javafx;

import javafx.scene.web.*;

final class ResearchPaneControl extends ResearchPaneView {
	
    @Override
    protected void setupChildern(WriterSceneControl control){
		getWebEngine().titleProperty().addListener((d, o, n) ->
			getTitleLabel().setText(n));
		getWebEngine().locationProperty().addListener((d, o, n) ->
			getAddressBarField().setText(n));
			
		WebHistory history = getWebEngine().getHistory();
		
		getTimeOutLabel().setText("Hello");
		getClockLabel().setText("World!");
		
		getAddressBarField().setOnAction(evt -> 
			loadUrl(getAddressBarField().getText())
		);
	}
	
	private void loadUrl(String text){
		if (! text.startsWith("http")){
			text = "http://" + text;
		}
		getWebEngine().load(text);
	}
}

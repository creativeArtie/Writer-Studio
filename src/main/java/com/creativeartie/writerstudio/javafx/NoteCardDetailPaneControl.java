package com.creativeartie.writerstudio.javafx;

import com.creativeartie.writerstudio.lang.markup.*;

class NoteCardDetailPaneControl extends NoteCardDetailPaneView{
    /// %Part 1: setupChildren

    private NoteCardSpan selectedSpan;

    @Override
    protected void setupChildern(WriterSceneControl control){
        showCardProperty().addListener((d, o, n) -> loadCard(n));
        loadCard(null);
    }

    private void loadCard(NoteCardSpan span){
        if (span == null){
            getTabs().clear();
            getTabs().add(getEmptyTab());
        }
    }

}

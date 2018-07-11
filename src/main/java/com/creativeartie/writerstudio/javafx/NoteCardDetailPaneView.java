package com.creativeartie.writerstudio.javafx;

import javafx.scene.control.*;
import javafx.beans.property.*;

import com.creativeartie.writerstudio.lang.markup.*;

abstract class NoteCardDetailPaneView extends TitledPane{
    /// %Part 1: Constructor and Class Fields

    private SimpleObjectProperty<NoteCardSpan> showCard;

    NoteCardDetailPaneView(){
        setCollapsible(false);

        showCard = new SimpleObjectProperty<>(this, "showCard");
    }

    /// %Part 3: Setup Properties

    public void setupProperties(WriterSceneControl control){
        setupChildern(control);
    }

    protected abstract void setupChildern(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 4.1: Show Note

    public ObjectProperty<NoteCardSpan> showCardProperty(){
        return showCard;
    }

    public NoteCardSpan getShowCard(){
        return showCard.getValue();
    }

    public void setShowCard(NoteCardSpan value){
        showCard.setValue(value);
    }

    /// %Part 5: Get Child Methods
}

package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

abstract class NoteCardTreeView extends TreeView<String>{

    /// %Part 1: Constructor and Class Fields
    public NoteCardTreeView(){

    }

    /// %Part 2: Layout


    /// %Part 3: Setup Properties

    public void setupProperties(WriterSceneControl control){
        setupChildern(control);
    }

    protected abstract void setupChildern(WriterSceneControl control);

    /// %Part 4: Properties

    /// %Part 5: Get Child Methods
}

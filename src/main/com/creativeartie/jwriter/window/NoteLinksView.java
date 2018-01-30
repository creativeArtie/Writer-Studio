package com.creativeartie.jwriter.window;

import java.util.*;
import java.util.Optional;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.scene.control.cell.*;

import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.property.window.*;

import com.google.common.base.*;
import com.google.common.collect.*;

/**
 * Stores a list hyperlinks.
 */
abstract class NoteLinksView extends Tab{
    private TableView<NoteLinkData> linkTable;
    public NoteLinksView(){
        super(WindowText.LINK_TAB.getText());
        setContent(new Label("Hello World"));
        // linkTable = initLinkTable();
    }

    private TableView<NoteLinkData> initLinkTable(){
        TableView<NoteLinkData> ans = new TableView<>();
        setContent(ans);
        return ans;
    }
}
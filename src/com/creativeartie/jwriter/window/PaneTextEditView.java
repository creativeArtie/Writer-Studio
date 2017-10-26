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
import javafx.geometry.*;

import com.creativeartie.jwriter.lang.markup.*;

abstract class PaneTextEditView extends GridPane {
    private ArrayList<EditNode> controls;

    private PaneTextEditStyle styles;
    private PaneTextEditFormat[] formats;
    private PaneTextEditIdentity id;
    private PaneTextEditStatus status;
    private PaneTextEditLinkMain[] links;
    private PaneTextEditLinkText linkText;
    private PaneTextEditDirectory[] refSpans;
    private PaneTextEditSpanAgenda agenda;
    private PaneTextEditEscape escape;
    private PaneTextEditData data;

    private SimpleIntegerProperty position;
    private SimpleObjectProperty<ManuscriptDocument> document;

    public PaneTextEditView(){
        styles = new PaneTextEditStyle();
        formats = PaneTextEditFormat.getFormats();
        id = new PaneTextEditIdentity();
        status = new PaneTextEditStatus();
        links = PaneTextEditLinkMain.getSpans();
        linkText = new PaneTextEditLinkText();
        refSpans = PaneTextEditDirectory.getSpans();
        agenda = new PaneTextEditSpanAgenda();
        escape = new PaneTextEditEscape();
        data = new PaneTextEditData();
        controls = new ArrayList<>();
        controls.addAll(Arrays.asList(styles, status, id, linkText, agenda,
            escape, data));
        controls.addAll(Arrays.asList(formats));
        controls.addAll(Arrays.asList(links));
        controls.addAll(Arrays.asList(refSpans));

        layoutTopBar();
        layoutBottomBar();

        position = new SimpleIntegerProperty(this, "position");
        position.addListener((data, oldValue, newValue) -> listenPosition());
        document = new SimpleObjectProperty<>(this, "document");
    }

    /// Getters
    public ArrayList<EditNode> getControlNodes(){
        return controls;
    }

    public PaneTextEditStyle getStyles(){
        return styles;
    }

    /// Layout Node
    private void layoutTopBar(){
        add(new ToolBar(styles, new Separator(), id, status,
            new Separator(), links[0], links[1], linkText), 0, 0);
    }

    private void layoutBottomBar(){
        add(new ToolBar(formats[0], formats[1], formats[2], formats[3],
            new Separator(), refSpans[0], refSpans[1], refSpans[2],
            agenda, data, new Separator(), escape), 0, 1);
    }
    /// Node Properties
    public SimpleIntegerProperty positionProperty(){
        return position;
    }

    public int getPosition(){
        return position.getValue();
    }

    public void setPosition(int value){
        position.setValue(value);
    }

    public SimpleObjectProperty<ManuscriptDocument> doucmentProperty(){
        return document;
    }

    public ManuscriptDocument getDocument(){
        return document.getValue();
    }

    public void setDocument(ManuscriptDocument value){
        document.setValue(value);
    }

    /// Control Methods
    protected abstract void listenPosition();

    public abstract void refreshButtons();
}
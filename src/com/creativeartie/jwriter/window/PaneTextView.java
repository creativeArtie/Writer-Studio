package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.animation.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.property.window.*;

abstract class PaneTextView extends BorderPane {

    private final InlineCssTextArea textArea;
    private final Button viewMode;
    private final Label currentStats;
    private final Label currentTime;
    private final ReadOnlyObjectWrapper<PlainTextChange> edited;
    private ReadOnlyObjectWrapper<WindowText> modeProp;
    private final ReadOnlyIntegerWrapper position;
    private final ReadOnlyBooleanWrapper editorFocus;

    PaneTextView(){
        textArea = new InlineCssTextArea();
        viewMode = new Button();
        currentStats = new Label();
        currentTime = new Label();

        layoutCenterPane();
        layoutBottomPane();

        edited = new ReadOnlyObjectWrapper<>(this, "edited");
        textArea.plainTextChanges().subscribe(value -> edited.setValue(value));

        position = new ReadOnlyIntegerWrapper(this, "position");
        position.bind(textArea.caretPositionProperty());

        editorFocus = new ReadOnlyBooleanWrapper(this, "editorFocus");
        editorFocus.bind(textArea.focusedProperty());

        modeProp = new ReadOnlyObjectWrapper<>(this, "mode");
        viewMode.setOnAction(evt -> modeProp
            .setValue(setNextMode(modeProp.getValue())));
        modeProp.setValue(setNextMode(null));

    }

    /// Getters
    protected InlineCssTextArea getTextArea(){
        return textArea;
    }


    protected Button getViewModeButton(){
        return viewMode;
    }

    protected Label getCurrentStatsLabel(){
        return currentStats;
    }

    protected Label getCurrentTimeLabel(){
        return currentTime;
    }

    /// Layout Node
    private void layoutCenterPane(){
        setCenter(textArea);
        textArea.setWrapText(true);
    }

    private void layoutBottomPane(){
        BorderPane pane = new BorderPane();
        pane.setLeft(viewMode);
        pane.setCenter(currentStats);
        pane.setRight(currentTime);
        setBottom(pane);
    }

    /// Node Properties
    public ReadOnlyObjectProperty<PlainTextChange> editedProperty(){
        return edited.getReadOnlyProperty();
    }

    public PlainTextChange getEdited(){
        return edited.getValue();
    }

    public ReadOnlyIntegerProperty positionProperty(){
        return position.getReadOnlyProperty();
    }

    public int getPosition(){
        return position.getValue();
    }

    public ReadOnlyBooleanProperty editorFocusProperty(){
        return editorFocus.getReadOnlyProperty();
    }

    public boolean isEditorFocus(){
        return editorFocus.getValue();
    }

    public ReadOnlyObjectProperty<WindowText> viewModeProperty(){
        return modeProp.getReadOnlyProperty();
    }

    public WindowText getViewMode(){
        return modeProp.getValue();
    }

    /// Control Methods
    public abstract void loadDoc(ManuscriptDocument doc);

    public abstract void updateCss(ManuscriptDocument doc);

    public abstract void moveTo(int position);

    public abstract void moveTo(Span span);

    public abstract WindowText setNextMode(WindowText lastMode);

}
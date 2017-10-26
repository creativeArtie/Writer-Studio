package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.animation.*;

import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;

abstract class PaneTextView extends BorderPane {

    private final PaneTextEditControl buttons;
    private final InlineCssTextArea textArea;
    private final Label viewMode;
    private final Label currentStats;
    private final Label currentTime;
    private final ReadOnlyObjectWrapper<PlainTextChange> edited;
    private final ReadOnlyIntegerWrapper position;
    private final ReadOnlyBooleanWrapper editorFocus;

    PaneTextView(){

        buttons = new PaneTextEditControl();
        textArea = new InlineCssTextArea();
        viewMode = new Label();
        currentStats = new Label();
        currentTime = new Label();

        // layoutTopPane();
        layoutCenterPane();
        layoutBottomPane();

        edited = new ReadOnlyObjectWrapper<>(this, "edited");
        textArea.plainTextChanges().subscribe(value -> edited.setValue(value));

        position = new ReadOnlyIntegerWrapper(this, "position");
        position.bind(textArea.caretPositionProperty());
        buttons.positionProperty().bind(position);

        editorFocus = new ReadOnlyBooleanWrapper(this, "editorFocus");
        editorFocus.bind(textArea.focusedProperty());

    }

    /// Getters
    protected InlineCssTextArea getTextArea(){
        return textArea;
    }

    protected PaneTextEditControl getButtonPane(){
        return buttons;
    }

    protected Label getViewModeLabel(){
        return viewMode;
    }

    protected Label getCurrentStatsLabel(){
        return currentStats;
    }

    protected Label getCurrentTimeLabel(){
        return currentTime;
    }

    /// Layout Node
    private void layoutTopPane(){
        setTop(buttons);
    }

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

    /// Control Methods
    public abstract void loadDoc(ManuscriptDocument doc);

    public abstract void updateCss(ManuscriptDocument doc);

    public abstract void moveTo(int position);

    public abstract void moveTo(Span span);

}
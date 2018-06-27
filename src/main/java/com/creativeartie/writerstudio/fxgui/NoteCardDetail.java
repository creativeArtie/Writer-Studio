package com.creativeartie.writerstudio.fxgui;

import java.util.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.resource.*;

class NoteCardDetail extends TitledPane{
    /// %Part 1: Constructor and Class Fields

    private final Label footnoteLabel;
    private final Label inTextLabel;
    private final Label sourceLabel;

    private final Label noCardTitleLabel;
    private final Label noCardDetailLabel;

    private final Label noTitleTextLabel;
    private final Label noContentTextLabel;

    private final Button goToButton;

    private SimpleObjectProperty<NoteCardSpan> showNote;
    private ReadOnlyObjectWrapper<NoteCardSpan> goToNote;

    NoteCardDetail(){
        setCollapsible(false);

        sourceLabel = new Label(WindowText.NOTE_CARD_SOURCE.getText());
        footnoteLabel = new Label(WindowText.NOTE_CARD_FOOTNOTE.getText());
        inTextLabel = new Label(WindowText.NOTE_CARD_IN_TEXT.getText());

        noCardTitleLabel = new Label(WindowText.NOTE_CARD_PLACEHOLDER_TITLE
            .getText());
        noCardDetailLabel = new Label(WindowText.NOTE_CARD_PLACHOLDER_DETAIL
            .getText());

        noTitleTextLabel = new Label(WindowText.NOTE_CARD_EMPTY_TITLE.getText());
        noContentTextLabel = new Label(WindowText.NOTE_CARD_EMTPY_DETAIL
            .getText());

        goToButton = new Button(WindowText.NOTE_CARD_EDIT.getText());

        StyleClass.NOT_FOUND.addClass(noCardTitleLabel);
        StyleClass.NOT_FOUND.addClass(noCardDetailLabel);
        StyleClass.NO_TEXT.addClass(noTitleTextLabel);

        showNote = new SimpleObjectProperty<>(this, "showNote");
        goToNote = new ReadOnlyObjectWrapper<>(this, "goToNote");

        goToButton.setOnAction(evt -> goToNote.setValue(getShowNote()));
        showNote.addListener((d, o, n) -> updateNote(n));

        clearContent();
    }

    /// %Part 2: Layout

    /// %Part 3: Listener Methods

    private void updateNote(NoteCardSpan note){
        if (note == null) clearContent(); else showNote(note);
    }

    private void clearContent(){
        setGraphic(noCardTitleLabel);
        setContent(noCardDetailLabel);
    }

    private void showNote(NoteCardSpan span){
        assert span != null: "Null span";
        /// Add the title
        Optional<FormattedSpan> title = span.getTitle();
        if (title.isPresent()){
            setGraphic(TextFlowBuilder.loadFormatText(span.getTitle()));
        } else {
            setGraphic(noTitleTextLabel);
        }

        /// Add the content
        Node content;
        Collection<FormattedSpan> lines = span.getContent();
        if (lines.isEmpty()){
            content = noContentTextLabel;
        } else {
            TextFlow text = new TextFlow();
            for (FormattedSpan line: lines){
                TextFlowBuilder.loadFormatText(text, Optional.of(line));
                text.getChildren().add(new Text("\n"));
            }
            ScrollPane pane = new ScrollPane(text);
            pane.setFitToHeight(true);
            pane.setFitToWidth(true);
            content = pane;
        }

        /// Add citations
        GridPane bottom = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(30.0);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(70.0);
        bottom.getColumnConstraints().addAll(column1, column2);
        /// add source
        span.getSource().ifPresent(source -> {

            bottom.add(sourceLabel, 0, 0);
            bottom.add(new ScrollPane(TextFlowBuilder
                .loadFormatText(Optional.of(source))), 1, 0);
        });
        /// add footnote/in-text
        span.getInTextLine().ifPresent(line -> {
            Label label = null;
            Node text = null;
            if (line.getInfoFieldType() == InfoFieldType.FOOTNOTE){
                label = footnoteLabel;
                text = new Text(line.getData()
                    .filter(s -> s instanceof ContentSpan)
                    .map(s -> ((ContentSpan)s).getTrimmed())
                    .orElse("")
                );
            } else {
                label = inTextLabel;
                text = TextFlowBuilder.loadFormatText( line.getData()
                    .filter(s -> s instanceof FormattedSpan)
                    .map(s -> (FormattedSpan) s)
                );
            }
            if (text != null){
                assert label != null : "Null label, but filled text";
                bottom.add(label, 0, 1);
                bottom.add(new ScrollPane(text), 1, 1);
            }
        });

        bottom.add(new StackPane(new Label(span.getLookupText())), 0, 2);
        bottom.add(new StackPane(goToButton), 1, 2);

        BorderPane ans = new BorderPane();
        ans.setCenter(content);
        ans.setBottom(bottom);
        setContent(ans);
    }

    /// %Part 4: Properties

    public ObjectProperty<NoteCardSpan> showNoteProperty(){
        return showNote;
    }

    public NoteCardSpan getShowNote(){
        return showNote.getValue();
    }

    public void setShowNote(NoteCardSpan value){
        showNote.setValue(value);
        goToNote.setValue(null);
    }

    public ReadOnlyObjectProperty<NoteCardSpan> goToNoteProperty(){
        return goToNote.getReadOnlyProperty();
    }

    public NoteCardSpan getGoToNote(){
        return goToNote.getValue();
    }

    /// %Part 5: Get Child Methods
}

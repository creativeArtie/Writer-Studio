package com.creativeartie.writerstudio.javafx;

import java.util.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.resource.*;

class NoteCardDetailControl extends NoteCardDetailView{
    private ObjectProperty<SpanBranch> lastSelected;
    private BooleanProperty refocusText;

    private final Label footnoteLabel;
    private final Label inTextLabel;
    private final Label sourceLabel;

    private final Label noCardTitleLabel;
    private final Label noCardDetailLabel;

    private final Label noTitleTextLabel;
    private final Label noContentTextLabel;

    private final Button goToButton;

    NoteCardDetailControl(){
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
    }

    @Override
    protected void setupChildern(WriterSceneControl control){
        lastSelected = control.lastSelectedProperty();
        refocusText = control.refocusTextProperty();

        goToButton.setOnAction(evt -> goToNote());

        showNoteProperty().addListener((d, o, n) -> updateNote(n));

        clearContent();
    }

    private void goToNote(){
        lastSelected.setValue(getShowNote());
        refocusText.setValue(true);
    }

    private void updateNote(NoteCardSpan show){
        if (show == null){
            clearContent();
        } else {
            showNote(show);
        }
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
}

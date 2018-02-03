package com.creativeartie.jwriter.window;

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.scene.control.cell.*;

import java.util.*;
import java.util.function.*;

import com.creativeartie.jwriter.lang.*;
import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.resource.*;

/**
 * Methods that are common in {@linkplain TableView}.
 */
final class TableViewHelper{

    private static class IdCell<T> extends TableCell<T,
            Optional<CatalogueIdentity>>{
        @Override
        protected void updateItem(Optional<CatalogueIdentity> item, boolean empty) {
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null){
                setText(null);
                setGraphic(null);
                StyleClass.NOT_FOUND.removeClass(this);
            } else {
                String text;
                if (item.isPresent()){
                    text = item.get().getFullIdentity();
                    StyleClass.NOT_FOUND.removeClass(this);
                } else {
                    text = WindowText.NO_ID.getText();
                    StyleClass.NOT_FOUND.addClass(this);
                }
                setText(text);
                setGraphic(null);
            }
        }
    }

    public static <T> TableColumn<T, Optional<CatalogueIdentity>> getIdColumn(
            WindowText title, Function<T,
                ObservableObjectValue<Optional<CatalogueIdentity>>
            > property){
        TableColumn<T, Optional<CatalogueIdentity>> ans = new TableColumn<>(
            title.getText());
        ans.setCellFactory(list -> new TableViewHelper.IdCell<>());
        ans.setCellValueFactory(c -> new SimpleObjectProperty<>(

            property.apply(c.getValue()).getValue()
        ));
        return ans;
    }

    private static class NumberCell<T> extends TableCell<T, Number>{
        @Override
        protected void updateItem(Number item, boolean empty) {
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null){
                setText(null);
                setGraphic(null);
            } else {
                setText(item.toString());
                StyleClass.NUMBER_COLUMN.addClass(this);
                setGraphic(null);
            }
        }
    }

    public static <T> TableColumn<T, Number> getNumberColumn(WindowText title,
            Function<T, ObservableNumberValue> property){
        TableColumn<T, Number> ans = new TableColumn<>(title.getText());
        ans.setCellFactory(list -> new TableViewHelper.NumberCell<>());
        ans.setCellValueFactory(c -> new SimpleIntegerProperty(
            /// 1st getValue() = T data; 2nd getValue() = Number
            property.apply(c.getValue()).intValue()
        ));
        return ans;
    }

    public static <T> TableColumn<T, Boolean> getBooleanColumn(WindowText title,
            Function<T, ObservableBooleanValue> property){
        TableColumn<T, Boolean> ans = new TableColumn<>(title.getText());
        ans.setCellFactory(list -> new CheckBoxTableCell<>());
        ans.setCellValueFactory(c -> new SimpleBooleanProperty(
            /// 1st getValue() = T data; 2nd getValue() = Boolean
            property.apply(c.getValue()).getValue()
        ));
        return ans;
    }

    private static class SectionCell<T> extends TableCell<T, SectionSpan>{
        @Override
        protected void updateItem(SectionSpan item, boolean empty) {
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null){
                setText(null);
                setGraphic(null);
            } else {
                /// Allows WindowSpanParser to create the Label
                TextFlow graphic = TextFlowBuilder.loadHeadingLine(item
                    .getHeading());
                setText(null);
                setGraphic(graphic);
            }
        }
    }

    public static <T> TableColumn<T, SectionSpan> getSectionColumn(
            WindowText title,
            Function<T, ObservableObjectValue<SectionSpan>> property){
        TableColumn<T, SectionSpan> ans = new TableColumn<>(title.getText());
        ans.setCellFactory(list -> new TableViewHelper.SectionCell<>());
        ans.setCellValueFactory(c -> new SimpleObjectProperty<>(
            /// 1st getValue() = T data; 2nd getValue() = SectionSpan
            property.apply(c.getValue()).getValue()
        ));
        return ans;
    }

    /** TableCell for strings */
    private static class FormatCell<T> extends TableCell<T, 
            Optional<FormatSpanMain>> {

        @Override
        public void updateItem(Optional<FormatSpanMain> item, boolean empty){
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                Node graphic = null;
                graphic = TextFlowBuilder.loadFormatText(item);

                /// Completing the setting
                setText(null);
                setGraphic(graphic);
            }
        }
    }

    public static <T> TableColumn<T, Optional<FormatSpanMain>> getFormatColumn(
            WindowText title,
            Function<T, ObservableObjectValue<Optional<FormatSpanMain>>> 
                property){
        TableColumn<T, Optional<FormatSpanMain>> ans = new TableColumn<>(title
            .getText());
        ans.setCellFactory(list -> new TableViewHelper.FormatCell<>());
        ans.setCellValueFactory(c -> new SimpleObjectProperty<>(
            /// 1st getValue() = T data; 2nd getValue() = Text
            property.apply(c.getValue()).getValue()
        ));
        return ans;
    }

    /** TableCell for strings */
    private static class LinkCell<T> extends TableCell<T, Object> {

        @Override
        public void updateItem(Object item, boolean empty){
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                Node graphic = null;
                if (item instanceof String){
                    graphic = new Hyperlink((String)item);
                } else {
                    assert item instanceof LinedSpanLevelSection;
                    graphic = TextFlowBuilder.loadHeadingLine(Optional.of(
                        (LinedSpanLevelSection)item));
                }

                /// Completing the setting
                setText(null);
                setGraphic(graphic);
            }
        }
    }

    public static <T> TableColumn<T, Object> getLinkColumn(WindowText title,
            Function<T, ObservableObjectValue<Object>> property){
        TableColumn<T, Object> ans = new TableColumn<>(title.getText());
        ans.setCellFactory(list -> new TableViewHelper.LinkCell<>());
        ans.setCellValueFactory(c -> new SimpleObjectProperty<>(
            /// 1st getValue() = T data; 2nd getValue() = Text
            property.apply(c.getValue()).getValue()
        ));
        return ans;
    }

    /** TableCell for strings */
    private static class TextCell<T> extends TableCell<T, String> {
        private WindowText emptyText;

        private TextCell(WindowText empty){
            emptyText = empty;
        }

        @Override
        public void updateItem(String item, boolean empty){
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                Text graphic = null;
                if (item.isEmpty()){
                    /// There is no text found.
                    graphic = new Text(emptyText.getText());
                    StyleClass.NO_TEXT.addClass(graphic);
                } else {
                    /// Add the text that is found.
                    graphic = new Text(item);
                }

                /// Completing the setting
                setText(null);
                setGraphic(graphic);
            }
        }
    }

    public static <T> TableColumn<T, String> getTextColumn(WindowText title,
            Function<T, ObservableStringValue> property, WindowText empty){
        TableColumn<T, String> ans = new TableColumn<>(title.getText());
        ans.setCellFactory(list -> new TableViewHelper.TextCell<>(empty));
        ans.setCellValueFactory(c -> new SimpleStringProperty(
            /// 1st getValue() = T data; 2nd getValue() = Text
            property.apply(c.getValue()).getValue()
        ));
        return ans;
    }

    public static void styleTableView(TableView<?> table, WindowText empty){
        table.setFixedCellSize(30);
        table.setPlaceholder(new Label(empty.getText()));
    }


    public static void setPrecentWidth(TableColumn<?, ?> column,
            TableView<?> parent, double precent){
        column.prefWidthProperty().bind(parent.widthProperty()
            .multiply(precent / 100));
    }
}
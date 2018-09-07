package com.creativeartie.writerstudio.javafx.utils;

import java.util.*;
import java.util.function.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import com.creativeartie.writerstudio.lang.*;
import com.creativeartie.writerstudio.lang.markup.*;
import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    UtilitiesConstants.*;

/**
 * Methods that are common in {@linkplain TableView}.
 */
public final class TableCellFactory{

    private static class IdCell<T> extends TableCell<T,
            Optional<CatalogueIdentity>>{
        @Override
        protected void updateItem(Optional<CatalogueIdentity> item,
                boolean empty) {
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null){
                setText(null);
                setGraphic(null);
                getStyleClass().remove(NOT_FOUND_STYLE);
            } else {
                String text;
                if (item.isPresent()){
                    text = item.get().getFullIdentity();
                    getStyleClass().remove(NOT_FOUND_STYLE);
                } else {
                    text = NO_ID;
                    getStyleClass().remove(NOT_FOUND_STYLE);
                }
                setText(text);
                setGraphic(null);
            }
        }
    }

    public static <T> TableColumn<T, Optional<CatalogueIdentity>> getIdColumn(
            String title, Function<T,
                ObservableObjectValue<Optional<CatalogueIdentity>>
            > property){
        TableColumn<T, Optional<CatalogueIdentity>> ans = new TableColumn<>(
            title);
        ans.setCellFactory(list -> new IdCell<>());
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
                getStyleClass().add(NUMBER_COLUMN_STYLE);
                setGraphic(null);
            }
        }
    }

    public static <T> TableColumn<T, Number> getNumberColumn(String title,
            Function<T, ObservableNumberValue> property){
        TableColumn<T, Number> ans = new TableColumn<>(title);
        ans.setCellFactory(list -> new NumberCell<>());
        ans.setCellValueFactory(c -> new SimpleIntegerProperty(
            /// 1st getValue() = T data; 2nd getValue() = Number
            property.apply(c.getValue()).intValue()
        ));
        return ans;
    }

    public static <T> TableColumn<T, Boolean> getBooleanColumn(String title,
            Function<T, ObservableBooleanValue> property){
        TableColumn<T, Boolean> ans = new TableColumn<>(title);
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
            String title,
            Function<T, ObservableObjectValue<SectionSpan>> property){
        TableColumn<T, SectionSpan> ans = new TableColumn<>(title);
        ans.setCellFactory(list -> new SectionCell<>());
        ans.setCellValueFactory(c -> new SimpleObjectProperty<>(
            /// 1st getValue() = T data; 2nd getValue() = SectionSpan
            property.apply(c.getValue()).getValue()
        ));
        return ans;
    }

    /** TableCell for strings */
    private static class FormatCell<T> extends TableCell<T,
            Optional<FormattedSpan>> {

        @Override
        public void updateItem(Optional<FormattedSpan> item, boolean empty){
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

    public static <T> TableColumn<T, Optional<FormattedSpan>> getFormatColumn(
            String title,
            Function<T, ObservableObjectValue<Optional<FormattedSpan>>>
                property){
        TableColumn<T, Optional<FormattedSpan>> ans = new TableColumn<>(title);
        ans.setCellFactory(list -> new FormatCell<>());
        ans.setCellValueFactory(c -> new SimpleObjectProperty<>(
            /// 1st getValue() = T data; 2nd getValue() = Text
            property.apply(c.getValue()).getValue()
        ));
        return ans;
    }

    /** TableCell for strings */
    private static class FieldTypeCell<T> extends TableCell<T, Object> {

        @Override
        public void updateItem(Object item, boolean empty){
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                /// Completing the setting
                String text;
                if (item instanceof InfoFieldType) {
                    text = getFieldTypeText((InfoFieldType)item);
                    getStyleClass().remove(UNKNONW_FIELD_STYLE);
                } else {
                    text = (String) item;
                    getStyleClass().add(UNKNONW_FIELD_STYLE);
                }
                setText(text);
                setGraphic(null);
            }
        }
    }

    public static <T> TableColumn<T, Object> getFieldTypeColumn(
        String title,
            Function<T, ObservableObjectValue<Object>> property){
        TableColumn<T, Object> ans = new TableColumn<>(title);
        ans.setCellFactory(list -> new FieldTypeCell<>());
        ans.setCellValueFactory(c -> new SimpleObjectProperty<>(
            /// 1st getValue() = T data; 2nd getValue() = Text
            property.apply(c.getValue()).getValue()
        ));
        return ans;
    }

    /** TableCell for strings */
    private static class MetaDataCell<T> extends TableCell<T,
        Optional<SpanBranch>>
    {

        @Override
        public void updateItem(Optional<SpanBranch> item, boolean empty){
            /// Required by JavaFX API:
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                Optional<FormattedSpan> text;
                if (item.isPresent()){
                    SpanBranch span = item.get();
                    if (span instanceof FormattedSpan){
                        text = item.map(s -> (FormattedSpan)s);
                    } else if (span instanceof DirectorySpan){
                        /// finds a reference
                        text = item.map(s -> (DirectorySpan) s)
                            /// s == DirectorySpan
                            .map(s -> s.buildId())
                            /// i == CatalogueIdentity
                            .map(i -> span.getDocument().getCatalogue().get(i))
                            /// d == CatalogueData
                            .filter(d -> d.isReady())
                            .map(d -> d.getTarget())
                            /// s == SpanBranch
                            .filter(s -> s instanceof NoteCardSpan)
                            .map(s -> (NoteCardSpan) s)
                            /// s == NoteCardSpan
                            .flatMap(s -> s.getSource());
                    } else {
                        setText(span.getRaw());
                        setGraphic(null);
                        return;
                    }
                } else {
                    text = Optional.empty();
                }
                TextFlow graphic = TextFlowBuilder.loadFormatText(text);
                setText(null);
                setGraphic(graphic);
            }
        }
    }

    public static <T> TableColumn<T, Optional<SpanBranch>> getMetaDataColumn(
        String title,
        Function<T, ObservableObjectValue<Optional<SpanBranch>>> property)
    {
        TableColumn<T, Optional<SpanBranch>> ans = new TableColumn<>(title);
        ans.setCellFactory(list -> new MetaDataCell<>());
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

    public static <T> TableColumn<T, Object> getLinkColumn(String title,
            Function<T, ObservableObjectValue<Object>> property){
        TableColumn<T, Object> ans = new TableColumn<>(title);
        ans.setCellFactory(list -> new LinkCell<>());
        ans.setCellValueFactory(c -> new SimpleObjectProperty<>(
            /// 1st getValue() = T data; 2nd getValue() = Text
            property.apply(c.getValue()).getValue()
        ));
        return ans;
    }

    /** TableCell for strings */
    private static class TextCell<T> extends TableCell<T, String> {
        private String emptyText;

        private TextCell(String empty){
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
                    graphic = new Text(emptyText);
                    getStyleClass().add(NO_TEXT_STYLE);
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

    public static <T> TableColumn<T, String> getTextColumn(String title,
            Function<T, ObservableStringValue> property, String empty){
        TableColumn<T, String> ans = new TableColumn<>(title);
        ans.setCellFactory(list -> new TextCell<>(empty));
        ans.setCellValueFactory(c -> new SimpleStringProperty(
            /// 1st getValue() = T data; 2nd getValue() = Text
            property.apply(c.getValue()).getValue()
        ));
        return ans;
    }

    public static void styleTableView(TableView<?> table, String empty){
        table.setFixedCellSize(30);
        table.setPlaceholder(new Label(empty));
    }


    public static void setPrecentWidth(TableColumn<?, ?> column,
            TableView<?> parent, double precent){
        column.prefWidthProperty().bind(parent.widthProperty()
            .multiply(precent / 100));
    }

    private TableCellFactory(){}
}

package com.creativeartie.jwriter.window;

import java.util.*;
import java.io.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.control.*;

import static com.google.common.base.CaseFormat.*;
import static com.google.common.base.Preconditions.*;

import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.lang.Span;
import com.creativeartie.jwriter.main.*;
import com.creativeartie.jwriter.property.*;
public final class SpanBranchParser {
    private static PropertyManager styleManager;

    private static PropertyManager getManager(){
        if (styleManager == null){
            try {
                styleManager = new PropertyManager("data/parse-styles",
                    "data/user-styles");
            } catch (IOException ex){
                throw new RuntimeException(ex);
            }
        }
        return styleManager;
    }

    private static class StyleBuilder{
        private List<StyleProperty> properties;

        StyleBuilder(){
            properties = new ArrayList<StyleProperty>();
        }

        StyleBuilder addKey(String base, String name){
            return addKey(base + "." + UPPER_UNDERSCORE.to(UPPER_CAMEL, name));
        }

        StyleBuilder addKey(String key){
            properties.add(getManager().getStyleProperty(key));
            return this;
        }

        @Override
        public String toString(){
            return StyleProperty.toCss(properties);
        }
    }

    public static StyleProperty getNotFoundProperty(){
        return getManager().getStyleProperty("Other.NotFound");
    }

    public static String getNotFoundStyle(){
        return getNotFoundProperty().toCss();
    }

    public static TextFlow parseDisplay(FormatSpanMain main, String ... style){
        TextFlow content = new TextFlow();
        parseDisplay(content, main, style);
        return content;

    }

    public static void parseDisplay(TextFlow content, FormatSpanMain main,
            String ... styles){
        checkNotNull(content, "Content cannot be null.");
        if (main != null){
            parsingDisplay(content, main, styles);
        }
    }

    private static void parsingDisplay(TextFlow content, FormatSpanMain main,
            String ... styles){
        for (Span span: main){
            StyleBuilder css = new StyleBuilder();
            Node input = null;
            if (span instanceof FormatSpanDirectory){
                input = ((FormatSpanDirectory)span).getSpanIdentity().map(
                        f -> f.getFullIdentity()
                    ).map(id -> new Text(id))
                    .orElse(null);
                css.addKey("Display.Directory");
            } else if (span instanceof FormatSpanContent){
                input = new Text(((FormatSpanContent)span).getText());
            } else if (span instanceof FormatSpanAgenda){
                input = new Text(((FormatSpanAgenda)span).getAgenda());
                css.addKey("Display.Agenda");
            } else if (span instanceof FormatSpanLink){
                FormatSpanLink format = (FormatSpanLink) span;
                Hyperlink link = new Hyperlink(format.getText());
                link.setTooltip(new Tooltip(format.getPath()));
                input = link;
            }
            if (span instanceof FormatSpan) {
                FormatSpan add = (FormatSpan) span;
                for (FormatType type: add.listFormats()){
                    css.addKey("Display", type.name());
                }
            }
            if (input != null){
                for (String key: styles){
                    css.addKey(key);
                }
                input.setStyle(css.toString());
                content.getChildren().add(input);
            }
        }
    }

    public static TextFlow parseDisplay(LinedSpanSection line,
            String ... styles){
        TextFlow content = new TextFlow();
        parseDisplay(content, line, styles);
        return content;
    }
    public static void parseDisplay(TextFlow content, LinedSpanSection line,
            String ... styles){
        checkNotNull(content, "Content cannot be null.");
        if (line != null){
            parsingDisplay(content, line, styles);
        } else {
            Text empty = new Text(SpanText.HEADING_PLACEHOLDER.getText());
            empty.setStyle(getNotFoundStyle());
            content.getChildren().add(empty);
        }
    }

    private static void parsingDisplay(TextFlow content, LinedSpanSection line,
            String ... styles){
        Text status = new Text(SpanText.getText(line.getEdition()));
        status.setStyle(getManager().getStyleProperty("Display.HeadingStatus")
            .toCss());
        content.getChildren().add(status);
        Optional<FormatSpanMain> title = line.getFormattedSpan();
        if (title.isPresent()){
            parseDisplay(content, line.getFormattedSpan().get(), styles);
        } else {
            Text empty = new Text(SpanText.HEADING_NO_TEXT.getText());
            empty.setStyle(getNotFoundStyle());
            content.getChildren().add(empty);
        }
    }

    private SpanBranchParser(){}
}
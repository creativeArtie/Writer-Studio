package com.creativeartie.jwriter.property.window;

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
public final class WindowSpanParser {

    public static TextFlow parseDisplay(FormatSpanMain main,
            WindowStyle ... style){
        TextFlow content = new TextFlow();
        parseDisplay(content, main, style);
        return content;

    }

    public static void parseDisplay(TextFlow content, FormatSpanMain main,
            WindowStyle ... styles){
        checkNotNull(content, "Content cannot be null.");
        if (main != null){
            parsingDisplay(content, main, styles);
        }
    }

    private static void parsingDisplay(TextFlow content, FormatSpanMain main,
            WindowStyle ... styles){
        for (Span span: main){
            WindowStyleBuilder css = new WindowStyleBuilder();
            Node input = null;
            if (span instanceof FormatSpanDirectory){
                input = ((FormatSpanDirectory)span).getSpanIdentity().map(
                        f -> f.getFullIdentity()
                    ).map(id -> new Text(id))
                    .orElse(null);
                css.add("Display.Directory");
            } else if (span instanceof FormatSpanContent){
                input = new Text(((FormatSpanContent)span).getText());
            } else if (span instanceof FormatSpanAgenda){
                input = new Text(((FormatSpanAgenda)span).getAgenda());
                css.add("Display.Agenda");
            } else if (span instanceof FormatSpanLink){
                FormatSpanLink format = (FormatSpanLink) span;
                Hyperlink link = new Hyperlink(format.getText());
                link.setTooltip(new Tooltip(format.getPath()));
                input = link;
            }
            if (span instanceof FormatSpan) {
                FormatSpan add = (FormatSpan) span;
                for (FormatType type: add.listFormats()){
                    css.add("Display", type.name());
                }
            }
            if (input != null){
                for (WindowStyle style: styles){
                    css.add(style);
                }
                input.setStyle(css.toString());
                content.getChildren().add(input);
            }
        }
    }

    public static TextFlow parseDisplay(LinedSpanSection line,
            WindowStyle ... styles){
        TextFlow content = new TextFlow();
        parseDisplay(content, line, styles);
        return content;
    }
    public static void parseDisplay(TextFlow content, LinedSpanSection line,
            WindowStyle ... styles){
        checkNotNull(content, "Content cannot be null.");
        if (line != null){
            parsingDisplay(content, line, styles);
        } else {
            Text empty = new Text(WindowText.HEADING_PLACEHOLDER.getText());
            empty.setStyle(WindowStyle.NOT_FOUND.toCss());
            content.getChildren().add(empty);
        }
    }

    private static void parsingDisplay(TextFlow content, LinedSpanSection line,
            WindowStyle ... styles){
        Text status = new Text(WindowText.getText(line.getEdition()));
        status.setStyle(WindowStyle.getStyle("Display.HeadingStatus")
            .toCss());
        content.getChildren().add(status);
        Optional<FormatSpanMain> title = line.getFormattedSpan();
        if (title.isPresent()){
            parseDisplay(content, line.getFormattedSpan().get(), styles);
        } else {
            Text empty = new Text(WindowText.HEADING_NO_TEXT.getText());
            empty.setStyle(WindowStyle.NOT_FOUND.toCss());
            content.getChildren().add(empty);
        }
    }

    private WindowSpanParser(){}
}
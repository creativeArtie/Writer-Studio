package com.creativeartie.writerstudio.javafx.utils;

import java.util.*;
import javafx.scene.*;
import javafx.scene.text.*;

import com.creativeartie.writerstudio.lang.markup.*;
import com.creativeartie.writerstudio.lang.*;
import static com.creativeartie.writerstudio.javafx.utils.LayoutConstants.
    UtilitiesConstants.*;

public final class TextFlowBuilder {

    public static TextFlow loadHeadingLine(
            Optional<LinedSpanLevelSection> span){
        TextFlow ans = new TextFlow();
        loadHeadingLine(ans, span);
        return ans;
    }

    public static void loadHeadingLine(TextFlow node,
            Optional<LinedSpanLevelSection> span){
        if (span.isPresent()){
            LinedSpanLevelSection heading = span.get();
            loadFormatText(node, heading.getFormattedSpan());
            EditionType type = heading.getEditionType();
            if (type != EditionType.NONE){
                newText("(" + type + ")", "display-edition").ifPresent(
                    t -> node.getChildren().add(t)
                );
            }

        } else {
            Text empty = new Text(EMPTY_TEXT);
            empty.getStyleClass().add(NOT_FOUND_STYLE);
            node.getChildren().add(empty);
        }
    }

    public static TextFlow loadFormatText(Optional<FormattedSpan> span){
        TextFlow ans = new TextFlow();
        loadFormatText(ans, span);
        return ans;
    }

    public static void loadFormatText(TextFlow node,
            Optional<FormattedSpan> span){
        span.ifPresent(format -> {
            for (Span child: format){
                createFormatSpan(child).ifPresent(found ->
                    node.getChildren().add(found));
            }
        });
    }

    public static TextFlow loadMetaText(TextSpanMatter print){
        TextFlow node = new TextFlow();
        String align;
        switch(print.getDataType()){
        case RIGHT:
            node.getStyleClass().add("display-right");
            break;
        case CENTER:
            node.getStyleClass().add("display-center");
            break;
        default: // LEFT, TEXT
            node.getStyleClass().add("display-left");
        }
        Optional<FormattedSpan> span = print.getData();
        if (span.isPresent()){
            loadFormatText(node, span);
        } else {
            node.getChildren().add(new Text(" "));
        }
        return node;
    }

    private static Optional<Text> createFormatSpan(Span child){
        Optional<Text> ans = Optional.empty();
        if (child instanceof FormatSpanAgenda){
            return newText(((FormatSpanAgenda)child).getAgenda(),
                "display-agenda");

        } else if (child instanceof FormatSpanContent){
            FormatSpanContent content = (FormatSpanContent) child;
            String text = content.getTrimmed();
            if (content.isSpaceBegin()){
                text = " " + text;
            }
            if (content.isSpaceEnd()){
                text = text + " ";
            }
            ans = newText(text);

        } else if (child instanceof FormatSpanPointId){
            FormatSpanPointId span = (FormatSpanPointId) child;
            String found = span.getSpanIdentity()
                .map(id -> id.getFullIdentity()).orElse("");
            DirectoryType type = span.getIdType();
            switch (type){
                case RESEARCH:
                    ans = newText(found, "display-note");
                    break;
                case FOOTNOTE:
                    ans = newText(found, "display-footnote");
                    break;
                case ENDNOTE:
                    ans = newText(found, "display-endnote");
                    break;
                default:
                    assert false: "Span with incorrect Directory Type:" + type;
            }
        } else if (child instanceof FormatSpanPointKey){
            String found = ((FormatSpanPointKey)child).getField().getFieldKey();
            ans = newText(found, "display-reference");
        } else if (child instanceof FormatSpanLink){
            FormatSpanLink span = (FormatSpanLink) child;
            ans = newText(span.getText(), "display-link");

        } else {
            return Optional.empty();
        }

        FormatSpan format = (FormatSpan) child;
        if (format.isBold()){
            addStyle(ans, "display-bold");
        }
        if (format.isItalics()){
            addStyle(ans, "display-italics");
        }
        if (format.isUnderline()){
            addStyle(ans, "display-underline");
        }
        if (format.isCoded()){
            addStyle(ans, "display-coded");
        }
        return ans;
    }

    private static Optional<Text> newText(String text){
        if (text.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(new Text(text));
    }

    private static Optional<Text> newText(String text, String style){
        Optional<Text> ans = newText(text);
        addStyle(ans, style);
        return ans;
    }

    private static Optional<Text> addStyle(Optional<Text> text, String style){
        text.ifPresent(node -> node.getStyleClass().add(style));
        return text;
    }
}

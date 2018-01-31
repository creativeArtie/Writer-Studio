package com.creativeartie.jwriter.property.window;


import java.util.*;
import javafx.scene.*;
import javafx.scene.text.*;

import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.lang.*;

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
            heading.getEditionSpan().flatMap(child ->
                newText("(" + child.getEdition() + ")", "display-edition")
            ).ifPresent(text -> node.getChildren().add(text));

        } else {
            Text empty = new Text(WindowText.HEADING_NO_TEXT.getText());
            empty.setStyle(WindowStyle.NOT_FOUND.toCss());
            node.getChildren().add(empty);
        }
    }

    public static TextFlow loadFormatText(Optional<FormatSpanMain> span){
        TextFlow ans = new TextFlow();
        loadFormatText(ans, span);
        return ans;
    }

    public static void loadFormatText(TextFlow node,
            Optional<FormatSpanMain> span){
        span.ifPresent(format -> {
            for (Span child: format){
                createFormatSpan(child).ifPresent(found ->
                    node.getChildren().add(found));
            }
        });
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

        } else if (child instanceof FormatSpanDirectory){
            FormatSpanDirectory span = (FormatSpanDirectory) child;
            String found = span.getSpanIdentity()
                .map(id -> id.getFullIdentity()).orElse("");
            DirectoryType type = span.getIdType();
            switch (type){
                case NOTE:
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

        } else if (child instanceof FormatSpanLink){
            FormatSpanLink span = (FormatSpanLink) child;
            ans = newText(span.getText(), "display-link");

        } else {
            return Optional.empty();
        }

        FormatSpan format = (FormatSpan) child;
        if (format.isBold()){
            addStyle(ans, "display-bold");
        } else if (format.isItalics()){
            addStyle(ans, "display-italics");
        } else if (format.isUnderline()){
            addStyle(ans, "display-underline");
        } else if (format.isCoded()){
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
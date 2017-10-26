package com.creativeartie.jwriter.window;

import java.util.Optional;
import javafx.scene.text.*;

import com.google.common.base.*;

import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.lang.Span;
import com.creativeartie.jwriter.main.*;
public class ParseTextUtilities {
    static String toCss(String base){
        return Utilities.getCss("Format." +
            CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, base));
    }

    static TextFlow setFormat(FormatSpanMain formatted){
        return setFormat(Optional.ofNullable(formatted));
    }

    static TextFlow setFormat(Optional<FormatSpanMain> formatted){
        TextFlow ans = new TextFlow();
        setFormat(ans, formatted);
        return ans;
    }

    static void setFormat(TextFlow node, FormatSpanMain formatted){
        setFormat(node, Optional.ofNullable(formatted));
    }
    static void setFormat(TextFlow node, Optional<FormatSpanMain> formatted){
        if (formatted.isPresent()){
            formatted.get().forEach((span) -> {
                String css = "";
                if (span instanceof FormatSpanDirectory){
                    css += toCss("Directory");
                }
                if (span instanceof FormatSpan) {
                    FormatSpan add = (FormatSpan) span;
                    Text input = new Text(add.getOutput());
                    for (FormatType type: add.listFormats()){
                        css += toCss(type.name());
                    }
                    input.setStyle(css);
                    node.getChildren().add(input);
                }
            });
        }
    }

    static TextFlow setHeading(Optional<LinedSpanSection> line){
        TextFlow ans = new TextFlow();
        setHeading(ans, line);
        return ans;
    }

    static void setHeading(TextFlow node, Optional<LinedSpanSection> line){
        if (line.isPresent()){
            String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,
                line.get().getEdition().name());
            Text status = new Text(Utilities.getString("SectionStatus." + name));
            status.setStyle(toCss("Status"));
            node.getChildren().add(status);
            setFormat(node, line.get().getFormattedSpan());
        } else {
            Text empty = new Text(Utilities.getString("HeadingView.NoFound"));
            empty.setStyle(Utilities.getCss("Common.NoneFound"));
            node.getChildren().add(empty);
        }
    }

    private ParseTextUtilities(){}
}
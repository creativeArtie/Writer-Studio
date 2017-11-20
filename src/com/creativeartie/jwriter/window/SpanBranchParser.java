package com.creativeartie.jwriter.window;

import java.util.Optional;
import javafx.scene.text.*;

import static com.google.common.base.CaseFormat.*;

import com.creativeartie.jwriter.lang.markup.*;
import com.creativeartie.jwriter.lang.Span;
import com.creativeartie.jwriter.main.*;
public final class SpanBranchParser {

    private static String toCss(String base, String name){
        return toCss(base + UPPER_UNDERSCORE.to(UPPER_CAMEL, name));
    }
    private static String toCss(String key){
        return Utilities.getCss(key);
    }

    public static TextFlow parseFormat(FormatSpanMain main){
        TextFlow content = new TextFlow();
        parseFormat(content, main, null);
        return content;
    }

    public static void parseFormat(TextFlow content, FormatSpanMain main,
            String style){
        if (content != null){
            for (Span span: main){
                String css = "";
                Text input = null;
                if (span instanceof FormatSpanDirectory){
                    input = parseSpan((FormatSpanDirectory) span);
                    css += toCss("Format.Directory");
                } else if (span instanceof FormatSpanContent){
                    input = new Text(((FormatSpanContent)span).getText());
                } /*else if (span instanceof FormatSpanAgenda){
                    input = new Text((FormatSpanAgenda);
                }*/
                if (span instanceof FormatSpan) {
                    FormatSpan add = (FormatSpan) span;
                    for (FormatType type: add.listFormats()){
                        css += toCss(type.name());
                    }
                }
                if (input != null){
                    if (style != null){
                        css += toCss(style);
                    }
                    input.setStyle(css);
                    content.getChildren().add(input);
                }
            }
        }
    }

    private static Text parseSpan(FormatSpanDirectory directory){
        return directory.getSpanIdentity().map(span -> span.getFullIdentity())
            .map(id -> new Text(id)).orElse(null);
    }

    private SpanBranchParser(){}
}
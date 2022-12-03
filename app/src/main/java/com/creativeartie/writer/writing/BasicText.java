package com.creativeartie.writer.writing;

import java.util.*;
import java.util.regex.*;

public class BasicText extends Span implements SpanBranch {

    private static String phraseName = "text";
    private static String ESCAPE = "\\\\.";

    private String rawPattern;
    private String namedPattern;
    private Pattern compilePattern;
    private ArrayList<Span> spanChildren;

    class AtomicText extends Span {

        private String simpleString;

        AtomicText(String text, DocBuilder docBuilder) {
            super(docBuilder);
            simpleString = text;
        }

        public String getText() {
            return simpleString;
        }
    }

    class EscapeText extends Span {

        private char escapedChar;

        EscapeText(char text, DocBuilder docBuilder) {
            super(docBuilder);
            escapedChar = text;
        }

        public String getText() {
            return escapedChar + "";
        }
    }

    private String outputText;

    public BasicText(
        String text, DocBuilder docBuilder, String forChars, boolean isAllowed,
        SpanStyles... inherited
    ) {
        super(docBuilder);
        setInheritedStyles(inherited);

        String pattern =
            isAllowed ? "[" + forChars + "]+" : "[^" + forChars + "\\\\]+";

        final String escapeName = "esc", textName = "txt";
        rawPattern = "(" + pattern + "|" + ESCAPE + ")+";
        namedPattern = namePattern(phraseName, rawPattern);
        compilePattern = Pattern.compile(
            "(" + namePattern(textName, pattern) + "|" +
                namePattern(escapeName, ESCAPE) + ")"
        );

        Matcher matched = compilePattern.matcher(text);
        spanChildren = new ArrayList<>();

        while (matched.find()) {
            String value;
            if ((value = matched.group(escapeName)) != null) {
                EscapeText child = new EscapeText(value.charAt(1), docBuilder);
                spanChildren.add(child);
                outputText += child.getText();
                this.addStyle(matched, escapeName, SpanStyles.ESCAPE);
                continue;
            }
            if ((value = matched.group(textName)) != null) {
                AtomicText child = new AtomicText(value, docBuilder);
                spanChildren.add(child);
                outputText += child.getText();
                addStyle(matched, textName, SpanStyles.TEXT);
            }

        }
    }

    @Override
    public Iterator<Span> iterator() {
        return spanChildren.iterator();
    }

    @Override
    public int size() {
        return spanChildren.size();
    }

}

package com.creativeartie.jwriter.lang.markup;

import java.util.*;

import static com.creativeartie.jwriter.main.Checker.*;
import static com.creativeartie.jwriter.lang.markup.AuxiliaryData.*;
import static com.creativeartie.jwriter.lang.SetupParser.*;
import com.creativeartie.jwriter.lang.*;

/**
 * All methods meant to check if a string can be reparsed locally in a single
 * {@link SpanBranch}.
 */
public final class AuxiliaryChecker{

    static boolean checkSectionEnd(boolean isLast, String text){
        checkNotNull(text, "text");
        if (text.endsWith(CHAR_ESCAPE)){
            return false;
        }
        if (text.endsWith(CHAR_ESCAPE + LINED_END)){
            return isLast;
        }

        if (! (isLast || text.endsWith(LINED_END))){
            return false;
        }
        return true;
    }

    static boolean checkLineEnd(boolean optional, String text){
        checkNotNull(text, "text");
        return optional?
            canParse(text.substring(0, text.length() - LINED_END.length()),
                LINED_END):
            willEndWith(text, LINED_END);
    }

    static boolean willEndWith(String text, String ender, List<String> endings){
        checkNotNull(text, "text");
        checkNotNull(ender, "ender");
        checkNotNull(endings, "endings");
        return willEndWith(text, ender, endings.toArray(new String[0]));
    }

    static boolean willEndWith(String text, String ender, String ... endings){
        checkNotNull(text, "text");
        checkNotNull(ender, "ender");
        return text.endsWith(ender)?
            canParse(text.substring(0, text.length() - ender.length()),
                combine(endings, ender)): false;
    }

    static boolean canParse(String text, List<String> endings){
        checkNotNull(text, "text");
        checkNotNull(endings, "endings");
        return canParse(text, endings.toArray(new String[endings.size()]));
    }

    static boolean canParse(String text, String ... endings){
        checkNotNull(text, "text");
        checkNotNull(endings, "endings");
        return checkParse(text, Arrays.asList(combine(endings, LINED_END)));
    }

    private static boolean checkParse(String text, List<String> endings){
        assert text != null: "Null text";
        assert endings != null: "Null endings";

        boolean isEscaped = false;
        for(int i = 0; i < text.length(); i++){
            if (! isEscaped){
                if (text.startsWith(CHAR_ESCAPE, i)){
                    isEscaped = true;
                } else {
                    for (String ender: endings){
                        if (text.startsWith(ender, i)){
                            return false;
                        }
                    }
                }
            } else {
                isEscaped = false;
            }
        }
        return ! isEscaped;
    }
    private AuxiliaryChecker(){}
}
package com.creativeartie.writerstudio.export.mock;

import java.util.*;

import com.creativeartie.writerstudio.export.*;

import com.google.common.collect.*;

import static org.junit.jupiter.api.Assertions.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

public class MockRenderData implements RenderData<Integer>{

    public RenderLine<Integer> newFootnote(){
        return null;
    }

    public OutputContentInfo<Integer> update(OutputContentInfo<Integer> info){
        return info;
    }

    public OutputContentInfo<Integer> split(OutputContentInfo<Integer> info){
        String full = info.getFullText();
        int split = info.getLineSplit();
        Integer spaces = info.getWidthSpaces();

        Optional<Integer> start = getFillingText(full, split);

        stateCheck(start.isPresent(), "Line split is too large: " + split);

        /// split[0] = last line text to ignore
        /// split[1] = current text to add
        String[] found = splitText(full, start.get());

        if (isFitAll(found[1], spaces)){
            info.setStartText(found[1]);
            info.setEndText("");
            /// info.setLineSplit(split); // No change
            return info; /// FITS ALL
        }

        Optional<Integer> current = getFillingText(full, ++split);
        if (! current.isPresent()){
            info.setStartText("");
            info.setEndText(found[1]);
            /// info.setLineSplit(split - 1); // No change
            return info; /// FITS NONE
        }

        while(current.isPresent()){
            /// split[0] = text to fit
            /// split[1] = possible overflow
            String[] test = splitText(full, start.get(), current.get());
            if (! isFitAll(test[0], spaces)){
                info.setStartText(found[0]);
                info.setEndText(found[1]);
                info.setLineSplit(split - 1);
                return info; /// FOUND FIRST UNFIT
            }
            found = test;
            current = getFillingText(full, ++split);
        }

        info.setStartText(found[0]);
        info.setEndText(found[1]);
        info.setLineSplit(split - 1);
        return info; ///

    }

    private Optional<Integer> getFillingText(String full, int to){
        Integer starter = 0;
        for (int i = 0; i < to; i++){
            starter = full.indexOf(starter,' ');
            if (starter == -1){
                return Optional.empty();
            }
        }
        return Optional.of(starter);
    }

    private boolean isFitAll(String text, Integer spaces){
        return text.length() < spaces;
    }

    private String[] splitText(String full, int to){
        return splitText(full, 0, to);
    }

    private String[] splitText(String full, int from, int to){
        return new String[]{full.substring(from, to + 1), full.substring(to)};
    }


    public Integer getWidth(OutputContentInfo<Integer> info){
        return getWidth(info.getCurrentText());
    }

    private Integer getWidth(String text){
        return text.length();
    }

    public Integer getHeight(OutputContentInfo<Integer> info){
        int base = 1;
        if(info.isBold()) base += 3;
        if(info.isItalics()) base += 5;
        if(info.isUnderline()) base += 7;
        if(info.isCoded()) base += 11;
        if(info.isSuperscript()) base += 13;
        return base * (info.getLineType().ordinal() + 1) * 10;
    }
}

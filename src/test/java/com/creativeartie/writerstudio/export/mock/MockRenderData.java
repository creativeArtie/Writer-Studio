package com.creativeartie.writerstudio.export.mock;

import java.util.*;

import com.creativeartie.writerstudio.export.*;

import com.google.common.collect.*;

import static org.junit.jupiter.api.Assertions.*;
import static com.creativeartie.writerstudio.main.ParameterChecker.*;

public class MockRenderData implements SetupDataSpace<Integer>{

    public RenderLine<Integer> newFootnote(){
        return null;
    }

    public OutputContentInfo<Integer> update(OutputContentInfo<Integer> info){
        return info;
    }

    public boolean isFitWidth(String text, Integer space){
        return text.length() <= space;
    }
/*
    public OutputContentInfo<Integer> split(OutputContentInfo<Integer> info){
        String full = info.getFullText();
        int line = info.getLineSplit();
        Integer spaces = info.getWidthSpaces();
        String[] split = splitText(full, line);
        if (canFitWidth(split[1], spaces)){
            info.setStartText(split[1]);
            info.setEndText(split[0]);
            return info;
        }
        String[] last = split;
        int until = line + 1;
        split = splitText(full, line, until);
        while (split[0].length() > 0){
            System.out.println(split[0] + "\t" + split[1]);
            split = splitText(full, line, ++until);
            return null;
        }
        info.setStartText("");
        info.setEndText(split[1]);
        info.setLineSplit(until);
        return info;
    }

    public String[] splitText(String text, int line){
        int split = 0;
        for(int i = 1; i < line; i++){
            int index = text.indexOf(split, ' ') + 1;
            if (index == -1){
                break;
            }
            split = index;
        }
        return new String[]{text.substring(0, split), text.substring(split)};
    }

    public String[] splitText(String text, int line, int cur){
        String[] use = splitText(text, line);
        int split = text.length();
        int until = cur - line;
        for(int i = line; i < until; i++){
            int index = use[1].indexOf(split, ' ') + 1;
            if (index == -1){
                return null;
            }
            split = index;
        }
        return new String[]{use[1].substring(0, split), use[1].substring(split)};
    }

    private boolean canFitWidth(String text, Integer width){
        return text.length() <= width;
    }
*/
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

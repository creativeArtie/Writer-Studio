package com.creativeartie.writerstudio.export.mock;

import java.util.*;

import com.creativeartie.writerstudio.export.*;

import com.google.common.collect.*;

import static org.junit.jupiter.api.Assertions.*;

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
        int spaces = info.getWidthSpaces();

        /// Skip the text of the last lines
        int ptr = 0;
        for(int i = 0; i < split; i++){
            ptr = full.indexOf(ptr, ' ');
        }

        String filling = full.substring(ptr);
        if (getWidth(filling) <= spaces){
            /// fits everything RETURN
            info.setStartText(filling);
            info.setEndText("");
            return info;
        }


        ptr = filling.indexOf(' '); /// reuses ptr :-)
        if (ptr == -1){
            ///Single word -> not fit RETURN
            info.setStartText("");
            info.setEndText(filling);
            return info;
        }

        System.out.println("MockRenderData");
        System.out.println(filling);
        System.out.println(filling.length());
        System.out.println(ptr);
        System.out.println();

        split++;
        String current = filling.substring(ptr);
        while (getWidth(current) < spaces){
            int tmp = filling.indexOf(ptr, ' ');
            if (tmp == -1){
                /// Last world
            }
            current = filling.substring(tmp);
            split++;
        }

        /// First word / After x word not fit RETURN
        info.setStartText(filling.substring(0, ptr + 1));
        info.setEndText(filling.substring(ptr + 1));
        info.setLineSplit(split);
        return info;
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

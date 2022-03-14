package com.creativeartie.writer.export.mock;

import com.creativeartie.writer.export.*;

public class MockRenderData implements SetupDataSpace<Integer>{

    public RenderLine<Integer> newFootnote(){
        return null;
    }

    public OutputContentInfo<Integer> update(OutputContentInfo<Integer> info){
        return info;
    }

    public boolean isFitWidth(String text, Integer space, Integer extra){
        extra = extra == null? 0: extra;
        if (text.endsWith(" ")){
            return text.length() - 1 <= space - extra;
        }
        return text.length() <= space - extra;
    }

    public Integer getWidth(OutputContentInfo<Integer> info){
        return getWidth(info.getCurrentText());
    }

    private Integer getWidth(String text){
        return text.length();
    }

    public Integer getHeight(OutputContentInfo<Integer> info){
        int base = 1;
        for (DataContentType type: info.getFormats()){
            switch(type){
                case BOLD:
                    base *= 3;
                    break;
                case ITALICS:
                    base *= 5;
                    break;
                case UNDERLINE:
                    base *= 7;
                    break;
                case LITERAL:
                    base *= 11;
                    break;
                case SUPERSRCRIPT:
                    base *= 13;
            }
        }
        return base * (info.getLineType().ordinal() + 1) * 10;
    }
}

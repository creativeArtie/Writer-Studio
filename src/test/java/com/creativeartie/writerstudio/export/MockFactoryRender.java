package com.creativeartie.writerstudio.export;

import java.util.*;
import java.util.Optional;
import com.google.common.base.*;

public enum MockFactoryRender implements FactoryRender<Integer>{

    FACTORY;

    @Override
    public RenderContent<Integer> getRenderContent(){
        RenderContent<Integer>.Builder builder = RenderContent.builder();
        return builder
            .setContentSplitter((width, content) -> {
                String current = "";
                String overflow = "";
                for(String split: Splitter.on(" ").split(content.getText())){
                    if (current.length() == 0 && split.length() < width){
                        current = split;
                    } else if (current.length() + split.length() + 1 <= width){
                        current += " " + split;
                    } else {
                        overflow += (overflow.length() == 0? "": " ") + split;
                    }
                }
                return new String[]{current, overflow};
            })
            .setContentWidth(content -> content.getText().length())
            .build();
    }

}

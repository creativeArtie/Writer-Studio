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
            .setSplitContent(renderer -> {
                String current = "";
                String overflow = "";
                float width = renderer.getMaxWidth();
                for(String split: Splitter.on(" ").split(renderer.getUseText())){
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
            .setCalculateWidth(renderer -> renderer.getUseText().length())
            .build();
    }

    @Override
    public RenderDivision<Integer> getRenderDivision(){
        RenderDivision<Integer>.Builder builder = RenderDivision.builder();
        return builder
            .setCalcaluteSpace(renderer ->
                (renderer.isFirstLine()? 20: 40) - renderer.getFillWidth()
            ).setCalcaluteFill(renderer -> renderer.getFillWidth() +
                renderer.getApplyContent().getWidth()
            ).build();
    }

    @Override
    public Integer getZero(){
        return 0;
    }

}

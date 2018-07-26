package com.creativeartie.writerstudio.export;

import java.util.*;
import java.util.Optional;
import com.google.common.base.*;

public enum MockFactoryRender{

    FACTORY;

    public RenderContent<Integer> getRenderContent(){
        RenderContent<Integer>.Builder builder = RenderContent.builder();
        builder.setSplitContent(renderer -> {
                String text = renderer.getUseText();
                float width = renderer.getMaxWidth();

                String current = "";
                String overflow = "";
                Iterator<String> it = Splitter.on(" ")
                    .trimResults().omitEmptyStrings().split(text).iterator();

                if (! it.hasNext()){
                    return new String[]{current, overflow};
                }
                String next = it.next();

                while(current.length() + next.length() < width){
                    current += next;
                    if (! it.hasNext()){
                        return new String[]{current, overflow};
                    }
                    next = " " + it.next();
                }

                overflow = next;

                while(it.hasNext()){
                    overflow += (overflow.isEmpty()? "": " ") + it.next();
                }
                return new String[]{current, overflow};
            })
            .setCalculateWidth(renderer -> renderer.getUseText().length())
            .setGetZero(() -> 0);
        return builder.build();
    }

    public RenderDivision<Integer> getRenderDivision(){
        RenderDivision<Integer>.Builder builder = RenderDivision.builder(
            getRenderContent()
        );
        builder
            .setCalcaluteSpace(renderer ->
                (renderer.isFirstLine()? 20: 40) - renderer.getFillWidth()
            ).setCalcaluteFill(renderer ->
                renderer.getFillWidth() + renderer.getContentWidth()
            ).setGetZero(() -> 0)
            .build();
        return builder.build();
    }
}

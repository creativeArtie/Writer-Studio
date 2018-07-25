package com.creativeartie.writerstudio.export;

import java.util.*;

import com.google.common.collect.*;

import static com.creativeartie.writerstudio.main.ParameterChecker.*;

/** Export a span of text */
abstract class ExportBaseParent<T extends Number, U> extends ForwardingList<U>
    implements ExportBase<T>
{

    private final FactoryRender<T> renderFactory;

    ExportBaseParent(FactoryRender<T> renderer){
        renderFactory = argumentNotNull(renderer, "renderer");
    }

    @Override
    public final FactoryRender<T> getRender(){
        return renderFactory;
    }
}

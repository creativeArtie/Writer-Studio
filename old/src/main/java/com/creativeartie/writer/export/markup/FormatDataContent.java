package com.creativeartie.writer.export.markup;

import com.creativeartie.writer.export.*;
import com.creativeartie.writer.lang.markup.*;

public final class FormatDataContent extends FormatData<FormatSpanContent> {

    FormatDataContent(FormatSpanContent span){
        super(span);
    }

    public boolean isFootnote(){
        return false;
    }

    public void updatePageInfo(OutputPageInfo info){}

    public String getText(){
        return getTargetSpan().getRendered();
    }
}

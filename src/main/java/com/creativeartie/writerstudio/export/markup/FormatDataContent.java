package com.creativeartie.writerstudio.export.markup;

import java.util.*;
import com.creativeartie.writerstudio.export.*;
import com.creativeartie.writerstudio.lang.markup.*;

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

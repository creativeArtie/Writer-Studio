package com.creativeartie.writerstudio.lang.markup;

import com.creativeartie.writerstudio.lang.*;

public class FormatAgendaDebug{

    public static IDBuilder buildId(String id){
        return new IDBuilder().addCategory(AuxiliaryData.TYPE_AGENDA).setId(id);
    }
}

package com.creativeartie.jwriter.lang;

import java.util.Optional;

public interface Catalogued{
    public Optional<CatalogueIdentity> getSpanIdentity();
    
    public boolean isId();
    
    public default boolean isRef(){
        return ! isId();
    }
    
    public default SpanBranch getBranch(){
        return (SpanBranch) this;
    }
}

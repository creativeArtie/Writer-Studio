package com.creativeartie.jwriter.lang;

import java.util.Optional;

/**
 * Gets {@link CatalogueIdentity} to store in {@link CatalogueMap} and modifies
 * {@link SpanBranch#getIdStatus}.
 */
public interface Catalogued{
    public Optional<CatalogueIdentity> getSpanIdentity();

    public boolean isId();

    public default boolean isRef(){
        return ! isId();
    }
}

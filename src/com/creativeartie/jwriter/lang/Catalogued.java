package com.creativeartie.jwriter.lang;

import java.util.Optional;

/**
 * Gets {@link CatalogueIdentity} to store in {@link CatalogueMap} and modifies
 * {@link SpanBranch#getIdStatus}.
 */
public interface Catalogued{

    /** Get the {@link CatalogueIdentity} if there is any. */
    public Optional<CatalogueIdentity> getSpanIdentity();

    /** Check if this represents an Identity. */
    public boolean isId();

    /** Check if this represents an Reference. */
    public default boolean isRef(){
        return ! isId();
    }
}

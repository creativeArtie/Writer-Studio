package com.creativeartie.writer.lang;

import java.util.*;

/** An {@link Span} that has an {@link CatalogueIdentity}. */
public interface Catalogued{

    /** Get the {@link CatalogueIdentity} if there is any.
     *
     * @return answer or empty
     */
    public Optional<CatalogueIdentity> getSpanIdentity();

    /** Check if this represents an Identity. */
    public boolean isId();

    /** Check if this represents an Reference. */
    public default boolean isRef(){
        return ! isId();
    }
}

package com.creativeartie.writerstudio.lang;

/** Types of error with the {@link CatalogueIdentity}. */
public enum CatalogueStatus implements StyleInfo {
    /// This is no id assoicate in a SpanBranch
    NO_ID,
    /// There is an id but nothing is refer to it
    UNUSED,
    /// A reference that pointing to no known CatalogueIdentity.
    NOT_FOUND,
    /// More than one CatalogueIdentity has the same name
    MULTIPLE,
    /// No error is found.
    READY;
}

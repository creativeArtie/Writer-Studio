package com.creativeartie.writer.lang;

/** Types of error with the {@link CatalogueIdentity}. */
public enum CatalogueStatus{
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

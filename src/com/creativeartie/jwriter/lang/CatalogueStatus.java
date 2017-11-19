package com.creativeartie.jwriter.lang;

import static com.creativeartie.jwriter.lang.SetupStrings.*;

/**
 * Types of error with the CatalogueIdentity.
 */
public enum CatalogueStatus implements DetailStyle {
    /// This is no id assoicate in a CatalogueHolder
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

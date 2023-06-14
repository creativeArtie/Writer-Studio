package com.creativeartie.humming.main;

import com.google.common.base.*;

public enum CssStyles {
    FILE_HEADING, FILE_DETIAL, ACTIVE, INACTIVE;

    @Override
    public String toString() {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
    }
}

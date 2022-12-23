package com.creativeartie.humming.document;

import java.util.*;

public interface Identity {
    IdentityGroup getIdGroup();

    List<String> getCategories();

    String getId();

    String getFullId();

    String getInternalId();
}

package com.creativeartie.writerstudio.export;

import java.util.*;

interface DivisionPolicySize<T extends Number> {

    T calculateFillWidth(List<Content<T>> contents);

    T calculateFillHeight(List<Content<T>> contents);

    T calculateSpace(T maximum, T current);
}

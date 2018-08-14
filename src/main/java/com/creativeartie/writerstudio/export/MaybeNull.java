package com.creativeartie.writerstudio.export;

import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Inherited

public @interface MaybeNull{
}

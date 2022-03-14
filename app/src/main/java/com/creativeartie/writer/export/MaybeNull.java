package com.creativeartie.writer.export;

import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Inherited

public @interface MaybeNull{
}

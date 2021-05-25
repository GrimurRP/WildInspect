package com.bgsoftware.wildinspect.config;

import java.lang.annotation.*;

@Repeatable(Comment.List.class)
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment {

    String value();

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    @interface List {
        Comment[] value();
    }

}

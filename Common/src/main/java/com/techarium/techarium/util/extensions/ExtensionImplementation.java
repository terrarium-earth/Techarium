package com.techarium.techarium.util.extensions;

import java.lang.annotation.*;

/**
 * A source marker to indicate that the following element is the implementation for an element in a type extended via {@link ExtensionFor} and potentially annotated with {@link ExtendableDeclaration}
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface ExtensionImplementation {
}

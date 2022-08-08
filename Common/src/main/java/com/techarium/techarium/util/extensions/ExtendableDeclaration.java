package com.techarium.techarium.util.extensions;

import java.lang.annotation.*;

/**
 * A source marker to indicate that the following element could be implemented using an extension with {@link ExtensionFor}.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface ExtendableDeclaration {
}

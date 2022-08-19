package earth.terrarium.techarium.util.extensions;

import java.lang.annotation.*;

/**
 * Indicates to a class post processor that the target {@link ExtensionFor#value} should have elements replaced or extended by elements from the annotated type.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
@ExtensionImplementation
public @interface ExtensionFor {
    Class<?> value();
}

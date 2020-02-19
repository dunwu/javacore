// Annotation type with an array parameter -  Page 173
package io.github.dunwu.javacore.effective.chapter06.item35;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method is a test method that must throw the any of the designated exceptions to
 * succeed.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest {

    Class<? extends Exception>[] value();

}

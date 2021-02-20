package framework.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Polite {
    String before() default "Bien le bonjour !";
    String after() default "Tchô !";
}

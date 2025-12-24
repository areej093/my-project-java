package tn.isg.economics.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelValidation {
    double minConfidence() default 0.5;
    String description() default "";
}

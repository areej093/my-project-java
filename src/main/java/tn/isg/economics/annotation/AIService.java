package tn.isg.economics.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AIService {
    String provider();
    String version() default "1.0";
}

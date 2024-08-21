package org.firstinspires.ftc.teamcode.utils.Annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
//@SuppressWarnings(value={"unused"})
public @interface LocalizationPlugin {
}

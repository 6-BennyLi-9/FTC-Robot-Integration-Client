package org.firstinspires.ftc.teamcode.Utils.Annotations;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在调试阶段结束后，请在{@link TuningOrSampleAutonomous}中取消注释{@link Disabled}
 * @see com.qualcomm.robotcore.eventloop.opmode.Autonomous
 */
//@Disabled
@Target({ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface TuningOrSampleAutonomous {
	String name();
	String group() default "";
}

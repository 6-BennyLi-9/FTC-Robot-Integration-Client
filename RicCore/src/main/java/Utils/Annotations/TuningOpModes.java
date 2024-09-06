package Utils.Annotations;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 在调试阶段结束后，请在{@link TuningOpModes}中取消注释{@link Disabled}
 */
//@Disabled
@Target({ElementType.TYPE})
@Documented
public @interface TuningOpModes{
}

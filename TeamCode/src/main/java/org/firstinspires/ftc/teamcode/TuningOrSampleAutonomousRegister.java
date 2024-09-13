package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;

import org.firstinspires.ftc.teamcode.Utils.Annotations.TuningOrSampleAutonomous;

import java.io.IOException;
import java.lang.reflect.TypeVariable;

/**
 * @see org.firstinspires.ftc.teamcode.Utils.Annotations.TuningOrSampleAutonomous
 */
@Deprecated
public final class TuningOrSampleAutonomousRegister {
	//TODO:将DISABLED设为true以禁用TuningOrSampleAutonomous
	public static final boolean DISABLED = false;

	@OpModeRegistrar
	public static void mainRegister(OpModeManager manager) throws IOException {
		Class<? extends TypeVariable[]> aClass = TuningOrSampleAutonomous.class.getTypeParameters().getClass();
	}
}

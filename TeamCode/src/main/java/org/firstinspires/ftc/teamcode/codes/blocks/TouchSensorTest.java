package org.firstinspires.ftc.teamcode.codes.blocks;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.Params;

/**
 * 测试触碰传感器
 */
@TeleOp(name = "TouchSensorTest (Blocks to Java)",group = Params.Configs.TuningAndTuneOpModesGroup)
@Disabled
public class TouchSensorTest extends LinearOpMode {

	/**
	 * This OpMode demonstrates how to use a REV Robotics Touch Sensor, REV Robotics
	 * Magnetic Limit Switch, or other device that implements the TouchSensor interface. Any
	 * touch sensor that connects its output to ground when pressed (known as "active low") can
	 * be configured as a "REV Touch Sensor". This includes REV's Magnetic Limit Switch.
	 * <p>
	 * A REV Robotics Touch Sensor must be configured on digital port number 1, 3, 5, or 7.
	 * A Magnetic Limit Switch can be configured on any digital port.
	 */
	@Override
	public void runOpMode() {
		TouchSensor touchSensor = hardwareMap.get(TouchSensor.class, "touchSensor");

		// Put initialization blocks here.
		waitForStart();
		if (opModeIsActive()) {
			// Put run blocks here.
			while (opModeIsActive()) {
				// Put loop blocks here.
				if (touchSensor.isPressed()) {
					telemetry.addData("Touch Sensor", "Is Pressed");
				} else {
					telemetry.addData("Touch Sensor", "Is Not Pressed");
				}
				telemetry.update();
			}
		}
	}
}

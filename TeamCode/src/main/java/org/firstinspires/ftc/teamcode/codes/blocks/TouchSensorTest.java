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
	public TouchSensor touchSensor;

	/**
	 * 此 OpMode 演示了如何使用 REV Robotics 触摸传感器 REV Robotics
	 * 磁性限位开关，或其他实现 TouchSensor 接口的设备。任何
	 * 按下时将其输出接地的触摸传感器（称为“低电平有效”）可以
	 * 配置为“REV Touch Sensor”。这包括 REV 的磁性限位开关。
	 * <p>
	 * REV Robotics 触摸传感器必须在数字端口号 1、3、5 或 7 上配置。
	 * 磁性限位开关可以在任何数字端口上配置。
	 */
	@Override
	public void runOpMode() {
		touchSensor = hardwareMap.get(TouchSensor.class, "touchSensor");

		waitForStart();
		if (opModeIsActive()) {
			while (opModeIsActive()) {
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

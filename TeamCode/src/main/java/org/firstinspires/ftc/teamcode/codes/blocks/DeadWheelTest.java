package org.firstinspires.ftc.teamcode.codes.blocks;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Params;

/**
 * 用于测试死轮的方向
 * <p>
 * 取消 @Disabled 注解以运行此 OpMode
 */
@TeleOp(name = "DeadWheelTest (Blocks to Java)",group = Params.Configs.TuningAndTuneOpModesGroup)
@Disabled
public class DeadWheelTest extends LinearOpMode {

	public DcMotor leftBack;
	public DcMotor leftFront;
	public DcMotor rightBack;
	public DcMotor rightFront;

	@Override
	public void runOpMode() {
		leftBack = hardwareMap.get(DcMotor.class, "leftBack");
		leftFront = hardwareMap.get(DcMotor.class, "leftFront");
		rightBack = hardwareMap.get(DcMotor.class, "rightBack");
		rightFront = hardwareMap.get(DcMotor.class, "rightFront");

		// Put initialization blocks here.
		waitForStart();
		leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		if (opModeIsActive()) {
			// Put run blocks here.
			while (opModeIsActive()) {
				// Put loop blocks here.
				telemetry.update();
				telemetry.addData("leftFront", leftFront.getCurrentPosition());
				telemetry.addData("leftBack", leftBack.getCurrentPosition());
				telemetry.addData("rightFront", rightFront.getCurrentPosition());
				telemetry.addData("rightBack", rightBack.getCurrentPosition());
			}
		}
	}
}

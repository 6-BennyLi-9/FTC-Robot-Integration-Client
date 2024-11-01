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
		this.leftBack = this.hardwareMap.get(DcMotor.class, "leftBack");
		this.leftFront = this.hardwareMap.get(DcMotor.class, "leftFront");
		this.rightBack = this.hardwareMap.get(DcMotor.class, "rightBack");
		this.rightFront = this.hardwareMap.get(DcMotor.class, "rightFront");

		// Put initialization blocks here.
		this.waitForStart();
		this.leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		this.leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		this.rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		this.rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		if (this.opModeIsActive()) {
			// Put run blocks here.
			while (this.opModeIsActive()) {
				// Put loop blocks here.
				this.telemetry.update();
				this.telemetry.addData("leftFront", this.leftFront.getCurrentPosition());
				this.telemetry.addData("leftBack", this.leftBack.getCurrentPosition());
				this.telemetry.addData("rightFront", this.rightFront.getCurrentPosition());
				this.telemetry.addData("rightBack", this.rightBack.getCurrentPosition());
			}
		}
	}
}

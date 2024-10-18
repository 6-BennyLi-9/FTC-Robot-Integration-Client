package org.firstinspires.ftc.teamcode.ric.codes.tunings;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.ric.Params;
import org.firstinspires.ftc.teamcode.ric.codes.templates.TuningProgramTemplate;
import org.firstinspires.ftc.teamcode.ric.hardwares.integration.IntegrationMotor;
import org.firstinspires.ftc.teamcode.ric.hardwares.namespace.HardwareDeviceTypes;

/**
 * done
 * @since 2024.10.15
 */
@Autonomous(name = "MotorsTest",group = Params.Configs.TuningAndTuneOpModesGroup)
@Disabled
public class MotorsTest extends TuningProgramTemplate {
	public DcMotorEx        motor1;
	public IntegrationMotor motor2;

	public double lstM1=0,lstM2=0,cM1,cM2;

	@Override
	public void whileActivating() {
		motor1.setPower(1);
		motor2.setPower(1);
		motor2.update();

		robot.client.changeData("M1 power",motor1.getPower());
		robot.client.changeData("M2 power",motor2.getPower());

		robot.client.changeData("M1 pose",cM1=motor1.getCurrentPosition());
		robot.client.changeData("M2 pose",cM2=motor2.getPosition());

		robot.client.changeData("M1 velocity",cM1-lstM1);
		robot.client.changeData("M2 velocity",cM2-lstM2);

		lstM1=cM1;
		lstM2=cM2;
	}

	@Override
	public void whenInit() {
		motor1 = ((IntegrationMotor) robot.lazyIntegratedDevices.getDevice(HardwareDeviceTypes.LeftFront)).motor;
		motor2= (IntegrationMotor) robot.lazyIntegratedDevices.getDevice(HardwareDeviceTypes.RightRear);
		motor2.ConfigPidEnable(false);
	}
}

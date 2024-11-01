package org.roadrunner.core.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.roadrunner.core.Drawing;
import org.roadrunner.core.MecanumDrive;
import org.roadrunner.core.TankDrive;

public class LocalizationTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
	    this.telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

        if (TuningOpModes.DRIVE_CLASS.equals(MecanumDrive.class)) {
            final MecanumDrive drive = new MecanumDrive(this.hardwareMap, new Pose2d(0, 0, 0));

	        this.waitForStart();

            while (this.opModeIsActive()) {
                drive.setDrivePowers(new PoseVelocity2d(
                        new Vector2d(
                                - this.gamepad1.left_stick_y,
                                - this.gamepad1.left_stick_x
                        ),
                        - this.gamepad1.right_stick_x
                ));

                drive.updatePoseEstimate();

	            this.telemetry.addData("x", drive.pose.position.x);
	            this.telemetry.addData("y", drive.pose.position.y);
	            this.telemetry.addData("heading (deg)", Math.toDegrees(drive.pose.heading.toDouble()));
	            this.telemetry.update();

                final TelemetryPacket packet = new TelemetryPacket();
                packet.fieldOverlay().setStroke("#3F51B5");
                Drawing.drawRobot(packet.fieldOverlay(), drive.pose);
                FtcDashboard.getInstance().sendTelemetryPacket(packet);
            }
        } else if (TuningOpModes.DRIVE_CLASS.equals(TankDrive.class)) {
            final TankDrive drive = new TankDrive(this.hardwareMap, new Pose2d(0, 0, 0));

	        this.waitForStart();

            while (this.opModeIsActive()) {
                drive.setDrivePowers(new PoseVelocity2d(
                        new Vector2d(
                                - this.gamepad1.left_stick_y,
                                0.0
                        ),
                        - this.gamepad1.right_stick_x
                ));

                drive.updatePoseEstimate();

	            this.telemetry.addData("x", drive.pose.position.x);
	            this.telemetry.addData("y", drive.pose.position.y);
	            this.telemetry.addData("heading (deg)", Math.toDegrees(drive.pose.heading.toDouble()));
	            this.telemetry.update();

                final TelemetryPacket packet = new TelemetryPacket();
                packet.fieldOverlay().setStroke("#3F51B5");
                Drawing.drawRobot(packet.fieldOverlay(), drive.pose);
                FtcDashboard.getInstance().sendTelemetryPacket(packet);
            }
        } else {
            throw new RuntimeException();
        }
    }
}

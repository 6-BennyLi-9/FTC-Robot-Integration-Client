package org.firstinspires.ftc.teamcode.drives.controls.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.drives.controls.MecanumDrive;
import org.firstinspires.ftc.teamcode.drives.controls.TrajectoryType;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrder;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrderPackage;
import org.firstinspires.ftc.teamcode.hardwares.Chassis;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.DriveDirection;
import org.firstinspires.ftc.teamcode.utils.Complex;
import org.firstinspires.ftc.teamcode.utils.Mathematics;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.Vector2d;
import org.firstinspires.ftc.teamcode.utils.annotations.ExtractedInterfaces;

public class DriveAction implements DriveOrder {
	private final Chassis chassis;

	/**
	 * 为了简化代码书写，我们使用了{@code @Override}的覆写来保存数据。
	 * <p>如果使用enum，则代码会明显过于臃肿</p>
	 */
	public abstract static class actionRunningNode implements Action {}
	public actionRunningNode MEAN;
	public double BufPower;
	public Position2d DeltaTrajectory;
	public final Position2d pose;
	/**
	 * {@code 面向开发者：} 不建议在DriveCommands中更改trajectoryType的值，而是在drivingCommandsBuilder中
	 */
	public TrajectoryType trajectoryType;

	DriveAction(final Chassis chassis, final double BufPower, final Position2d pose) {
		this.BufPower = BufPower;
		this.pose = pose;
		this.chassis = chassis;
	}

	@Override
	public void setPower(final double power) {
		this.MEAN =new actionRunningNode() {
			@Override
			public boolean run(@NonNull final TelemetryPacket telemetryPacket) {
				DriveAction.this.BufPower =power;
				DriveAction.this.BufPower = Mathematics.intervalClip(DriveAction.this.BufPower, - 1, 1);
				return false;
			}
		};
	}

	@Override
	public void turn(final double radians) {
		this.MEAN =new actionRunningNode() {
			@Override
			public boolean run(@NonNull final TelemetryPacket telemetryPacket) {
				DriveAction.this.chassis.drive(DriveDirection.turn, DriveAction.this.BufPower);
				return false;
			}
		};
		this.DeltaTrajectory = new Position2d(0, 0, radians);
	}

	@Override
	public void strafeInDistance(final double radians, final double distance) {
		this.MEAN =new actionRunningNode() {
			@Override
			public boolean run(@NonNull final TelemetryPacket telemetryPacket) {
				DriveAction.this.chassis.SimpleRadiansDrive(DriveAction.this.BufPower, radians);
				return false;
			}
		};
		this.DeltaTrajectory = new Position2d(
				(new Complex(new Vector2d(distance, 0))).times(new Complex(Math.toDegrees(radians)))
						.divide(new Complex(Math.toDegrees(radians)).magnitude())
						.toVector2d()
				, radians
		);
	}

	@Override
	public void strafeTo(final Vector2d pose) {
		final Complex cache = new Complex(this.pose.minus(pose));
		this.MEAN =new actionRunningNode() {
			@Override
			public boolean run(@NonNull final TelemetryPacket telemetryPacket) {
				DriveAction.this.chassis.SimpleRadiansDrive(DriveAction.this.BufPower, Math.toRadians(cache.toDegree()));
				return false;
			}
		};
		this.DeltaTrajectory = new Position2d(cache.toVector2d(), this.pose.heading);
	}

	@Override
	public void run() {
		Actions.runBlocking(this.MEAN);
	}

	@Override
	public Position2d getDeltaTrajectory() {
		return this.DeltaTrajectory;
	}

	@NonNull
	@Override
	public Position2d nextPose() {
		return new Position2d(this.pose.x + this.DeltaTrajectory.x, this.pose.y + this.DeltaTrajectory.y, this.pose.heading + this.DeltaTrajectory.heading
		);
	}

	@Override
	public Position2d getPose() {
		return this.pose;
	}

	@Override
	public TrajectoryType getState() {
		return this.trajectoryType;
	}

	@ExtractedInterfaces
	public Action AsAction(final MecanumDrive drive){
		return new InstantAction(() -> drive.runOrderPackage((DriveOrderPackage) this));
	}
}

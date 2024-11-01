package org.firstinspires.ftc.teamcode.drives.controls.commands;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.drives.controls.TrajectoryType;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrder;
import org.firstinspires.ftc.teamcode.hardwares.Chassis;
import org.firstinspires.ftc.teamcode.hardwares.integration.hardwaremap.namespace.DriveDirection;
import org.firstinspires.ftc.teamcode.utils.Complex;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Mathematics;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.Vector2d;

public class DriveCommand implements DriveOrder {
	private final Chassis chassis;

	/**
	 * 为了简化代码书写，我们使用了{@code @Override}的覆写来保存数据。
	 * <p>如果使用enum，则代码会明显过于臃肿</p>
	 */
	public abstract static class commandRunningNode {
		public void runCommand() {
		}
	}

	public commandRunningNode commandMeaning;
	public double             BufPower;
	public Position2d DeltaTrajectory;
	public final Position2d pose;
	/**
	 * {@code 面向开发者：} 不建议在DriveCommands中更改trajectoryType的值，而是在drivingCommandsBuilder中
	 */
	public TrajectoryType trajectoryType;

	public DriveCommand(Chassis chassis, final double BufPower, final Position2d pose) {
		this.BufPower = BufPower;
		this.pose = pose;
		this.chassis = chassis;
		this.DeltaTrajectory =new Position2d(0,0,0);
	}

	@Override
	public void setPower(final double power) {
		this.commandMeaning = new commandRunningNode() {
			@Override
			public void runCommand() {
				DriveCommand.this.BufPower = power;
				DriveCommand.this.BufPower = Mathematics.intervalClip(DriveCommand.this.BufPower, - 1.0f, 1.0f);
			}
		};
	}

	@Override
	public void turn(final double radians) {
		this.commandMeaning = new commandRunningNode() {
			@Override
			public void runCommand() {
				DriveCommand.this.chassis.drive(DriveDirection.turn, DriveCommand.this.BufPower);
			}
		};
		this.DeltaTrajectory = new Position2d(0,0, radians);
	}

	@Override
	public void strafeInDistance(final double radians, final double distance) {
		this.commandMeaning = new commandRunningNode() {
			@Override
			public void runCommand() {
				DriveCommand.this.chassis.SimpleRadiansDrive(DriveCommand.this.BufPower, radians);
			}
		};
		this.DeltaTrajectory = new Position2d(
				(new Complex(new Vector2d(distance, 0))).times(new Complex(Math.toDegrees(radians)))
						.divide(new Complex(Math.toDegrees(radians)).magnitude())
						.toVector2d()
				, radians);
	}

	@Override
	public void strafeTo(final Vector2d pose) {
		final Complex cache = new Complex(this.pose.minus(pose));
		this.commandMeaning = new commandRunningNode() {
			@Override
			public void runCommand() {
				DriveCommand.this.chassis.SimpleRadiansDrive(DriveCommand.this.BufPower, Math.toRadians(cache.toDegree()));
			}
		};
		this.DeltaTrajectory = new Position2d(cache.toVector2d(), this.pose.heading);
	}

	@Override
	public void run() {
		if(null == commandMeaning){
			return;
		}
		this.commandMeaning.runCommand();
	}

	@Override
	public Position2d getDeltaTrajectory() {
		return this.DeltaTrajectory;
	}

	@Override
	@NonNull
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
}

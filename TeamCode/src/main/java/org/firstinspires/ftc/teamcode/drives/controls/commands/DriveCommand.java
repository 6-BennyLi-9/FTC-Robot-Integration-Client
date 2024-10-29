package org.firstinspires.ftc.teamcode.drives.controls.commands;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.drives.controls.TrajectoryType;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrder;
import org.firstinspires.ftc.teamcode.hardwares.Chassis;
import org.firstinspires.ftc.teamcode.hardwares.namespace.DriveDirection;
import org.firstinspires.ftc.teamcode.utils.Complex;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.Vector2d;

public class DriveCommand implements DriveOrder {
	private final Chassis chassis;

	/**
	 * 为了简化代码书写，我们使用了<code>@Override</code>的覆写来保存数据。
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
	 * <code>面向开发者：</code> 不建议在DriveCommands中更改trajectoryType的值，而是在drivingCommandsBuilder中
	 */
	public TrajectoryType trajectoryType = null;

	public DriveCommand(final Chassis chassis, double BufPower, Position2d pose) {
		this.BufPower = BufPower;
		this.pose = pose;
		this.chassis = chassis;
		DeltaTrajectory=new Position2d(0,0,0);
	}

	@Override
	public void setPower(double power) {
		commandMeaning = new commandRunningNode() {
			@Override
			public void runCommand() {
				BufPower = power;
				BufPower = Functions.intervalClip(BufPower, -1f, 1f);
			}
		};
	}

	@Override
	public void turn(double radians) {
		commandMeaning = new commandRunningNode() {
			@Override
			public void runCommand() {
				chassis.drive(DriveDirection.turn, BufPower);
			}
		};
		DeltaTrajectory = new Position2d(0,0, radians);
	}

	@Override
	public void strafeInDistance(double radians, double distance) {
		commandMeaning = new commandRunningNode() {
			@Override
			public void runCommand() {
				chassis.SimpleRadiansDrive(BufPower, radians);
			}
		};
		DeltaTrajectory = new Position2d(
				(new Complex(new Vector2d(distance, 0))).times(new Complex(Math.toDegrees(radians)))
						.divide(new Complex(Math.toDegrees(radians)).magnitude())
						.toVector2d()
				, radians);
	}

	@Override
	public void strafeTo(Vector2d pose) {
		Complex cache = new Complex(this.pose.minus(pose));
		commandMeaning = new commandRunningNode() {
			@Override
			public void runCommand() {
				chassis.SimpleRadiansDrive(BufPower, Math.toRadians(cache.toDegree()));
			}
		};
		DeltaTrajectory = new Position2d(cache.toVector2d(), this.pose.heading);
	}

	@Override
	public void run() {
		if(commandMeaning == null){
			return;
		}
		commandMeaning.runCommand();
	}

	@Override
	public Position2d getDeltaTrajectory() {
		return DeltaTrajectory;
	}

	@Override
	@NonNull
	public Position2d nextPose() {
		return new Position2d(
				pose.x + DeltaTrajectory.x,
				pose.y + DeltaTrajectory.y,
				pose.heading + DeltaTrajectory.heading
		);
	}

	@Override
	public Position2d getPose() {
		return pose;
	}

	@Override
	public TrajectoryType getState() {
		return trajectoryType;
	}
}

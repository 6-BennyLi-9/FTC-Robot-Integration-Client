package org.firstinspires.ftc.teamcode.DriveControls.Commands;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition.DriveOrder;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Utils.Complex;
import org.firstinspires.ftc.teamcode.DriveControls.TrajectoryType;
import org.firstinspires.ftc.teamcode.Hardwares.Namespace.DriveDirection;
import org.firstinspires.ftc.teamcode.Utils.Functions;

public class DriveCommand implements DriveOrder {
	private final Classic classic;

	/**
	 * 为了简化代码书写，我们使用了<code>@Override</code>的覆写来保存数据。
	 * <p>如果使用enum，则代码会明显过于臃肿</p>
	 */
	public abstract static class commandRunningNode {
		public void runCommand() {
		}
	}

	public commandRunningNode MEAN;
	public double BufPower;
	public Pose2d DeltaTrajectory;
	public final Pose2d pose;
	/**
	 * <code>面向开发者：</code> 不建议在DriveCommands中更改trajectoryType的值，而是在drivingCommandsBuilder中
	 */
	public TrajectoryType trajectoryType = null;

	public DriveCommand(final Classic classic, double BufPower, Pose2d pose) {
		this.BufPower = BufPower;
		this.pose = pose;
		this.classic = classic;
	}

	@Override
	public void SetPower(double power) {
		MEAN = new commandRunningNode() {
			@Override
			public void runCommand() {
				BufPower = power;
				BufPower = Functions.intervalClip(BufPower, -1f, 1f);
			}
		};
	}

	@Override
	public void Turn(double radians) {
		MEAN = new commandRunningNode() {
			@Override
			public void runCommand() {
				classic.drive(DriveDirection.turn, BufPower);
			}
		};
		DeltaTrajectory = new Pose2d(new Vector2d(0, 0), radians);
	}

	@Override
	public void StrafeInDistance(double radians, double distance) {
		MEAN = new commandRunningNode() {
			@Override
			public void runCommand() {
				classic.SimpleRadiansDrive(BufPower, radians);
			}
		};
		DeltaTrajectory = new Pose2d(
				(new Complex(new Vector2d(distance, 0))).times(new Complex(Math.toDegrees(radians)))
						.divide(new Complex(Math.toDegrees(radians)).magnitude())
						.toVector2d()
				, radians);
	}

	@Override
	public void StrafeTo(Vector2d pose) {
		Complex cache = new Complex(this.pose.position.minus(pose));
		MEAN = new commandRunningNode() {
			@Override
			public void runCommand() {
				classic.SimpleRadiansDrive(BufPower, Math.toRadians(cache.toDegree()));
			}
		};
		DeltaTrajectory = new Pose2d(cache.toVector2d(), this.pose.heading);
	}

	@Override
	public void RUN() {
		MEAN.runCommand();
	}

	@Override
	public Pose2d getDeltaTrajectory() {
		return DeltaTrajectory;
	}

	@Override
	@NonNull
	public Pose2d NEXT() {
		return new Pose2d(
				pose.position.x + DeltaTrajectory.position.x,
				pose.position.y + DeltaTrajectory.position.y,
				pose.heading.toDouble() + DeltaTrajectory.heading.toDouble()
		);
	}

	@Override
	public Pose2d getPose() {
		return pose;
	}

	@Override
	public TrajectoryType getState() {
		return trajectoryType;
	}
}

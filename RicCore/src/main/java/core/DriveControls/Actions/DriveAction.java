package core.DriveControls.Actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import core.DriveControls.MecanumDrive;
import core.DriveControls.OrderDefinition.DriveOrder;
import core.DriveControls.OrderDefinition.DriveOrderPackage;
import core.Utils.*;

public class DriveAction implements DriveOrder {
	private final Hardwares.Classic classic;

	/**
	 * 为了简化代码书写，我们使用了<code>@Override</code>的覆写来保存数据。
	 * <p>如果使用enum，则代码会明显过于臃肿</p>
	 */
	public abstract static class actionRunningNode implements Action {}
	public actionRunningNode MEAN;
	public double BufPower;
	public Pose2d DeltaTrajectory;
	public final Pose2d pose;
	/**
	 * <code>面向开发者：</code> 不建议在DriveCommands中更改trajectoryType的值，而是在drivingCommandsBuilder中
	 */
	public Utils.Enums.TrajectoryType trajectoryType = null;

	DriveAction(final Hardwares.Classic classic, double BufPower, Pose2d pose) {
		this.BufPower = BufPower;
		this.pose = pose;
		this.classic = classic;
	}

	@Override
	public void SetPower(double power) {
		MEAN=new actionRunningNode() {
			@Override
			public boolean run(@NonNull TelemetryPacket telemetryPacket) {
				BufPower=power;
				BufPower= Mathematics.intervalClip(BufPower,-1,1);
				return false;
			}
		};
	}

	@Override
	public void Turn(double radians) {
		MEAN=new actionRunningNode() {
			@Override
			public boolean run(@NonNull TelemetryPacket telemetryPacket) {
				classic.drive(driveDirection.turn, BufPower);
				return false;
			}
		};
		DeltaTrajectory = new Pose2d(0, 0, radians);
	}

	@Override
	public void StrafeInDistance(double radians, double distance) {
		MEAN=new actionRunningNode() {
			@Override
			public boolean run(@NonNull TelemetryPacket telemetryPacket) {
				classic.SimpleRadiansDrive(BufPower, radians);
				return false;
			}
		};
		DeltaTrajectory = new Pose2d(
				(new Complex(new Vector2d(distance, 0))).times(new Complex(Math.toDegrees(radians)))
						.divide(new Complex(Math.toDegrees(radians)).magnitude())
						.toVector2d()
				, radians
		);
	}

	@Override
	public void StrafeTo(Vector2d pose) {
		Utils.Complex cache = new Utils.Complex(this.pose.position.minus(pose));
		MEAN=new actionRunningNode() {
			@Override
			public boolean run(@NonNull TelemetryPacket telemetryPacket) {
				classic.SimpleRadiansDrive(BufPower, Math.toRadians(cache.toDegree()));
				return false;
			}
		};
		DeltaTrajectory = new Pose2d(cache.toVector2d(), this.pose.heading);
	}

	@Override
	public void RUN() {
		Actions.runBlocking(MEAN);
	}

	@Override
	public Pose2d getDeltaTrajectory() {
		return DeltaTrajectory;
	}

	@NonNull
	@Override
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

	@ExtractedInterfaces
	public Action AsAction(MecanumDrive drive){
		return new InstantAction(() -> drive.runOrderPackage((DriveOrderPackage) this));
	}
}
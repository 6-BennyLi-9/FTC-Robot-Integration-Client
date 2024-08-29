package org.firstinspires.ftc.teamcode.DriveControls.Commands;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.utils.Complex;
import org.firstinspires.ftc.teamcode.utils.Enums.TrajectoryType;
import org.firstinspires.ftc.teamcode.utils.Enums.driveDirection;
import org.firstinspires.ftc.teamcode.utils.Mathematics;

public class DriveCommand {
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

	DriveCommand(final Classic classic, double BufPower, Pose2d pose) {
		this.BufPower = BufPower;
		this.pose = pose;
		this.classic = classic;
	}

	/**
	 * 在该节点只修改电机BufPower，不会在定义时影响主程序
	 *
	 * @param power 目标设置的电机BufPower
	 */
	public void SetPower(double power) {
		MEAN = new commandRunningNode() {
			@Override
			public void runCommand() {
				BufPower = power;
				BufPower = Mathematics.intervalClip(BufPower, -1f, 1f);
			}
		};
	}

	/**
	 * 在该节点让机器旋转指定弧度
	 *
	 * @param radians 要转的弧度
	 */
	public void Turn(double radians) {
		MEAN = new commandRunningNode() {
			@Override
			public void runCommand() {
				classic.drive(driveDirection.turn, BufPower);
			}
		};
		DeltaTrajectory = new Pose2d(new Vector2d(0, 0), radians);
	}

	/**
	 * 在该节点让机器在指定角度行驶指定距离
	 *
	 * @param radians  相较于机器的正方向，目标点位的度数（注意不是相较于当前机器方向，而是坐标系定义时给出的机器正方向）
	 * @param distance 要行驶的距离
	 */
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

	/**
	 * 在该节点让机器在不旋转的情况下平移
	 *
	 * @param pose 目标矢量点位
	 */
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

	/**
	 * 不要在自动程序中调用这个函数，否则你会后悔的
	 */
	public void RUN() {
		MEAN.runCommand();
	}

	public Pose2d getDeltaTrajectory() {
		return DeltaTrajectory;
	}

	/**
	 * @return 该Command节点的目标点位
	 */
	@NonNull
	public Pose2d NEXT() {
		return new Pose2d(
				pose.position.x + DeltaTrajectory.position.x,
				pose.position.y + DeltaTrajectory.position.y,
				pose.heading.toDouble() + DeltaTrajectory.heading.toDouble()
		);
	}
}

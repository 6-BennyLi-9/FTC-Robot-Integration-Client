package org.firstinspires.ftc.teamcode.DriveControls;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.utils.Enums.TrajectoryType;
import org.firstinspires.ftc.teamcode.utils.Mathematics;

public class drivingCommandsBuilder {
	private final DriveCommandPackage commandPackage;
	private final SimpleMecanumDrive drive;
	private DriveCommand cache;

	drivingCommandsBuilder(@NonNull SimpleMecanumDrive drive) {
		commandPackage = new DriveCommandPackage();
		commandPackage.commands.add(new DriveCommand(drive.classic, drive.BufPower, drive.poseHistory.getLast()));
		this.drive = drive;
	}

	drivingCommandsBuilder(SimpleMecanumDrive drive, DriveCommandPackage commandPackage) {
		this.commandPackage = commandPackage;
		this.drive = drive;
	}

	/**
	 * 在该节点只修改电机BufPower，不会在定义时影响主程序
	 *
	 * @param power 目标设置的电机BufPower
	 */
	public drivingCommandsBuilder SetPower(double power) {
		power = Mathematics.intervalClip(power, -1f, 1f);
		cache = new DriveCommand(drive.classic, commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().NEXT());
		cache.SetPower(power);
		cache.trajectoryType = TrajectoryType.WithoutChangingPosition;
		commandPackage.commands.add(cache);
		return new drivingCommandsBuilder(drive, commandPackage);
	}

	/**
	 * 在该节点让机器旋转指定弧度
	 *
	 * @param radians 要转的弧度[-PI,PI)
	 */
	public drivingCommandsBuilder TurnRadians(double radians) {
		radians = Mathematics.intervalClip(radians, -Math.PI, Math.PI);
		cache = new DriveCommand(drive.classic, commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().NEXT());
		cache.Turn(radians);
		cache.trajectoryType = TrajectoryType.TurnOnly;
		commandPackage.commands.add(cache);
		return new drivingCommandsBuilder(drive, commandPackage);
	}

	/**
	 * 在该节点让机器旋转指定角度
	 *
	 * @param deg 要转的角度[-180,180)
	 */
	public drivingCommandsBuilder TurnAngle(double deg) {
		return TurnRadians(Math.toRadians(deg));
	}

	/**
	 * 在该节点让机器在指定角度行驶指定距离
	 *
	 * @param radians  相较于机器的正方向，目标点位的度数（注意不是相较于当前机器方向，而是坐标系定义时给出的机器正方向）
	 * @param distance 要行驶的距离
	 */
	public drivingCommandsBuilder StrafeInDistance(double radians, double distance) {
		cache = new DriveCommand(drive.classic, commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().NEXT());
		cache.StrafeInDistance(radians, distance);
		cache.trajectoryType = TrajectoryType.LinerStrafe;
		commandPackage.commands.add(cache);
		return new drivingCommandsBuilder(drive, commandPackage);
	}

	/**
	 * 在该节点让机器在不旋转的情况下平移
	 *
	 * @param pose 目标矢量点位
	 */
	public drivingCommandsBuilder StrafeTo(Vector2d pose) {
		cache = new DriveCommand(drive.classic, commandPackage.commands.getLast().BufPower, commandPackage.commands.getLast().NEXT());
		cache.StrafeTo(pose);
		cache.trajectoryType = TrajectoryType.LinerStrafe;
		commandPackage.commands.add(cache);
		return new drivingCommandsBuilder(drive, commandPackage);
	}

	/**
	 * 结束该DriveCommandPackage
	 */
	public DriveCommandPackage END() {
		return commandPackage;
	}
}

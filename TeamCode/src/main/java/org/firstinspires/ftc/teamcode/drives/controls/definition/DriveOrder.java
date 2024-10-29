package org.firstinspires.ftc.teamcode.drives.controls.definition;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.drives.controls.TrajectoryType;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.Vector2d;

public interface DriveOrder {
	/**
	 * 在该节点只修改电机BufPower，不会在定义时影响主程序
	 * @param power 目标设置的电机BufPower
	 */
	void setPower(double power);
	/**
	 * 在该节点让机器旋转指定弧度
	 * @param radians 要转的弧度
	 */
	void turn(double radians);
	/**
	 * 在该节点让机器在指定角度行驶指定距离
	 *
	 * @param radians  相较于机器的正方向，目标点位的度数（注意不是相较于当前机器方向，而是坐标系定义时给出的机器正方向）
	 * @param distance 要行驶的距离
	 */
	void strafeInDistance(double radians, double distance);
	/**
	 * 在该节点让机器在不旋转的情况下平移
	 *
	 * @param pose 目标矢量点位
	 */
	void strafeTo(Vector2d pose);
	/**
	 * 不要在自动程序中调用这个函数，否则你会后悔的
	 */
	void run();
	Position2d getDeltaTrajectory();
	/**
	 * @return 该Command节点的目标点位
	 */
	@NonNull
	Position2d nextPose();

	Position2d getPose();
	TrajectoryType getState();
}

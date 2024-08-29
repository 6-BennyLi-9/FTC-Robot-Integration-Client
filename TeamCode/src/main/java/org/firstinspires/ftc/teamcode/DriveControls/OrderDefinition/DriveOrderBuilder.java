package org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition;

import com.acmerobotics.roadrunner.Vector2d;

public interface DriveOrderBuilder {
	/**
	 * 在该节点只修改电机BufPower，不会在定义时影响主程序
	 * @param power 目标设置的电机BufPower
	 */
	DriveOrderBuilder SetPower(double power);
	/**
	 * 在该节点让机器旋转指定弧度
	 * @param radians 要转的弧度[-PI,PI)
	 */
	DriveOrderBuilder TurnRadians(double radians);
	/**
	 * 在该节点让机器旋转指定角度
	 * @param deg 要转的角度[-180,180)
	 */
	DriveOrderBuilder TurnAngle(double deg);
	/**
	 * 在该节点让机器在指定角度行驶指定距离
	 * @param radians  相较于机器的正方向，目标点位的度数（注意不是相较于当前机器方向，而是坐标系定义时给出的机器正方向）
	 * @param distance 要行驶的距离
	 */
	DriveOrderBuilder StrafeInDistance(double radians, double distance);
	/**
	 * 在该节点让机器在不旋转的情况下平移
	 * @param pose 目标矢量点位
	 */
	DriveOrderBuilder StrafeTo(Vector2d pose);
	/**
	 * 结束该 DriveOrderBuilder
	 */
	DriveOrderPackage END();
}

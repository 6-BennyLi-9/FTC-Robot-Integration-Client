package org.firstinspires.ftc.teamcode.DriveControlsAddition;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.DualNum;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.Twist2dDual;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.Vector2dDual;

import org.firstinspires.ftc.teamcode.DriveControls.Localizer;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.utils.Complex;

/**
 * 该Localizer无法与roadrunner中的Localize兼容，我们可能会在后续对其进行优化
 */
public final class ImuLocalizer implements Localizer {
	public static class Params{
		/**
		 * IMU相较于机器的正中心在X轴上的偏差
		 * @see org.firstinspires.ftc.teamcode.RIC_tuning.IMUPositionTuner
		 */
		public static double X_error=0;
		/**
		 * IMU相较于机器的正中心在Y轴上的偏差
		 * @see org.firstinspires.ftc.teamcode.RIC_tuning.IMUPositionTuner
		 */
		public static double Y_error=0;
	}
	Sensors sensors;
	Complex error;

	ImuLocalizer(Sensors sensors){
		this.sensors=sensors;
		error=new Complex(new Vector2d(Params.X_error,Params.Y_error));
	}

	private boolean initialized=false;
	private Pose2d lastPose;
	
	/**
	 * @return 返回参照RoadRunner的Localizer的格式，参数第一个为Delta量，第二个为实际位置。由于官方写得实在是太抽象了，而且
	 * 一点注释也不写，这是我们自己摸索出来的最好的结果
	 */
	@NonNull
	public Twist2dDual<Time> update() {
		if(!initialized){
			initialized=true;
			sensors.update();

			lastPose=new Pose2d(sensors.XMoved, sensors.YMoved,Math.toRadians(sensors.FirstAngle));
			return new Twist2dDual<>(
					Vector2dDual.constant(new Vector2d(0.0, 0.0), 2),
					DualNum.constant(0.0, 2)
			);
		}
		sensors.update();
		Pose2d pose=new Pose2d(sensors.XMoved,sensors.YMoved,Math.toRadians(sensors.FirstAngle));
		Complex complex=new Complex(pose.position);
		Complex angle=new Complex(Math.toDegrees(pose.heading.toDouble()));
		complex=complex.minus(error.times(angle).divide(angle.magnitude()));
		pose=new Pose2d(complex.toVector2d(),pose.heading);
		Pose2d delta=new Pose2d(
				pose.position.x-lastPose.position.x,
				pose.position.y-lastPose.position.y,
				pose.heading.toDouble()-lastPose.heading.toDouble()
		);

		Twist2dDual<Time> res=new Twist2dDual<>(
				new Vector2dDual<>(
						new DualNum<>(new double[]{
								delta.position.x,
								pose.position.x
						}), new DualNum<>(new double[]{
							delta.position.y,
							pose.position.y
						})
				),
				new DualNum<>(new double[]{
						delta.heading.toDouble(),
						pose.heading.toDouble()
				})
		);
		lastPose=pose;
		return res;
	}
}

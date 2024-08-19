package org.firstinspires.ftc.teamcode.DriveControls.Localizers;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition.PositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.utils.Complex;

/**
 * 该Localizer无法与roadrunner中的Localize兼容，我们可能会在后续对其进行优化
 */
public class ImuLocalizer implements PositionLocalizerPlugin {
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

	public ImuLocalizer(Sensors sensors){
		this.sensors=sensors;
		error=new Complex(new Vector2d(Params.X_error,Params.Y_error));
	}

	private boolean initialized=false;

	protected Pose2d pose;

	public void update() {
		if(!initialized){
			initialized=true;
			sensors.update();

			pose=new Pose2d(0,0,0);
			return;
		}
		sensors.update();
		pose=new Pose2d(sensors.XMoved,sensors.YMoved,Math.toRadians(sensors.FirstAngle));
		Complex complex=new Complex(pose.position);
		Complex angle=new Complex(Math.toDegrees(pose.heading.toDouble()));
		complex=complex.minus(error.times(angle).divide(angle.magnitude()));
		pose=new Pose2d(complex.toVector2d(),pose.heading);
	}/**
	 * @return 返回我们重写的Localizer的格式，返回参数为实际位置。
	 */
	@Override
	public Pose2d getCurrentPose() {
		return pose;
	}
}

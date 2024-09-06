package DriveControls.Localizers.LocalizerPlugins;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import DriveControls.Localizers.LocalizerDefinition.PositionLocalizerPlugin;
import org.firstinspires.ftc.teamcode.Hardwares.Classic;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Sensors;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Utils.Annotations.LocalizationPlugin;
import org.firstinspires.ftc.teamcode.Utils.Complex;

@LocalizationPlugin
public class ImuLocalizer implements PositionLocalizerPlugin {
	public Sensors sensors;
	public Complex error;

	public ImuLocalizer(@NonNull Classic classic){
		this.sensors=classic.sensors;
		error=new Complex(new Vector2d(Params.X_error, Params.Y_error));
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
	}
	/**
	 * @return 返回我们重写的Localizer的格式，返回参数为实际位置。
	 */
	@Override
	public Pose2d getCurrentPose() {
		return pose;
	}
}

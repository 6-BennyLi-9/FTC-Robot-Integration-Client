package core.DriveControls.Localizers.LocalizerPlugins;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.Vector2d;

import core.DriveControls.Localizers.LocalizerDefinition.VectorPositionLocalizerPlugin;
import core.Hardwares.Classic;
import core.Hardwares.basic.Sensors;
import core.Params;
import core.Utils.Annotations.LocalizationPlugin;

@LocalizationPlugin
public final class DeadWheelVectorPositionLocalizer implements VectorPositionLocalizerPlugin {
	public Sensors sensors;
	public Vector2d RobotPosition;
	public double HeadingRad;

	public DeadWheelVectorPositionLocalizer(@NonNull Classic classic){
		sensors=classic.sensors;
		RobotPosition=new Vector2d(0,0);
	}
	@Override
	public Vector2d getCurrentVector() {
		return RobotPosition;
	}

	@Override
	public void SetHeadingRad(double Rad){
		HeadingRad= Rad;
	}

	@Override
	public void update() {
		sensors.update();
		Vector2d delta=getDelta();
		if(delta==null)return;
		//处理前进
		RobotPosition=new Vector2d(
				Math.sin(HeadingRad)*delta.y+RobotPosition.x,
				Math.cos(HeadingRad)*delta.y+RobotPosition.y
		);
		//处理平移
		RobotPosition=new Vector2d(
				Math.sin(HeadingRad-Math.PI/2)*delta.x+RobotPosition.x,
				Math.cos(HeadingRad-Math.PI/2)*delta.x+RobotPosition.y
		);
	}
	@Nullable
	public Vector2d getDelta(){
		switch (sensors.DeadWheelType) {
			case BE_NOT_USING_DEAD_WHEELS:
				return null;
			case TwoDeadWheels:
				return new Vector2d(
								0,
								sensors.getDeltaA()* Params.AxialInchPerTick
						);
			case ThreeDeadWheels:
				return new Vector2d(
						sensors.getDeltaL()* Params.LateralInchPerTick,
						sensors.getDeltaA()* Params.AxialInchPerTick
				);
		}
		return null;
	}
}

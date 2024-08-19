package org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.utils.enums.LocalizerSubassemblyType;

public abstract class LocalizerSubassembly {
	public final LocalizerSubassemblyType type;
	public final LocalizerPlugin[] plugins;
	public Pose2d RobotPosition;

	public LocalizerSubassembly(PositionLocalizerPlugin localizerPlugin){
		plugins=new LocalizerPlugin[]{localizerPlugin};
		type=LocalizerSubassemblyType.SingleLocalizer;
	}
	public LocalizerSubassembly(VectorPositionLocalizerPlugin localizerPlugin1,HeadingLocalizerPlugin localizerPlugin2){
		plugins=new LocalizerPlugin[]{localizerPlugin1,localizerPlugin2};
		type=LocalizerSubassemblyType.SubassemblyLocalizer;
	}

	public void update(){
		switch (type) {
			case SingleLocalizer:
				plugins[0].update();
				RobotPosition=((PositionLocalizerPlugin) plugins[0]).getCurrentPose();
				break;
			case SubassemblyLocalizer:
				plugins[0].update();
				plugins[1].update();
				RobotPosition=new Pose2d(
						((VectorPositionLocalizerPlugin) plugins[0]).getCurrentVector(),
						((HeadingLocalizerPlugin) plugins[1]).getHeadingDeg()
				);
				break;
		}
	}
}

package org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.utils.enums.LocalizerSubassemblyType;

public abstract class SubassemblyLocalizer implements Localizer{
	public final LocalizerSubassemblyType type;
	public final LocalizerPlugin[] plugins;
	public Pose2d RobotPosition;

	public SubassemblyLocalizer(PositionLocalizerPlugin localizerPlugin){
		plugins=new LocalizerPlugin[]{localizerPlugin};
		type=LocalizerSubassemblyType.SingleLocalizer;
	}
	public SubassemblyLocalizer(VectorPositionLocalizerPlugin localizerPlugin1, HeadingLocalizerPlugin localizerPlugin2){
		plugins=new LocalizerPlugin[]{localizerPlugin1,localizerPlugin2};
		type=LocalizerSubassemblyType.SubassemblyLocalizer;
	}
	public SubassemblyLocalizer(@NonNull LocalizerPlugin[] localizerClasses){
		type=LocalizerSubassemblyType.SynthesisLocalizer;
		plugins=localizerClasses;
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
			case SynthesisLocalizer:
				Pose2d cache=new Pose2d(0,0,0);
				for(LocalizerPlugin plugin:plugins){
					cache=new Pose2d(
							cache.position.x+((PositionLocalizerPlugin)plugin).getCurrentPose().position.x,
							cache.position.y+((PositionLocalizerPlugin)plugin).getCurrentPose().position.y,
							cache.heading.toDouble()+((PositionLocalizerPlugin)plugin).getCurrentPose().heading.toDouble()
					);
				}
				cache=new Pose2d(
						cache.position.x/plugins.length,
						cache.position.y/plugins.length,
						cache.heading.toDouble()/plugins.length
				);
				RobotPosition=cache;
		}
	}
}

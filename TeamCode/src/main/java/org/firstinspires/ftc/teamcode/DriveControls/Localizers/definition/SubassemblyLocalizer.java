package org.firstinspires.ftc.teamcode.DriveControls.Localizers.definition;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

public abstract class SubassemblyLocalizer implements Localizer{
	public final LocalizerPlugin[] plugins;
	public Pose2d RobotPosition;

	public SubassemblyLocalizer(PositionLocalizerPlugin localizerPlugin){
		plugins=new LocalizerPlugin[]{localizerPlugin};
	}
	public SubassemblyLocalizer(VectorPositionLocalizerPlugin localizerPlugin1, HeadingLocalizerPlugin localizerPlugin2){
		plugins=new LocalizerPlugin[]{localizerPlugin1,localizerPlugin2};
	}
	public SubassemblyLocalizer(@NonNull LocalizerPlugin[] localizerClasses){
		plugins=localizerClasses;
	}

	public void update(){
		Pose2d cache=new Pose2d(0,0,0);
		int VectorFactor=0,HeadingFactor=0;
		for(LocalizerPlugin plugin:plugins){
			plugin.update();
			if(plugin instanceof VectorPositionLocalizerPlugin){
				cache=new Pose2d(
						cache.position.x+((VectorPositionLocalizerPlugin)plugin).getCurrentVector().x,
						cache.position.y+((VectorPositionLocalizerPlugin)plugin).getCurrentVector().y,
						cache.heading.toDouble()
				);
				++VectorFactor;
			}else if(plugin instanceof HeadingLocalizerPlugin){
				cache=new Pose2d(
						cache.position,
						cache.heading.toDouble()+Math.toRadians(((HeadingLocalizerPlugin)plugin).getHeadingDeg())
				);
				++HeadingFactor;
			}else if(plugin instanceof PositionLocalizerPlugin){
				cache=new Pose2d(
						cache.position.x+((PositionLocalizerPlugin)plugin).getCurrentPose().position.x,
						cache.position.y+((PositionLocalizerPlugin)plugin).getCurrentPose().position.y,
						cache.heading.toDouble()+((PositionLocalizerPlugin)plugin).getCurrentPose().heading.toDouble()
				);
				++VectorFactor;
				++HeadingFactor;
			}else{
				throw new RuntimeException("unknown localizer plugin");
			}
		}
		RobotPosition=new Pose2d(
				cache.position.x/VectorFactor,
				cache.position.y/VectorFactor,
				cache.heading.toDouble()/HeadingFactor
		);
	}
}

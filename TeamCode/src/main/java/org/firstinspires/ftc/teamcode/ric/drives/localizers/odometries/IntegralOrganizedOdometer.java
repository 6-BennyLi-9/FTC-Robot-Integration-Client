package org.firstinspires.ftc.teamcode.ric.drives.localizers.odometries;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.ric.drives.localizers.mathematics.ConstantAccelMath;
import org.firstinspires.ftc.teamcode.ric.utils.annotations.OdometerPrograms;
import org.firstinspires.ftc.teamcode.ric.utils.clients.Client;
import org.firstinspires.ftc.teamcode.ric.utils.Functions;
import org.firstinspires.ftc.teamcode.ric.utils.Position2d;
import org.firstinspires.ftc.teamcode.ric.utils.Timer;

import java.util.Vector;

@OdometerPrograms
public class IntegralOrganizedOdometer extends ClassicOdometer implements Odometry{
	public double distanceTraveled=0;
	protected ConstantAccelMath processor;
	protected Timer timer;
	protected Vector<Pose2d> relHistory;

	public IntegralOrganizedOdometer(Client client) {
		super();
		timer=new Timer();
		timer.stopAndRestart();

		processor=new ConstantAccelMath();
		relHistory=new Vector<>();
		relHistory.add(new Pose2d(0,0,0));
		timer.pushMileageTimeTag("updateTime");
	}

	@Override
	public void update(double relDeltaX, double relDeltaY, double relDeltaTheta) {
		double loopTime=timer.restartAndGetDeltaTime();

		distanceTraveled += Functions.distance(relDeltaX, relDeltaY);
		Position2d relDelta=new Position2d(relDeltaX,relDeltaY,relDeltaTheta);
		Position2d curr=new Position2d(LastPose());
		processor.calculate(loopTime,relDelta,curr);

		AddDelta(curr.x,curr.y,curr.heading);

		relHistory.add(0,relDelta.asPose2d());
		timer.pushMileageTimeTag("updateTime");

		updateVelocity();
	}

	Position2d relCurrentVel,currentVel;

	public void updateVelocity(){
		double targetVelTimeEstimate=0.2;
		double actualVelTime=0,relDeltaXTotal=0,relDeltaYTotal=0,totalTime;
		int lastIndex=0;
		Vector<Double> times=timer.getMileageTimeTag("updateTime");
		double startTime=times.isEmpty()? 0:times.lastElement();

		for(int i=relHistory.size()-1;i>=0;--i){
			totalTime= startTime - times.get(i);
			if(totalTime<=targetVelTimeEstimate){
				actualVelTime=totalTime;
				relDeltaXTotal+=relHistory.get(i).position.x;
				relDeltaYTotal+=relHistory.get(i).position.y;
				lastIndex=i;
			}
		}

		if(actualVelTime!=0){
			relCurrentVel =new Position2d(
				relDeltaXTotal/actualVelTime,
					relDeltaYTotal/actualVelTime,
					LastPose().heading.toDouble()-relHistory.get(lastIndex).heading.toDouble()/actualVelTime
			);
			currentVel=new Position2d(Functions.Alignment2d(relCurrentVel));
		}

		//pop
		while (lastIndex>=0){
			relHistory.remove(0);
			--lastIndex;
		}
	}
}

package org.firstinspires.ftc.teamcode.drives.localizers.odometries;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.drives.localizers.mathematics.ConstantAccelMath;
import org.firstinspires.ftc.teamcode.utils.annotations.OdometerPrograms;
import org.firstinspires.ftc.teamcode.utils.clients.Client;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.SimplePosition;
import org.firstinspires.ftc.teamcode.utils.Timer;

import java.util.Vector;

@OdometerPrograms
public class IntegralOrganizedOdometer extends ClassicOdometer implements Odometry{
	public double distanceTraveled=0;
	protected ConstantAccelMath processor;
	protected Timer timer;
	protected Vector<Pose2d> relHistory;

	public IntegralOrganizedOdometer(Client client) {
		super(client);
		timer=new Timer();
		timer.stopAndRestart();
	}

	@Override
	public void update(double relDeltaX, double relDeltaY, double relDeltaTheta) {
		double loopTime=timer.restartAndGetDeltaTime();

		distanceTraveled += Functions.distance(relDeltaX, relDeltaY);
		SimplePosition relDelta=new SimplePosition(relDeltaX,relDeltaY,relDeltaTheta);
		SimplePosition curr=new SimplePosition(LastPose());
		processor.calculate(loopTime,relDelta,curr);

		AddDelta(curr.x,curr.y,curr.heading);

		relHistory.add(0,relDelta.asPose2d());
		timer.pushMileageTimeTag("updateTime");

		updateVelocity();
	}

	SimplePosition relCurrentVel,currentVel;

	public void updateVelocity(){
		double targetVelTimeEstimate=0.2;
		double actualVelTime=0,relDeltaXTotal=0,relDeltaYTotal=0,totalTime;
		int lastIndex=0;
		Vector<Double> times=timer.getMileageTimeTag("updateTime");
		double startTime=times.isEmpty()? 0:times.lastElement();

		for(int i=times.size()-1;i>=0;--i){
			totalTime= startTime - times.get(i);
			if(totalTime<=targetVelTimeEstimate){
				actualVelTime=totalTime;
				relDeltaXTotal+=relHistory.get(i).position.x;
				relDeltaYTotal+=relHistory.get(i).position.y;
				lastIndex=i;
			}
		}

		if(actualVelTime!=0){
			relCurrentVel =new SimplePosition(
				relDeltaXTotal/actualVelTime,
					relDeltaYTotal/actualVelTime,
					LastPose().heading.toDouble()-relHistory.get(lastIndex).heading.toDouble()/actualVelTime
			);
			currentVel=new SimplePosition(Functions.Alignment2d(relCurrentVel));
		}

		//pop
		while (lastIndex>=0){
			relHistory.remove(0);
			--lastIndex;
		}
	}
}

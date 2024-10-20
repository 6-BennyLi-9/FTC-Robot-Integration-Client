package org.firstinspires.ftc.teamcode.drives.localizers.odometries;

import org.firstinspires.ftc.teamcode.drives.localizers.mathematics.ConstantAccelMath;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.Timer;
import org.firstinspires.ftc.teamcode.utils.annotations.OdometerPrograms;
import org.firstinspires.ftc.teamcode.utils.clients.Client;

import java.util.Vector;

@OdometerPrograms
public class IntegralOrganizedOdometer extends ClassicOdometer implements Odometry{
	public double distanceTraveled=0;
	protected ConstantAccelMath processor;
	protected Timer timer;
	protected Vector<Position2d> relHistory;

	public IntegralOrganizedOdometer(Client client) {
		super();
		timer=new Timer();
		timer.stopAndRestart();

		processor=new ConstantAccelMath();
		relHistory=new Vector<>();
		relHistory.add(new Position2d(0,0,0));
		timer.pushMileageTimeTag("updateTime");
	}

	@Override
	public void update(double relDeltaX, double relDeltaY, double relDeltaTheta) {
		double loopTime=timer.restartAndGetDeltaTime();

		distanceTraveled += Functions.distance(relDeltaX, relDeltaY);
		Position2d relDelta=new Position2d(relDeltaX,relDeltaY,relDeltaTheta);
		Position2d curr=LastPose();
		processor.calculate(loopTime,relDelta,curr);

		AddDelta(curr.x,curr.y,curr.heading);

		relHistory.add(0,relDelta);
		timer.pushMileageTimeTag("updateTime");

		updateVelocity();
	}

	@Override
	public void registerToDashBoard(String tag) {
		super.registerToDashBoard(tag);
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
				relDeltaXTotal+=relHistory.get(i).x;
				relDeltaYTotal+=relHistory.get(i).y;
				lastIndex=i;
			}
		}

		if(actualVelTime!=0){
			relCurrentVel =new Position2d(
				relDeltaXTotal/actualVelTime,
					relDeltaYTotal/actualVelTime,
					LastPose().heading-relHistory.get(lastIndex).heading/actualVelTime
			);
			currentVel=Functions.Alignment2d(relCurrentVel);
		}

		//pop
		while (lastIndex>=0){
			relHistory.remove(0);
			--lastIndex;
		}
	}
}

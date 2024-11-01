package org.firstinspires.ftc.teamcode.drives.localizers.odometries;

import org.firstinspires.ftc.teamcode.drives.localizers.mathematics.ConstantAccelMath;
import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.Timer;
import org.firstinspires.ftc.teamcode.utils.annotations.OdometerPrograms;

import java.util.Vector;

@OdometerPrograms
public class IntegralOrganizedOdometer extends ClassicOdometer {
	public double distanceTraveled;
	protected ConstantAccelMath processor;
	protected Timer timer;
	protected Vector<Position2d> relHistory;

	public IntegralOrganizedOdometer() {
		this.timer =new Timer();
		this.timer.stopAndRestart();

		this.processor =new ConstantAccelMath();
		this.relHistory =new Vector<>();
		this.relHistory.add(new Position2d(0,0,0));
		this.timer.pushMileageTimeTag("updateTime");
	}

	@Override
	public void update(final double relDeltaX, final double relDeltaY, final double relDeltaTheta) {
		final double loopTime = this.timer.restartAndGetDeltaTime();

		this.distanceTraveled += Functions.distance(relDeltaX, relDeltaY);
		final Position2d relDelta =new Position2d(relDeltaX,relDeltaY,relDeltaTheta);
		final Position2d curr     = this.LastPose();
		this.processor.calculate(loopTime,relDelta,curr);

		this.AddDelta(curr.x,curr.y,curr.heading);

		this.relHistory.add(0,relDelta);
		this.timer.pushMileageTimeTag("updateTime");

		this.updateVelocity();
	}

	Position2d relCurrentVel,currentVel;

	public void updateVelocity(){
		final double targetVelTimeEstimate =0.2;
		double       actualVelTime         =0, relDeltaXTotal =0, relDeltaYTotal =0, totalTime;
		int                  lastIndex =0;
		final Vector<Double> times     = this.timer.getMileageTimeTag("updateTime");
		final double         startTime = times.isEmpty() ? 0 : times.lastElement();

		for(int i = this.relHistory.size() - 1 ; 0 <= i ; --i){
			totalTime= startTime - times.get(i);
			if(totalTime<=targetVelTimeEstimate){
				actualVelTime=totalTime;
				relDeltaXTotal+= this.relHistory.get(i).x;
				relDeltaYTotal+= this.relHistory.get(i).y;
				lastIndex=i;
			}
		}

		if(0 != actualVelTime){
			this.relCurrentVel =new Position2d(
				relDeltaXTotal/actualVelTime,
					relDeltaYTotal/actualVelTime, this.LastPose().heading - this.relHistory.get(lastIndex).heading / actualVelTime
			);
			this.currentVel =Functions.Alignment2d(this.relCurrentVel);
		}

		//pop
		while (0 <= lastIndex){
			this.relHistory.remove(0);
			--lastIndex;
		}
	}
}

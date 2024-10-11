package org.firstinspires.ftc.teamcode.drives.localizers.odometries;

import org.firstinspires.ftc.teamcode.utils.annotations.OdometerPrograms;
import org.firstinspires.ftc.teamcode.utils.clients.Client;

@OdometerPrograms
public class ArcOrganizedOdometer extends ClassicOdometer implements Odometry{
	public ArcOrganizedOdometer(Client client) {
		super(client);
	}

	@Override
	public void update(double relDeltaX, double relDeltaY, double relDeltaTheta) {
		double REL_X=GetArcRelX(relDeltaX, relDeltaY, relDeltaTheta);
		double REL_Y=GetArcRelY(relDeltaX, relDeltaY, relDeltaTheta);
		AddUnAlignmentDelta(REL_X,REL_Y, relDeltaTheta);
	}

	/**
	 * @param relTheta 角度制
	 */
	protected double GetArcRelX(double relX,double relY,double relTheta){
		double r0=relX/relTheta,r1=relY/relTheta;
		return r0*Math.sin(Math.toRadians(relTheta))-r1*(1-Math.cos(Math.toRadians(relTheta)));
	}
	/**
	 * @param relTheta 角度制
	 */
	protected double GetArcRelY(double relX,double relY,double relTheta){
		double r0=relX/relTheta,r1=relY/relTheta;
		return r1*Math.sin(Math.toRadians(relTheta))+r0*(1-Math.cos(Math.toRadians(relTheta)));
	}
}

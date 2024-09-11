package org.firstinspires.ftc.teamcode.DriveControls.Odometry;

import org.firstinspires.ftc.teamcode.Utils.Annotations.OdometerPrograms;
import org.firstinspires.ftc.teamcode.Utils.Clients.Client;

@OdometerPrograms
public class ArcOrganizedOdometer extends ClassicOdometer{
	ArcOrganizedOdometer(Client client) {
		super(client);
	}

	@Override
	public void ProcessDeltaRelPose(double relX, double relY, double relTheta) {
		double REL_X=GetArcRelX(relX,relY,relTheta);
		double REL_Y=GetArcRelY(relX,relY,relTheta);
		AddDelta(
				REL_X*Math.cos(Math.toRadians(relTheta))-REL_Y*Math.sin(Math.toRadians(relTheta)),
				REL_Y*Math.cos(Math.toRadians(relTheta))+REL_X*Math.sin(Math.toRadians(relTheta)),
				relTheta
		);
	}

	protected double GetArcRelX(double relX,double relY,double relTheta){
		double r0=relX/relTheta,r1=relY/relTheta;
		return r0*Math.sin(Math.toRadians(relTheta))-r1*(1-Math.cos(Math.toRadians(relTheta)));
	}
	protected double GetArcRelY(double relX,double relY,double relTheta){
		double r0=relX/relTheta,r1=relY/relTheta;
		return r1*Math.sin(Math.toRadians(relTheta))+r0*(1-Math.cos(Math.toRadians(relTheta)));
	}
}

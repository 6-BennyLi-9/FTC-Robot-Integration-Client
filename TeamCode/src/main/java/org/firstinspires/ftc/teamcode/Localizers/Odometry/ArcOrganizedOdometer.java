package org.firstinspires.ftc.teamcode.Localizers.Odometry;

import org.firstinspires.ftc.teamcode.Utils.Annotations.OdometerPrograms;
import org.firstinspires.ftc.teamcode.Utils.Clients.Client;
import org.firstinspires.ftc.teamcode.Utils.Functions;

@OdometerPrograms
public class ArcOrganizedOdometer extends ClassicOdometer implements Odometry{
	public ArcOrganizedOdometer(Client client) {
		super(client);
	}

	@Override
	public void ProcessDeltaRelPose(double relX, double relY, double relTheta) {
		double REL_X=GetArcRelX(relX,relY,relTheta);
		double REL_Y=GetArcRelY(relX,relY,relTheta);
		AddDelta(Functions.Alignment2d(REL_X,REL_Y,relTheta));
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

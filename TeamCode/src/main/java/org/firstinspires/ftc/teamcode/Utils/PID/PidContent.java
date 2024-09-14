package org.firstinspires.ftc.teamcode.Utils.PID;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.Utils.Timer;

@UtilFunctions
public class PidContent {
	public final String Tag;
	public final double vP,vI,vD,MAX_I;
	public double P,I,D;
	public double inaccuracies,lastInaccuracies,fulfillment;
	public Timer timer;
	public final int ParamID;

	public PidContent(String tag,double p,double i,double d,double max_i,int paramID){
		Tag=tag;
		vP=p;
		vI=i;
		vD=d;
		MAX_I=max_i;
		timer=new Timer();
		ParamID=paramID;
	}

	public double getFulfillment(){
		return P+I+D;
	}
	public double getKp(){
		return Params.PIDParams.kP[ParamID];
	}
	public double getKd(){
		return Params.PIDParams.kD[ParamID];
	}
	public double getKi(){
		return Params.PIDParams.kI[ParamID];
	}

}

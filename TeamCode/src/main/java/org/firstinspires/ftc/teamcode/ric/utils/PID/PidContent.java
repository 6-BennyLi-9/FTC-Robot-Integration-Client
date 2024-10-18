package org.firstinspires.ftc.teamcode.ric.utils.PID;

import static org.firstinspires.ftc.teamcode.ric.Params.PIDParams;
import static org.firstinspires.ftc.teamcode.ric.Params.PIDParams.kD;
import static org.firstinspires.ftc.teamcode.ric.Params.PIDParams.kI;
import static org.firstinspires.ftc.teamcode.ric.Params.PIDParams.kP;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.ric.utils.annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.ric.utils.Timer;

@UtilFunctions
public class PidContent {
	public final String tag;
	public final double vP,vI,vD,MAX_I;
	public double P,I,D;
	public double inaccuracies,lastInaccuracies,fulfillment;
	public Timer timer;
	public final int ParamID;

	public PidContent(String tag,double p,double i,double d,double max_i,int paramID){
		this.tag =tag;
		vP=p;
		vI=i;
		vD=d;
		MAX_I=max_i;
		timer=new Timer();
		ParamID=paramID;
	}
	public PidContent(String tag,int paramID){
		this(tag, kP[paramID], kI[paramID], kD[paramID], PIDParams.MAX_I[paramID],paramID);
	}

	public double getFulfillment(){
		return P+I+D;
	}
	public double getKp(){
		return kP[ParamID];
	}
	public double getKd(){
		return kD[ParamID];
	}
	public double getKi(){
		return kI[ParamID];
	}

	@NonNull
	@Override
	public String toString() {
		return "\""+ tag +"\":"+P+","+I+","+"D";
	}
}

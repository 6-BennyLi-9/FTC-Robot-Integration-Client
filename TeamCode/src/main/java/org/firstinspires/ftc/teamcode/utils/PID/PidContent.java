package org.firstinspires.ftc.teamcode.utils.PID;

import static org.firstinspires.ftc.teamcode.Params.PIDParams;
import static org.firstinspires.ftc.teamcode.Params.PIDParams.kD;
import static org.firstinspires.ftc.teamcode.Params.PIDParams.kI;
import static org.firstinspires.ftc.teamcode.Params.PIDParams.kP;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.utils.annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.utils.Timer;

@UtilFunctions
public class PidContent {
	public final String tag;
	public final double vP,vI,vD,MAX_I;
	public double P,I,D;
	public double inaccuracies,lastInaccuracies,fulfillment;
	public Timer timer;
	public final int ParamID;

	public PidContent(final String tag, final double p, final double i, final double d, final double max_i, final int paramID){
		this.tag =tag;
		this.vP =p;
		this.vI =i;
		this.vD =d;
		this.MAX_I =max_i;
		this.timer =new Timer();
		this.ParamID =paramID;
	}
	public PidContent(final String tag, final int paramID){
		this(tag, kP[paramID], kI[paramID], kD[paramID], Params.PIDParams.MAX_I[paramID],paramID);
	}

	public double getFulfillment(){
		return this.P + this.I + this.D;
	}
	public double getKp(){
		return kP[this.ParamID];
	}
	public double getKd(){
		return kD[this.ParamID];
	}
	public double getKi(){
		return kI[this.ParamID];
	}

	@NonNull
	@Override
	public String toString() {
		return "\"" + this.tag + "\":" + this.P + "," + this.I + "," + "D";
	}
}

package org.firstinspires.ftc.teamcode.Utils.PID;

import org.firstinspires.ftc.teamcode.Utils.Annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.Utils.Timer;

@UtilFunctions
public class PidContent {
	public final String Tag;
	public final double vP,vI,vD,MAX_I;
	public double P,I,D;
	public Timer timer;

	public PidContent(String tag,double p,double i,double d,double max_i){
		Tag=tag;
		vP=p;
		vI=i;
		vD=d;
		MAX_I=max_i;
		timer=new Timer();
	}

	public double getFulfillment(){
		return P+I+D;
	}
}

package org.firstinspires.ftc.teamcode.utils;


import java.util.Arrays;

public class PID_processor {
	private static final double[] kP={0.12,0.15,0.12};
	private static final double[] kI={0,0,0};
	private static final double[] MAX_I={100,100,360};
	private static final double[] kD={0.04,0.05,0.04};

	private double[] P,I,D;
	public double[] inaccuracies,lastInaccuracies,fulfillment;

	private final long[] timeFlags={0,0,0};

	public PID_processor(){
		this(System.currentTimeMillis());
	}
	public PID_processor(long TimeNow) {
		Arrays.fill(timeFlags, TimeNow);
	}

	private void I_processor(int ID){
		if(ID==2){//TODO:列出所有与角度有关的ID
			I[ID]=Mathematics.angleRationalize(I[ID]);
		}else{
			I[ID]=Mathematics.intervalClip(I[ID],-MAX_I[ID],MAX_I[ID]);
		}
	}

	/**
	 * 不要更改，不要更改，不要更改
	 * @param runTime 在上一次调用ModifyPID距离本次的时间差
	 * @param ID 要调用那一个数据的编号
	 */
	public void ModifyPID(long runTime,int ID){
		P[ID]=inaccuracies[ID]*kP[ID];
		I[ID]+=inaccuracies[ID]*kI[ID]*runTime;

		I_processor(ID);

		D[ID]=(inaccuracies[ID]-lastInaccuracies[ID])*kD[ID]/runTime;
		lastInaccuracies[ID]=inaccuracies[ID];

		fulfillment[ID]=P[ID]+I[ID]+D[ID];
	}

	/**
	 * @param ID 要调用那一个数据的编号
	 */
	public void simplePID(int ID){
		long now=System.currentTimeMillis();
		ModifyPID(now-timeFlags[ID],ID);
		timeFlags[ID]=now;
	}

	/**
	 * 刷新所有PID
	 */
	public void update(){
		for(int i=0;i<kP.length;++i){
			simplePID(i);
		}
	}
}

package org.firstinspires.ftc.teamcode.utils;


import java.util.Arrays;

public class PID_processor {
	private static final int N = 3;
	private static double[] kP,kI,kD;
	private static double[] MAX_I;

	private final  double[] P,I,D;
	public double[] inaccuracies,lastInaccuracies,fulfillment;

	private final long[] timeFlags={0,0,0};

	public PID_processor(){
		this(System.currentTimeMillis());
	}
	public PID_processor(long TimeNow) {
		Arrays.fill(timeFlags, TimeNow);
		P=new double[N];
		I=new double[N];
		D=new double[N];

		//与底盘相关的kP理论值：SimpleMecanumDrive.Params.vP
		//TODO:预设...[0]为底盘X，[1]为底盘Y，[2]为底盘方向
		kP= new double[]{0.12, 0.15, 0.12};
		kI= new double[]{0, 0, 0};
		kD= new double[]{0.04, 0.05, 0.04};

		MAX_I= new double[]{100, 100, 0};//可以用0代替所有与角度有关的I
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

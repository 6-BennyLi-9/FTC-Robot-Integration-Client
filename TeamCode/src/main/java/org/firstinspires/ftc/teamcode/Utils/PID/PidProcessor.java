package org.firstinspires.ftc.teamcode.Utils.PID;


import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.Utils.Mathematics;

import java.util.Objects;
import java.util.Vector;

public class PidProcessor {
	private final Vector< PidContent > contents;
	public double[] inaccuracies,lastInaccuracies,fulfillment;

	public PidProcessor(){
		contents=new Vector<>();
	}

	private void I_processor(int ID){
		if(Objects.equals(contents.get(ID).Tag, "Turning")){//TODO:列出所有与角度有关的ID
			contents.get(ID).I= Mathematics.angleRationalize(contents.get(ID).vI);
		}else{
			contents.get(ID).I=Mathematics.intervalClip(contents.get(ID).vI,-Params.PIDParams.MAX_I[ID], Params.PIDParams.MAX_I[ID]);
		}
	}

	/**
	 * 不要更改，不要更改，不要更改
	 * @param ID 要调用那一个数据的编号
	 */
	public void ModifyPID(int ID){
		contents.get(ID).P=inaccuracies[ID]* Params.PIDParams.kP[ID];
		contents.get(ID).I+=inaccuracies[ID]* Params.PIDParams.kI[ID]*contents.get(ID).timer.getDeltaTime();

		I_processor(ID);

		contents.get(ID).D=(inaccuracies[ID]-lastInaccuracies[ID])* Params.PIDParams.kD[ID]/contents.get(ID).timer.getDeltaTime();
		lastInaccuracies[ID]=inaccuracies[ID];

		fulfillment[ID]=contents.get(ID).getFulfillment();
	}

	/**
	 * @param ID 要调用那一个数据的编号
	 */
	public void simplePID(int ID){
		for (PidContent content : contents) {
			content.timer.stopAndRestart();
		}
		ModifyPID(ID);
	}

	/**
	 * 刷新所有PID
	 */
	public void update(){
		for(int i = 0; i< contents.size(); ++i){
			simplePID(i);
		}
	}

	@UtilFunctions
	public void loadContent(PidContent content){
		contents.add(content);
	}
}

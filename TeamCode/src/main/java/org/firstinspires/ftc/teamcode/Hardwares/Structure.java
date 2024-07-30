package org.firstinspires.ftc.teamcode.Hardwares;

import org.firstinspires.ftc.teamcode.Hardwares.basic.Motors;
import org.firstinspires.ftc.teamcode.Hardwares.basic.Servos;

public class Structure {
	Motors motors;
	Servos servos;

	public Structure(Motors motors,Servos servos){
		this.motors=motors;
		this.servos=servos;
	}

	public void OpenFrontClip(){
		servos.FrontClipPosition=0.84;
	}
	public void OpenRearClip(){
		servos.FrontClipPosition=0;
	}
	public void CloseFrontClip(){
		servos.FrontClipPosition=0.46;
	}
	public void CloseRearClip(){
		servos.FrontClipPosition=0.4;
	}

	public void openClips(){
		OpenFrontClip();
		OpenRearClip();
	}
	public void closeClips(){
		CloseFrontClip();
		CloseRearClip();
	}
}

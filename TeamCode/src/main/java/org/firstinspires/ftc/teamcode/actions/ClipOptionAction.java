package org.firstinspires.ftc.teamcode.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.hardwares.Structure;
import org.firstinspires.ftc.teamcode.hardwares.basic.ClipPosition;
import org.firstinspires.ftc.teamcode.hardwares.basic.Servos;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

@Deprecated
public class ClipOptionAction implements Action {
	private Structure structure=null;
	private Servos servos=null;
	private final ClipPosition aimPositionType;

	@UserRequirementFunctions
	public ClipOptionAction(Structure structure,@NonNull ClipPosition aimPositionType){
		this.structure=structure;
		this.aimPositionType=aimPositionType;
	}
	@UserRequirementFunctions
	public ClipOptionAction(Servos servos,@NonNull ClipPosition aimPositionType){
		this.servos=servos;
		this.aimPositionType=aimPositionType;
	}

	private boolean OptionPushed=false;
	@Override
	public boolean run(@NonNull TelemetryPacket telemetryPacket) {
		if (!OptionPushed) {
			OptionPushed=true;
			if(structure!=null){
				structure.clipOption(aimPositionType);
				structure.servos.update();
			}else if (servos!=null){
				switch (aimPositionType) {
					case Open:
						servos.FrontClipPosition= Params.ServoConfigs.frontClipOpen;
						servos.RearClipPosition= Params.ServoConfigs.rearClipOpen;
						break;
					case Close:
						servos.FrontClipPosition= Params.ServoConfigs.frontClipClose;
						servos.RearClipPosition= Params.ServoConfigs.rearClipClose;
						break;
				}
				servos.update();
			}else{
				throw new NullPointerException("Both Structure and Servos are null");
			}
		}

		if(structure!=null){
			return  !structure.servos.inPlace();
		}else if (servos!=null){
			return  !servos.inPlace();
		}else{
			throw new NullPointerException("Both Structure and Servos are null");
		}
	}
}

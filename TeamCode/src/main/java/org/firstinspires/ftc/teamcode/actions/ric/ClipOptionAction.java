package org.firstinspires.ftc.teamcode.actions.ric;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.actions.Action;
import org.firstinspires.ftc.teamcode.hardwares.Structure;
import org.firstinspires.ftc.teamcode.hardwares.controllers.ClipPosition;
import org.firstinspires.ftc.teamcode.hardwares.controllers.Servos;
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;

@Deprecated
public class ClipOptionAction implements Action {
	private Structure structure;
	private Servos servos;
	private final ClipPosition aimPositionType;

	@UserRequirementFunctions
	public ClipOptionAction(final Structure structure, @NonNull final ClipPosition aimPositionType){
		this.structure=structure;
		this.aimPositionType=aimPositionType;
	}
	@UserRequirementFunctions
	public ClipOptionAction(final Servos servos, @NonNull final ClipPosition aimPositionType){
		this.servos=servos;
		this.aimPositionType=aimPositionType;
	}

	private boolean OptionPushed;
	@Override
	public boolean run() {
		if (! this.OptionPushed) {
			this.OptionPushed =true;
			if(null != structure){
				this.structure.clipOption(this.aimPositionType);
				this.structure.servos.update();
			}else if (null != servos){
				switch (this.aimPositionType) {
					case Open:
						this.servos.FrontClipPosition= Params.ServoConfigs.frontClipOpen;
						this.servos.RearClipPosition= Params.ServoConfigs.rearClipOpen;
						break;
					case Close:
						this.servos.FrontClipPosition= Params.ServoConfigs.frontClipClose;
						this.servos.RearClipPosition= Params.ServoConfigs.rearClipClose;
						break;
				}
				this.servos.update();
			}else{
				throw new NullPointerException("Both Structure and Servos are null");
			}
		}

		if(null != structure){
			return  ! this.structure.servos.inPlace();
		}else if (null != servos){
			return  ! this.servos.inPlace();
		}else{
			throw new NullPointerException("Both Structure and Servos are null");
		}
	}
}

package org.firstinspires.ftc.teamcode.drives.localizers.odometries;

import org.firstinspires.ftc.teamcode.utils.annotations.OdometerPrograms;

@OdometerPrograms
public class ArcOrganizedOdometer extends ClassicOdometer {
	public ArcOrganizedOdometer() {
	}

	@Override
	public void update(final double relDeltaX, final double relDeltaY, final double relDeltaTheta) {
		final double REL_X = this.GetArcRelX(relDeltaX, relDeltaY, relDeltaTheta);
		final double REL_Y = this.GetArcRelY(relDeltaX, relDeltaY, relDeltaTheta);
		this.AddUnAlignmentDelta(REL_X,REL_Y, relDeltaTheta);
	}

	/**
	 * @param relTheta 角度制
	 */
	protected double GetArcRelX(final double relX, final double relY, final double relTheta){
		final double r0 = relX / relTheta;
		final double r1 = relY / relTheta;
		return r0*Math.sin(Math.toRadians(relTheta))-r1*(1-Math.cos(Math.toRadians(relTheta)));
	}
	/**
	 * @param relTheta 角度制
	 */
	protected double GetArcRelY(final double relX, final double relY, final double relTheta){
		final double r0 = relX / relTheta;
		final double r1 = relY / relTheta;
		return r1*Math.sin(Math.toRadians(relTheta))+r0*(1-Math.cos(Math.toRadians(relTheta)));
	}
}

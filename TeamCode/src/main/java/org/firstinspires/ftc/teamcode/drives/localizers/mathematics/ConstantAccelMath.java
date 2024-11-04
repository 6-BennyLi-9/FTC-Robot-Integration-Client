package org.firstinspires.ftc.teamcode.drives.localizers.mathematics;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.utils.Position2d;

public class ConstantAccelMath {
	public static final double FIDELITY = 1.0E-8;

	private double lastLoop = 0.008;
	private Position2d lastRelativeDelta = new Position2d(0,0,0);

	public void calculate(final double loopTime, @NonNull final Position2d relDelta, @NonNull final Position2d currPose){
		final double relDeltaX = relDelta.x;
		final double relDeltaY = relDelta.y;
		final double deltaHeading = relDelta.heading;

		final double arx = (relDeltaX * this.lastLoop - this.lastRelativeDelta.x * loopTime) / (loopTime * this.lastLoop * this.lastLoop + loopTime * loopTime * this.lastLoop);
		final double vrx = relDeltaX / loopTime - arx * loopTime;
		//v_x = vrx + arx*t
		final double ary = (relDeltaY * this.lastLoop - this.lastRelativeDelta.y * loopTime) / (loopTime * this.lastLoop * this.lastLoop + loopTime * loopTime * this.lastLoop);
		final double vry = relDeltaY / loopTime - ary * loopTime;
		//v_y = vry + ary*t
		final double arh = (deltaHeading * this.lastLoop - this.lastRelativeDelta.heading * loopTime) / (loopTime * this.lastLoop * this.lastLoop + loopTime * loopTime * this.lastLoop);
		final double vrh = deltaHeading / loopTime - arh * loopTime;
		//h = h1 + vry*t + ary*t^2

		final IntegralAutoCorrection xQuadrature = new IntegralAutoCorrection(new double[] {vrx, 2 * arx},new double[] {currPose.heading, vrh, arh});
		final IntegralAutoCorrection yQuadrature = new IntegralAutoCorrection(new double[] {vry, 2 * ary},new double[] {currPose.heading, vrh, arh});

		currPose.x += xQuadrature.evaluateCos(FIDELITY, 0, loopTime, 0) - yQuadrature.evaluateSin(FIDELITY, 0, loopTime, 0);
		currPose.y += yQuadrature.evaluateCos(FIDELITY, 0, loopTime, 0) + xQuadrature.evaluateSin(FIDELITY, 0, loopTime, 0);
		currPose.heading += deltaHeading;

		this.lastRelativeDelta = relDelta;
		this.lastLoop = loopTime;
	}
}
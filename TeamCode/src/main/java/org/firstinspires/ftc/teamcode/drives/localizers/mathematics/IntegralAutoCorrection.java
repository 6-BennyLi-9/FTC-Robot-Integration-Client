package org.firstinspires.ftc.teamcode.drives.localizers.mathematics;

/**
 * <a href="https://github.com/FTCclueless/Centerstage/blob/master/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/subsystems/drive/localizers/AdaptiveQuadrature.java">详见此处</a>
 */
public class IntegralAutoCorrection {
	private final double[] vel, heading;
	public IntegralAutoCorrection(final double[] vel, final double[] heading) {
		this.vel = vel;
		this.heading = heading;
	}

	private double f_cos(final double t) {
		if (0 == t) {
			return this.vel[0] * Math.cos(this.heading[0]);
		}
		double v = 0;
		for (int i = 0 ; i < this.vel.length; i ++)
			v += this.vel[i] * Math.pow(t,i);
		double h = 0;
		for (int i = 0 ; i < this.heading.length; i ++)
			h += this.heading[i] * Math.pow(t,i);
		return v*Math.cos(h);
	}

	private double f_sin(final double t) {
		if (0 == t) {
			return this.vel[0] * Math.sin(this.heading[0]);
		}
		double v = 0;
		for (int i = 0 ; i < this.vel.length; i ++)
			v += this.vel[i] * Math.pow(t,i);
		double h = 0;
		for (int i = 0 ; i < this.heading.length; i ++)
			h += this.heading[i] * Math.pow(t,i);
		return v*Math.sin(h);
	}

	public double evaluateCos(final double eps, final double t1, final double t2, final int level) { //vel*cos(heading)
		final double s1 = (t2 - t1) * (this.f_cos(t1) + 4.0 * this.f_cos((t1 + t2) / 2.0) + this.f_cos(t2)) / 6.0;
		final double s2 = (t2 - t1) * (this.f_cos(t1) + 4.0 * this.f_cos(0.75 * t1 + 0.25 * t2) + 2.0 * this.f_cos(0.5 * t1 + 0.5 * t2) + 4.0 * this.f_cos(0.25 * t1 + 0.75 * t2) + this.f_cos(t2)) / 12.0;
		if (Math.abs(s2-s1) <= eps) {
			return s2;
		}
		if (10 < level) {
			return s2;
		}
		return this.evaluateCos(eps / 2.0,t1, (t1 + t2) / 2.0, level + 1) + this.evaluateCos(eps / 2.0, (t1 + t2) / 2.0,t2, level + 1);
	}

	public double evaluateSin(final double eps, final double t1, final double t2, final int level) { //vel*sin(heading)
		final double s1 = (t2 - t1) * (this.f_sin(t1) + 4.0 * this.f_sin((t1 + t2) / 2.0) + this.f_sin(t2)) / 6.0;
		final double s2 = (t2 - t1) * (this.f_sin(t1) + 4.0 * this.f_sin(0.75 * t1 + 0.25 * t2) + 2.0 * this.f_sin(0.5 * t1 + 0.5 * t2) + 4.0 * this.f_sin(0.25 * t1 + 0.75 * t2) + this.f_sin(t2)) / 12.0;
		if (Math.abs(s2-s1) <= eps) {
			return s2;
		}
		if (10 < level) {
			return s2;
		}
		return this.evaluateSin(eps / 2.0,t1, (t1 + t2) / 2.0, level + 1) + this.evaluateSin(eps / 2.0, (t1 + t2) / 2.0,t2, level + 1);
	}
}

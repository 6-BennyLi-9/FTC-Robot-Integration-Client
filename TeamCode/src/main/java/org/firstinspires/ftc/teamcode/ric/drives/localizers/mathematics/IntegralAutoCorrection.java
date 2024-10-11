package org.firstinspires.ftc.teamcode.ric.drives.localizers.mathematics;

/**
 * <a href="https://github.com/FTCclueless/Centerstage/blob/master/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/subsystems/drive/localizers/AdaptiveQuadrature.java">详见此处</a>
 */
public class IntegralAutoCorrection {
	private final double[] vel, heading;
	public IntegralAutoCorrection(double[] vel, double[] heading) {
		this.vel = vel;
		this.heading = heading;
	}

	private double f_cos(double t) {
		if (t == 0) {
			return vel[0]*Math.cos(heading[0]);
		}
		double v = 0;
		for (int i = 0; i < vel.length; i ++)
			v += vel[i] * Math.pow(t,i);
		double h = 0;
		for (int i = 0; i < heading.length; i ++)
			h += heading[i] * Math.pow(t,i);
		return v*Math.cos(h);
	}

	private double f_sin(double t) {
		if (t == 0) {
			return vel[0]*Math.sin(heading[0]);
		}
		double v = 0;
		for (int i = 0; i < vel.length; i ++)
			v += vel[i] * Math.pow(t,i);
		double h = 0;
		for (int i = 0; i < heading.length; i ++)
			h += heading[i] * Math.pow(t,i);
		return v*Math.sin(h);
	}

	public double evaluateCos(double eps, double t1, double t2, int level) { //vel*cos(heading)
		double s1 = (t2-t1)*(f_cos(t1)+4.0* f_cos((t1+t2)/2.0)+ f_cos(t2))/6.0;
		double s2 = (t2-t1)*(f_cos(t1)+4.0* f_cos(0.75*t1+0.25*t2)+2.0* f_cos(0.5*t1+0.5*t2)+4.0* f_cos(0.25*t1+0.75*t2)+ f_cos(t2))/12.0;
		if (Math.abs(s2-s1) <= eps) {
			return s2;
		}
		if (level > 10) {
			return s2;
		}
		return evaluateCos(eps/2.0,t1,(t1+t2)/2.0,level+1) + evaluateCos(eps/2.0,(t1+t2)/2.0,t2,level+1);
	}

	public double evaluateSin(double eps, double t1, double t2, int level) { //vel*sin(heading)
		double s1 = (t2-t1)*(f_sin(t1)+4.0* f_sin((t1+t2)/2.0)+ f_sin(t2))/6.0;
		double s2 = (t2-t1)*(f_sin(t1)+4.0* f_sin(0.75*t1+0.25*t2)+2.0* f_sin(0.5*t1+0.5*t2)+4.0* f_sin(0.25*t1+0.75*t2)+ f_sin(t2))/12.0;
		if (Math.abs(s2-s1) <= eps) {
			return s2;
		}
		if (level > 10) {
			return s2;
		}
		return evaluateSin(eps/2.0,t1,(t1+t2)/2.0,level+1) + evaluateSin(eps/2.0,(t1+t2)/2.0,t2,level+1);
	}
}

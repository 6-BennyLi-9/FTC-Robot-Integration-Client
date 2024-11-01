package org.roadrunner.core.messages;

import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public final class TwoDeadWheelInputsMessage {
    public long timestamp;
    public PositionVelocityPair par;
    public PositionVelocityPair perp;
    public double yaw;
    public double pitch;
    public double roll;
    public double xRotationRate;
    public double yRotationRate;
    public double zRotationRate;

    public TwoDeadWheelInputsMessage(final PositionVelocityPair par, final PositionVelocityPair perp, final YawPitchRollAngles angles, final AngularVelocity angularVelocity) {
        timestamp = System.nanoTime();
        this.par = par;
        this.perp = perp;
        {
            yaw = angles.getYaw(AngleUnit.RADIANS);
            pitch = angles.getPitch(AngleUnit.RADIANS);
            roll = angles.getRoll(AngleUnit.RADIANS);
        }
        {
            xRotationRate = angularVelocity.xRotationRate;
            yRotationRate = angularVelocity.yRotationRate;
            zRotationRate = angularVelocity.zRotationRate;
        }
    }
}

package org.roadrunner.core.messages;

import com.acmerobotics.roadrunner.PoseVelocity2dDual;
import com.acmerobotics.roadrunner.Time;

public final class DriveCommandMessage {
    public long timestamp;
    public double forwardVelocity;
    public double forwardAcceleration;
    public double lateralVelocity;
    public double lateralAcceleration;
    public double angularVelocity;
    public double angularAcceleration;

    public DriveCommandMessage(final PoseVelocity2dDual<Time> poseVelocity) {
        timestamp = System.nanoTime();
        forwardVelocity = poseVelocity.linearVel.x.get(0);
        forwardAcceleration = poseVelocity.linearVel.x.get(1);
        lateralVelocity = poseVelocity.linearVel.y.get(0);
        lateralAcceleration = poseVelocity.linearVel.y.get(1);
        angularVelocity = poseVelocity.angVel.get(0);
        angularAcceleration = poseVelocity.angVel.get(1);
    }
}

package org.roadrunner.core.messages;

import com.acmerobotics.roadrunner.Pose2d;

public final class PoseMessage {
    public long timestamp;
    public double x;
    public double y;
    public double heading;

    public PoseMessage(final Pose2d pose) {
        timestamp = System.nanoTime();
        x = pose.position.x;
        y = pose.position.y;
        heading = pose.heading.toDouble();
    }
}


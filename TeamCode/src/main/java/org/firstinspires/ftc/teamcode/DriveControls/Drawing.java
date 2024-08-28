package org.firstinspires.ftc.teamcode.DriveControls;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

public final class Drawing {
    private Drawing() {}

    public static final String Blue="#3F51B5";

    public static void drawInstantRobot(@NonNull Canvas c, @NonNull Pose2d t) {
        final double ROBOT_RADIUS = 9;

        c.setStrokeWidth(1);
        c.strokeCircle(t.position.x, t.position.y, ROBOT_RADIUS);

        Vector2d half_v = t.heading.vec().times(0.5 * ROBOT_RADIUS);
        Vector2d p1 = t.position.plus(half_v);
        Vector2d p2 = p1.plus(half_v);
        c.strokeLine(p1.x, p1.y, p2.x, p2.y);
    }
    public static void drawInstantRobot(@NonNull Pose2d pose){
        TelemetryPacket packet=new TelemetryPacket();
        packet.fieldOverlay().setStroke(Blue);
        drawInstantRobot(packet.fieldOverlay(),pose);
        FtcDashboard.getInstance().sendTelemetryPacket(packet);
    }
}

package org.firstinspires.ftc.teamcode.Utils;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.DriveControls.OrderDefinition.DriveOrder;
import org.firstinspires.ftc.teamcode.DriveControls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.Utils.Enums.State;

public final class Functions {
    @UtilFunctions
    public static double getCurrentTimeMills(){
        return System.nanoTime()/1.0E06;
    }

    /**
     * @param from 起始点位
     * @param end 结束点位
     * @param progress 当前执行进度[0,1)
     *
     * @return 在目标进度下机器的理想位置
     */
    @NonNull
    @UtilFunctions
    public static Pose2d getAimPositionThroughTrajectory(@NonNull Pose2d from, @NonNull Pose2d end, @NonNull Pose2d RobotPosition, double progress){
        Complex cache=new Complex(new Vector2d(
                end.position.x-from.position.x,
                end.position.y-from.position.y
        ));
        cache=cache.times(progress);
        return new Pose2d(
                RobotPosition.position.x+cache.RealPart,
                RobotPosition.position.y+cache.imaginary(),
                from.heading.toDouble()+(end.heading.toDouble()-from.heading.toDouble())*progress
        );
    }

    /**
     * @param driveOrder 给出的执行命令，会自动根据给出的命令进行判断处理
     * @param progress   当前执行进度[0,1)
     * @return 在目标进度下机器的理想位置
     */
    @NonNull
    @UtilFunctions
    public static Pose2d getAimPositionThroughTrajectory(@NonNull DriveOrder driveOrder, @NonNull Pose2d RobotPosition , double progress){
        switch (driveOrder.getState()) {
            case LinerStrafe:
            case LinerWithTurn:
            case TurnOnly:
                SimpleMecanumDrive.state= State.StrafeToPoint;
                return getAimPositionThroughTrajectory(driveOrder.getPose(), driveOrder.NEXT(), RobotPosition , progress);
            case Spline://TODO:功能仍在开发中
                SimpleMecanumDrive.state= State.FollowSpline;
                break;
            default:
                return new Pose2d(0, 0, 0);
        }
        throw new RuntimeException("If you see this Exception on DriverHub, please let us know in the issue");
    }
}

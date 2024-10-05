package org.firstinspires.ftc.teamcode.utils;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.controls.definition.DriveOrder;
import org.firstinspires.ftc.teamcode.controls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utils.annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.utils.enums.RobotState;
import org.jetbrains.annotations.Contract;

public final class Functions extends Mathematics{
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
                SimpleMecanumDrive.robotState = RobotState.StrafeToPoint;
                return getAimPositionThroughTrajectory(driveOrder.getPose(), driveOrder.NEXT(), RobotPosition , progress);
            case Spline://TODO:功能仍在开发中
                SimpleMecanumDrive.robotState = RobotState.FollowSpline;
                break;
            default:
                return new Pose2d(0, 0, 0);
        }
        throw new RuntimeException("If you see this Exception on DriverHub, please let us know in the issue");
    }

    /**
     * @param globalTheta 为角度制
     */
    @NonNull
    @Contract("_, _, _ -> new")
    @UtilFunctions
    public static Pose2d Alignment2d(double deltaX, double deltaY, double globalTheta){
        return new Pose2d(
                deltaX *Math.cos(Math.toRadians(globalTheta))- deltaY *Math.sin(Math.toRadians(globalTheta)),
                deltaY *Math.cos(Math.toRadians(globalTheta))+ deltaX *Math.sin(Math.toRadians(globalTheta)),
                globalTheta
        );
    }
    @NonNull
    @Contract("_ -> new")
    @UtilFunctions
    public static Pose2d Alignment2d(@NonNull Pose2d pose){
		return Alignment2d(pose.position.x,pose.position.y,Math.toDegrees(pose.heading.toDouble()));
    }
    @NonNull
    @Contract("_ -> new")
    @UtilFunctions
    public static Pose2d Alignment2d(@NonNull SimplePosition pose){
        return Alignment2d(pose.x,pose.y,pose.heading);
    }

	@UtilFunctions
	public static double distance(double deltaX,double deltaY){
		return Math.sqrt(deltaX*deltaX+deltaY*deltaY);
	}
    @UtilFunctions
    public static double getX(@NonNull Object pose){
	    if (pose.getClass().equals(Pose2d.class)) {
            return ((Pose2d) pose).position.x;
	    }else if(pose.getClass().equals(Vector2d.class)){
            return ((Vector2d) pose).x;
        }else if(pose.getClass().equals(SimplePosition.class)){
            return ((SimplePosition) pose).x;
        }else{
            throw new ClassCastException("Unknown Position Class:"+pose.getClass().getName());
        }
    }
    @UtilFunctions
    public static double getY(@NonNull Object pose){
        if (pose.getClass().equals(Pose2d.class)) {
            return ((Pose2d) pose).position.y;
        }else if(pose.getClass().equals(Vector2d.class)){
            return ((Vector2d) pose).y;
        }else if(pose.getClass().equals(SimplePosition.class)){
            return ((SimplePosition) pose).y;
        }else{
            throw new ClassCastException("Unknown Position Class:"+pose.getClass().getName());
        }
    }
}

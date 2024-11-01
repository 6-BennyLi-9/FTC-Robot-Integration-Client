package org.firstinspires.ftc.teamcode.utils;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.drives.controls.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.drives.controls.definition.DriveOrder;
import org.firstinspires.ftc.teamcode.utils.annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.utils.enums.RobotState;

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
    public static Position2d getAimPositionThroughTrajectory(@NonNull final Position2d from, @NonNull final Position2d end, @NonNull final Position2d RobotPosition, final double progress){
        Complex cache=new Complex(new Vector2d(
                end.x-from.x,
                end.y-from.y
        ));
        cache=cache.times(progress);
        return new Position2d(
                RobotPosition.x+cache.RealPart,
                RobotPosition.y+cache.imaginary(),
                from.heading+(end.heading-from.heading)*progress
        );
    }

    /**
     * @param driveOrder 给出的执行命令，会自动根据给出的命令进行判断处理
     * @param progress   当前执行进度[0,1)
     * @return 在目标进度下机器的理想位置
     */
    @NonNull
    @UtilFunctions
    public static Position2d getAimPositionThroughTrajectory(@NonNull final DriveOrder driveOrder, @NonNull final Position2d RobotPosition , final double progress){
        switch (driveOrder.getState()) {
            case LinerStrafe:
            case LinerWithTurn:
            case TurnOnly:
                SimpleMecanumDrive.robotState = RobotState.StrafeToPoint;
                return Functions.getAimPositionThroughTrajectory(driveOrder.getPose(), driveOrder.nextPose(), RobotPosition , progress);
            case Spline://TODO:功能仍在开发中
                SimpleMecanumDrive.robotState = RobotState.FollowSpline;
                break;
            default:
                return new Position2d(0, 0, 0);
        }
        throw new RuntimeException("If you see this Exception on DriverHub, please let us know in the issue");
    }

    /**
     * @param globalTheta 为角度制
     */
    @NonNull
    @UtilFunctions
    public static Position2d Alignment2d(final double deltaX, final double deltaY, final double globalTheta){
        return new Position2d(
                deltaX *Math.cos(Math.toRadians(globalTheta))- deltaY *Math.sin(Math.toRadians(globalTheta)),
                deltaY *Math.cos(Math.toRadians(globalTheta))+ deltaX *Math.sin(Math.toRadians(globalTheta)),
                globalTheta
        );
    }
    @NonNull
    @UtilFunctions
    public static Position2d Alignment2d(@NonNull final Position2d pose){
        return Functions.Alignment2d(pose.x,pose.y,pose.heading);
    }

	@UtilFunctions
	public static double distance(final double deltaX, final double deltaY){
		return Math.sqrt(deltaX*deltaX+deltaY*deltaY);
	}
    @UtilFunctions
    public static double getX(@NonNull final Object pose){
	    if (pose.getClass().equals(Position2d.class)) {
            return ((Position2d) pose).x;
	    }else if(pose.getClass().equals(Vector2d.class)){
            return ((Vector2d) pose).x;
        }else {
            throw new ClassCastException("Unknown Position Class:"+pose.getClass().getName());
        }
    }
    @UtilFunctions
    public static double getY(@NonNull final Object pose){
        if(pose.getClass().equals(Vector2d.class)){
            return ((Vector2d) pose).y;
        }else if(pose.getClass().equals(Position2d.class)){
            return ((Position2d) pose).y;
        }else{
            throw new ClassCastException("Unknown Position Class:"+pose.getClass().getName());
        }
    }
}

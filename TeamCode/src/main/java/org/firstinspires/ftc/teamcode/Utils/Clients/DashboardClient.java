package org.firstinspires.ftc.teamcode.Utils.Clients;

import android.util.Pair;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.Utils.Annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UtilFunctions;
import org.firstinspires.ftc.teamcode.Utils.Exceptions.UnKnownErrorsException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class DashboardClient {
	public static final String Blue="#3F51B5";
	public static final String Green="#4CAF50";
	public static final class Drawing {
		/**
		 * 智能地根据机器的点位，但是需要搭配相应的配套操作
		 * @param c 绘制的关键程序变量
		 * @param t 机器位置
		 */
		@UtilFunctions
		public static void drawRobot(@NonNull Canvas c, @NonNull Pose2d t) {
			final double ROBOT_RADIUS = 9;

			c.setStrokeWidth(1);
			c.strokeCircle(t.position.x, t.position.y, ROBOT_RADIUS);

			Vector2d half_v = t.heading.vec().times(0.5 * ROBOT_RADIUS);
			Vector2d p1 = t.position.plus(half_v);
			Vector2d p2 = p1.plus(half_v);
			c.strokeLine(p1.x, p1.y, p2.x, p2.y);
		}

		/**
		 * 默认为蓝色
		 * @param pose 机器位置
		 * @param packet 使用packet绘制机器
		 */
		@UtilFunctions
		public static void drawRobotUsingPacket(@NonNull Pose2d pose,@NonNull TelemetryPacket packet){
			drawRobotUsingPacket(pose,packet,Blue);
		}
		/**
		 * @param pose 机器位置
		 * @param packet 使用packet绘制机器
		 */
		@UtilFunctions
		public static void drawRobotUsingPacket(@NonNull Pose2d pose,@NonNull TelemetryPacket packet,@NonNull String color){
			packet.fieldOverlay().setStroke(color);
			drawRobot(packet.fieldOverlay(),pose);
			FtcDashboard.getInstance().sendTelemetryPacket(packet);
		}
	}
	private final Map< Integer , Pair < String , TelemetryPacket > > packets;
	private int ID=0;

	public DashboardClient(){
		packets=new HashMap<>();
	}

	private void pushPacket(TelemetryPacket packet, @NonNull Object tag){
		packets.put(++ID,new Pair<>(String.valueOf(tag),packet));
	}
	private void pushPacket(TelemetryPacket packet){
		pushPacket(packet,ID+1);
	}

	/**
	 * 推荐使用的DrawRobot方法。可以自动使用packet进行draw
	 * <p>
	 * 同时会在DashBoard中发送机器的位置信息
	 * @see Drawing
	 */
	@ExtractedInterfaces
	public void DrawRobot(@NonNull Pose2d pose,@NonNull String color){
		DrawRobot(pose,color,ID+1);
	}
	/**
	 * 推荐使用的DrawRobot方法。可以自动使用packet进行draw
	 * <p>
	 * 同时会在DashBoard中发送机器的位置信息
	 * @see Drawing
	 */
	public void DrawRobot(@NonNull Pose2d pose,@NonNull String color,@NonNull Object tag){
		TelemetryPacket packet=new TelemetryPacket();
		Drawing.drawRobotUsingPacket(pose,packet, color);
		packet.put("TargetX", pose.position.x);
		packet.put("TargetY", pose.position.y);
		packet.put("TargetHeading(DEG)", Math.toDegrees(pose.heading.toDouble()));
		pushPacket(packet,tag);
		update();
	}

	@UtilFunctions
	public void DrawLine(@NonNull Object start,@NonNull Object end){
		DrawLine(start,end,ID+1);
	}
	@UtilFunctions
	public void DrawLine(@NonNull Object start,@NonNull Object end,@NonNull String color){
		DrawLine(start,end,ID+1,color);
	}
	public void DrawLine(@NonNull Object start,@NonNull Object end,@NonNull Object tag){
		DrawLine(start,end,tag,Blue);
	}
	public void DrawLine(@NonNull Object start,@NonNull Object end,@NonNull Object tag,String color){
		double sx,sy,ex,ey;
		if(start.getClass()==Vector2d.class){
			sx=((Vector2d) start).x;
			sy=((Vector2d) start).y;
		}else if(start.getClass()==Pose2d.class){
			sx=((Pose2d) start).position.x;
			sy=((Pose2d) start).position.y;
		}else{
			throw new UnKnownErrorsException(start.getClass().toString());
		}
		if(end.getClass()==Vector2d.class){
			ex=((Vector2d) end).x;
			ey=((Vector2d) end).y;
		}else if(end.getClass()==Pose2d.class){
			ex=((Pose2d) end).position.x;
			ey=((Pose2d) end).position.y;
		}else{
			throw new UnKnownErrorsException(end.getClass().toString());
		}
		TelemetryPacket packet=new TelemetryPacket();
		Canvas c=packet.fieldOverlay();
		c.setStroke(color);
		c.strokeLine(sx,sy,ex,ey);
		pushPacket(packet,tag);
		update();
	}
	@UtilFunctions
	public void clearDashBoardScreen(){
		packets.clear();
		update();
	}
	public void update(){
		FtcDashboard.getInstance().clearTelemetry();

		Vector<Pair<Integer, TelemetryPacket>> outputData = new Vector<>();
		for (Map.Entry<Integer, Pair<String, TelemetryPacket>> entry : packets.entrySet()) {
			outputData.add(new Pair<>(entry.getKey(),entry.getValue().second));
		}
		outputData.sort(Comparator.comparingInt(x -> x.first));
		for ( Pair<Integer, TelemetryPacket> outputPacket : outputData ){
			FtcDashboard.getInstance().sendTelemetryPacket(outputPacket.second);
		}

		FtcDashboard.getInstance().updateConfig();
	}
}

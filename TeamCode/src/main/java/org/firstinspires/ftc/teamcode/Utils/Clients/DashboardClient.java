package org.firstinspires.ftc.teamcode.Utils.Clients;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.Utils.Annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.Utils.Annotations.UtilFunctions;

import java.util.HashMap;
import java.util.Map;

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
	private final Map< String , TelemetryPacket > packets;
	private int ID=0;

	public DashboardClient(){
		packets=new HashMap<>();
	}


	/**
	 * 推荐使用的DrawRobot方法。可以自动使用packet进行draw
	 * <p>
	 * 同时会在DashBoard中发送机器的位置信息
	 * @see Drawing
	 */
	@ExtractedInterfaces
	public void DrawRobot(@NonNull Pose2d pose){
		TelemetryPacket packet = new TelemetryPacket();
		Drawing.drawRobotUsingPacket(pose, packet);
		packet.put("X", pose.position.x);
		packet.put("Y", pose.position.y);
		packet.put("Heading(DEG)", Math.toDegrees(pose.heading.toDouble()));
		packets.put(String.valueOf(++ID), packet);
		update();
	}
	/**
	 * 可以自动使用packet进行draw
	 * <p>
	 * 同时会在DashBoard中发送机器的位置信息
	 * @see Drawing
	 */
	@ExtractedInterfaces
	public void DrawTargetRobot(@NonNull Pose2d pose2d){
		TelemetryPacket packet=new TelemetryPacket();
		Drawing.drawRobotUsingPacket(pose2d,packet, Green);
		packet.put("TargetX",pose2d.position.x);
		packet.put("TargetY",pose2d.position.y);
		packet.put("TargetHeading(DEG)", Math.toDegrees(pose2d.heading.toDouble()));
		packets.put(String.valueOf(++ID),packet);
		update();
	}
	@UtilFunctions
	public void DrawLine(@NonNull Pose2d start,@NonNull Pose2d end){
		DrawLine(start.position,end.position);
	}
	@UtilFunctions
	public void DrawLine(@NonNull Vector2d start, @NonNull Vector2d end){
		TelemetryPacket packet=new TelemetryPacket();
		Canvas c=packet.fieldOverlay();
		c.setStroke(Blue);
		c.strokeLine(start.x,start.y,end.x,end.y);
		packets.put(String.valueOf(++ID),packet);
		update();
	}
	@UtilFunctions
	public void DrawTargetLine(@NonNull Pose2d start,@NonNull Pose2d end){
		TelemetryPacket packet=new TelemetryPacket();
		Canvas c=packet.fieldOverlay();
		c.setStroke(Green);
		c.strokeLine(start.position.x,start.position.y,end.position.x,end.position.y);
		packets.put(String.valueOf(++ID),packet);
		update();
	}
	@UtilFunctions
	public void clearDashBoardScreen(){
		packets.clear();
		update();
	}
	public void update(){
		FtcDashboard.getInstance().clearTelemetry();
		for (Map.Entry<String, TelemetryPacket> entry : packets.entrySet()) {
			TelemetryPacket packet = entry.getValue();
			FtcDashboard.getInstance().sendTelemetryPacket(packet);
		}
		FtcDashboard.getInstance().updateConfig();
	}
}

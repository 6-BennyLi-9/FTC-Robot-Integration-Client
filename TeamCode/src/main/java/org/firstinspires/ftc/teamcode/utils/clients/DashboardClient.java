package org.firstinspires.ftc.teamcode.utils.clients;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Position2d;
import org.firstinspires.ftc.teamcode.utils.Timer;
import org.firstinspires.ftc.teamcode.utils.annotations.UtilFunctions;

import java.util.HashMap;
import java.util.Map;

public class DashboardClient {
	private static DashboardClient instanceDashboardClient;
	public static final String Blue="#3F51B5";
	public static final String Green="#4CAF50";
	public static final String Red="#DEB887";
	public static final String Gray="#808080";

	protected Map < String , String > data;

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
		public static void drawRobotUsingPacket(@NonNull Position2d pose,@NonNull TelemetryPacket packet){
			drawRobotUsingPacket(pose,packet,Blue);
		}
		/**
		 * @param pose 机器位置
		 * @param packet 使用packet绘制机器
		 */
		@UtilFunctions
		public static void drawRobotUsingPacket(@NonNull Position2d pose,@NonNull TelemetryPacket packet,@NonNull String color){
			packet.fieldOverlay().setStroke(color);
			drawRobot(packet.fieldOverlay(),pose.asPose2d());
		}
	}
	public TelemetryPacket recentPacket;

	public DashboardClient(){
		recentPacket=new TelemetryPacket();
		data=new HashMap <>();
//		if(instanceDashboardClient!=null){
		instanceDashboardClient = this;
//		}else{
//			throw new RuntimeException("DashboardClient had already been created!");
//		}
	}

	public static DashboardClient getInstance(){
		return instanceDashboardClient;
	}

	/**
	 * 推荐使用的DrawRobot方法。可以自动使用packet进行draw
	 * <p>
	 * 同时会在DashBoard中发送机器的位置信息
	 * @see Drawing
	 */
	public void drawRobot(@NonNull Position2d pose, @NonNull String color, @NonNull String tag){
		Drawing.drawRobotUsingPacket(pose, recentPacket, color);
		recentPacket.put(tag+"X", pose.x);
		recentPacket.put(tag+"Y", pose.y);
		recentPacket.put(tag+"Heading(DEG)", Math.toDegrees(pose.heading));

		sendPacket();
	}

	/**
	 * 自动选择：蓝色，将 ID 作为tag
	 */
	@UtilFunctions
	public void drawLine(@NonNull Object start, @NonNull Object end){
		drawLine(start,end, String.valueOf(Timer.getCurrentTime()));
	}
	public void drawLine(@NonNull Object start, @NonNull Object end, String color){
		double sx,sy,ex,ey;
		sx= Functions.getX(start);
		sy= Functions.getY(start);
		ex= Functions.getX(end);
		ey= Functions.getY(end);

		Canvas c=recentPacket.fieldOverlay();
		c.setStroke(color);
		c.strokeLine(sx,sy,ex,ey);

		sendPacket();
	}

	@UtilFunctions
	public void clearDashBoardScreen(){
		recentPacket=new TelemetryPacket();
		FtcDashboard.getInstance().clearTelemetry();
	}

	public void sendPacket(){
		syncData();
		FtcDashboard.getInstance().sendTelemetryPacket(recentPacket);
		recentPacket=new TelemetryPacket(true);
	}

	public void put(@NonNull Object key, @NonNull Object value){
		data.put(key.toString(),value.toString());
	}

	private void syncData(){
		for(Map.Entry<String,String> entry:data.entrySet()){
			recentPacket.put(entry.getKey(),entry.getValue());
		}
	}
}

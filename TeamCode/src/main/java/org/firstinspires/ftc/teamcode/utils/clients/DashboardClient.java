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
import org.firstinspires.ftc.teamcode.utils.annotations.UserRequirementFunctions;
import org.firstinspires.ftc.teamcode.utils.annotations.UtilFunctions;

import java.util.HashMap;
import java.util.Map;

public class DashboardClient {
	public static final String Blue="#3F51B5";
	public static final String Green="#4CAF50";
	public static final String Red="#DEB887";
	public static final String Gray="#808080";

	/**
	 * @author roadrunner
	 */
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
	private final Map< String , TelemetryPacket  > packets;

	public DashboardClient(){
		packets=new HashMap<>();
	}

	/**
	 * 自动update()
	 */
	@UtilFunctions
	public void pushPacket(TelemetryPacket packet, @NonNull String tag){
		packets.put(tag,packet);
		update();
	}

	@UserRequirementFunctions
	public TelemetryPacket newRegisteredPacket(String tag){
		if(packets.containsKey(tag)){
			return packets.get(tag);
		}

		TelemetryPacket res=new TelemetryPacket();
		packets.put(tag,res);
		return res;
	}

	/**
	 * 推荐使用的DrawRobot方法。可以自动使用packet进行draw
	 * <p>
	 * 同时会在DashBoard中发送机器的位置信息
	 * @see Drawing
	 */
	public void DrawRobot(@NonNull Position2d pose, @NonNull String color, @NonNull String tag){
		TelemetryPacket packet=newRegisteredPacket(tag);
		Drawing.drawRobotUsingPacket(pose,packet, color);
		packet.put("TargetX", pose.x);
		packet.put("TargetY", pose.y);
		packet.put("TargetHeading(DEG)", Math.toDegrees(pose.heading));

		update();
	}

	/**
	 * 自动选择：蓝色，将 ID 作为tag
	 */
	@UtilFunctions
	public void DrawLine(@NonNull Object start,@NonNull Object end){
		DrawLine(start,end, String.valueOf(Timer.getCurrentTime()));
	}
	/**
	 * 自动选择：蓝色
	 */
	public void DrawLine(@NonNull Object start,@NonNull Object end,@NonNull String tag){
		DrawLine(start,end,tag,Blue);
	}
	public void DrawLine(@NonNull Object start,@NonNull Object end,@NonNull String tag,String color){
		double sx,sy,ex,ey;
		sx= Functions.getX(start);
		sy= Functions.getY(start);
		ex= Functions.getX(end);
		ey= Functions.getY(end);

		TelemetryPacket packet=newRegisteredPacket(tag);
		Canvas c=packet.fieldOverlay();
		c.setStroke(color);
		c.strokeLine(sx,sy,ex,ey);

		update();
	}

	@UtilFunctions
	public void deletePacketByTag(@NonNull String tag){
		if(!packets.containsKey(tag)){
			return;
		}
		packets.remove(tag);
	}

	@UtilFunctions
	public void clearDashBoardScreen(){
		packets.clear();
	}

	public void update(){
		for (Map.Entry<String, TelemetryPacket> entry : packets.entrySet()) {
			FtcDashboard.getInstance().sendTelemetryPacket(entry.getValue());
		}
	}
}

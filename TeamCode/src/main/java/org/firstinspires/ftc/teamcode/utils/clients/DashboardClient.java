package org.firstinspires.ftc.teamcode.utils.clients;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.utils.Functions;
import org.firstinspires.ftc.teamcode.utils.Position2d;
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

	public enum Drawing {
		;

		/**
		 * 智能地根据机器的点位，但是需要搭配相应的配套操作
		 * @param c 绘制的关键程序变量
		 * @param t 机器位置
		 */
		@UtilFunctions
		public static void drawRobot(@NonNull final Canvas c, @NonNull final Pose2d t) {
			final double ROBOT_RADIUS = 9;

			c.setStrokeWidth(1);
			c.strokeCircle(t.position.x, t.position.y, ROBOT_RADIUS);

			final Vector2d half_v = t.heading.vec().times(0.5 * ROBOT_RADIUS);
			final Vector2d p1     = t.position.plus(half_v);
			final Vector2d p2     = p1.plus(half_v);
			c.strokeLine(p1.x, p1.y, p2.x, p2.y);
		}

		/**
		 * 默认为蓝色
		 * @param pose 机器位置
		 * @param packet 使用packet绘制机器
		 */
		@UtilFunctions
		public static void drawRobotUsingPacket(@NonNull final Position2d pose, @NonNull final TelemetryPacket packet){
			Drawing.drawRobotUsingPacket(pose,packet, DashboardClient.Blue);
		}
		/**
		 * @param pose 机器位置
		 * @param packet 使用packet绘制机器
		 */
		@UtilFunctions
		public static void drawRobotUsingPacket(@NonNull final Position2d pose, @NonNull final TelemetryPacket packet, @NonNull final String color){
			packet.fieldOverlay().setStroke(color);
			Drawing.drawRobot(packet.fieldOverlay(),pose.toPose2d());
		}
	}
	public TelemetryPacket recentPacket;

	public DashboardClient(){
		this.recentPacket =new TelemetryPacket();
		this.data =new HashMap <>();
//		if(instanceDashboardClient!=null){
		DashboardClient.instanceDashboardClient = this;
//		}else{
//			throw new RuntimeException("DashboardClient had already been created!");
//		}
	}

	public static DashboardClient getInstance(){
		return DashboardClient.instanceDashboardClient;
	}

	/**
	 * 推荐使用的DrawRobot方法。可以自动使用packet进行draw
	 * <p>
	 * 同时会在DashBoard中发送机器的位置信息
	 * @see Drawing
	 */
	public void drawRobot(@NonNull final Position2d pose, @NonNull final String color, @NonNull final String tag){
		Drawing.drawRobotUsingPacket(pose, this.recentPacket, color);
		this.recentPacket.put(tag + "X", pose.x);
		this.recentPacket.put(tag + "Y", pose.y);
		this.recentPacket.put(tag + "Heading(DEG)", Math.toDegrees(pose.heading));

		this.sendPacket();
	}

	/**
	 * 自动选择：蓝色
	 */
	@UtilFunctions
	public void drawLine(@NonNull final Object start, @NonNull final Object end){
		this.drawLine(start,end, DashboardClient.Blue);
	}
	public void drawLine(@NonNull final Object start, @NonNull final Object end, final String color){
		final double sx;
		double       sy;
		double       ex;
		final double ey;
		sx= Functions.getX(start);
		sy= Functions.getY(start);
		ex= Functions.getX(end);
		ey= Functions.getY(end);

		final Canvas c = this.recentPacket.fieldOverlay();
		c.setStroke(color);
		c.strokeLine(sx,sy,ex,ey);

		this.sendPacket();
	}

	@UtilFunctions
	public void clearDashBoardScreen(){
		this.recentPacket =new TelemetryPacket();
		FtcDashboard.getInstance().clearTelemetry();
	}

	public void sendPacket(){
		this.syncData();
		FtcDashboard.getInstance().sendTelemetryPacket(this.recentPacket);
		this.recentPacket =new TelemetryPacket(true);
	}

	public void put(@NonNull final Object key, @NonNull final Object value){
		if(this.data.containsKey(key.toString())) {
			this.data.replace(key.toString(),value.toString());
		}else{
			this.data.put(key.toString(), value.toString());
		}
	}

	private void syncData(){
		for(final Map.Entry<String,String> entry: this.data.entrySet()){
			this.recentPacket.put(entry.getKey(),entry.getValue());
		}
	}

	public void showDataInTelemetry(){
		for(final Map.Entry<String,String> entry: this.data.entrySet()){
			TelemetryClient.getInstance().changeData("Dashboard-"+entry.getKey(),entry.getValue());
		}
	}
}

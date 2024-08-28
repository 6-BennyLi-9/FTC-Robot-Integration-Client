package org.firstinspires.ftc.teamcode.utils;

import android.util.Pair;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.Annotations.ExtractedInterfaces;
import org.firstinspires.ftc.teamcode.utils.Annotations.UtilFunctions;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

/**
 * @see org.firstinspires.ftc.teamcode.RIC_samples.ClientUsage
 */
public class Client {
	public static final class Drawing {
		private Drawing() {}

		public static final String Blue="#3F51B5";
		public static final String Green="#4CAF50";

		/**
		 * 智能地根据机器的点位，但是需要搭配相应的配套操作
		 * @param c 绘制的关键程序变量
		 * @param t 机器位置
		 */
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
		 * 智能地根据机器的点位，但是是瞬时的，一旦FtcDashboard有了更新，则很有可能机器的图案会消失
		 * @param pose 机器位置
		 */
		@UtilFunctions
		public static void drawInstantRobot(@NonNull Pose2d pose){
			TelemetryPacket packet=new TelemetryPacket();
			packet.fieldOverlay().setStroke(Blue);
			drawRobot(packet.fieldOverlay(),pose);
			FtcDashboard.getInstance().sendTelemetryPacket(packet);
		}

		/**
		 * 默认为蓝色
		 * @param pose 机器位置
		 * @param packet 使用packet绘制机器
		 */
		@UtilFunctions
		public static void drawRobotUsingPacket(@NonNull Pose2d pose,@NonNull TelemetryPacket packet){
			packet.fieldOverlay().setStroke(Blue);
			drawRobot(packet.fieldOverlay(),pose);
			FtcDashboard.getInstance().sendTelemetryPacket(packet);
		}
		/**
		 * @param pose 机器位置
		 * @param packet 使用packet绘制机器
		 */
		@UtilFunctions
		public static void drawRobotUsingPacket(@NonNull Pose2d pose,@NonNull TelemetryPacket packet,@NonNull String color){
			packet.fieldOverlay().setStroke(color);
			packet.fieldOverlay().setStroke(Blue);
			drawRobot(packet.fieldOverlay(),pose);
			FtcDashboard.getInstance().sendTelemetryPacket(packet);
		}
	}
	public Telemetry telemetry;
	public TelemetryPacket packet;
	private final Map < String , Pair < String , Integer > > data;
	private final Map < String , Integer > lines;
	private int ID=0;

	public Client(Telemetry telemetry){
		this.telemetry=telemetry;
		data =new HashMap<>();
		lines=new HashMap<>();
		packet=new TelemetryPacket();
		update();
	}

	public void clearInfo(){
		data.clear();
		update();
	}
	public void clearLines(){
		lines.clear();
		update();
	}
	public void clear(){
		clearInfo();
		clearLines();
		update();
	}

	/**
	 * 注意：这是新的Data
	 */
	public void addData(String key,String val){
		data.put(key,new Pair<>(val, ++ ID));
		update();
	}
	/**
	 * 注意：这是新的Data
	 */
	public void addData(String key,int val){
		addData(key,String.valueOf(val));
	}
	/**
	 * 注意：这是新的Data
	 */
	public void addData(String key,double val){
		addData(key,String.valueOf(val));
	}
	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	public void deleteDate(String key){
		if( data.containsKey(key)){
			data.remove(key);
		}else{
			throw new RuntimeException("can't find the key \""+key+"\".");
		}
		update();
	}

	/**
	 * 自动创建新的行如果key所指向的值不存在
	 */
	public void changeDate(String key,String val){
		if(data.containsKey(key)){
			data.replace(key,new Pair<>(val, Objects.requireNonNull(data.get(key)).second));
		}else{
			addData(key, val);
		}
		update();
	}
	/**
	 * 自动创建新的行如果key所指向的值不存在
	 */
	public void changeDate(String key,int val){
		changeDate(key,String.valueOf(val));
	}
	/**
	 * 自动创建新的行如果key所指向的值不存在
	 */
	public void changeDate(String key,double val){
		changeDate(key,String.valueOf(val));
	}

	public void addLine(String key){
		lines.put(key,++ID);
		update();
	}
	public void addLine(int key){
		addLine(String.valueOf(key));
	}
	public void addLine(double key){
		addLine(String.valueOf(key));
	}
	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	public void deleteLine(String key){
		if(lines.containsKey(key)){
			data.remove(key);
		}else{
			throw new RuntimeException("can't find the key \""+key+"\".");
		}
		update();
	}

	/**
	 * 将key行替代为val，自动创建新的行如果key所指向的值不存在
	 * @param oldDate 目标行
	 * @param newData 替换行
	 */
	public void changeLine(String oldDate, String newData){
		if(lines.containsKey(oldDate)){
			int cache=Objects.requireNonNull(lines.get(oldDate));
			lines.remove(oldDate);
			lines.put(newData,cache);
		}else{
			addLine(newData);
		}
		update();
	}

	public void update(){
		Vector<Pair<Integer, String>> outputData = new Vector<>();
		for (Map.Entry<String, Pair<String, Integer>> i : data.entrySet() ) {
			String key = i.getKey(),val=i.getValue().first;
			Integer IDCache=i.getValue().second;
			outputData.add(new Pair<>(IDCache,key+":"+val));
		}
		for ( Map.Entry<String, Integer> entry : lines.entrySet() ) {
			String key = entry.getKey();
			Integer val = entry.getValue();
			outputData.add(new Pair<>(val, key));
		}
		outputData.sort(Comparator.comparingInt(x -> x.first));
		for ( Pair<Integer, String> outputLine : outputData ) {
			telemetry.addLine("["+ outputLine.first+"]"+ outputLine.second);
		}
		telemetry.update();

		FtcDashboard.getInstance().sendTelemetryPacket(packet);
	}


	/**
	 * 推荐使用的DrawRobot方法。可以自动使用packet进行draw
	 * <p>
	 * 同时会在DashBoard中发送机器的位置信息
	 * @see Drawing
	 */
	@ExtractedInterfaces
	public void DrawRobot(@NonNull Pose2d pose2d){
		Drawing.drawRobotUsingPacket(pose2d,packet);
		packet.put("X",pose2d.position.x);
		packet.put("Y",pose2d.position.y);
		packet.put("Heading(DEG)", Math.toDegrees(pose2d.heading.toDouble()));
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
		Drawing.drawRobotUsingPacket(pose2d,packet,Drawing.Green);
		packet.put("TargetX",pose2d.position.x);
		packet.put("TargetY",pose2d.position.y);
		packet.put("TargetHeading(DEG)", Math.toDegrees(pose2d.heading.toDouble()));
		update();
	}
	@UtilFunctions
	public void DrawLine(@NonNull Pose2d start,@NonNull Pose2d end){
		Canvas c=packet.fieldOverlay();
		c.setStroke(Drawing.Blue);
		c.strokeLine(start.position.x,start.position.y,end.position.x,end.position.y);
		update();
	}
	@UtilFunctions
	public void DrawTargetLine(@NonNull Pose2d start,@NonNull Pose2d end){
		Canvas c=packet.fieldOverlay();
		c.setStroke(Drawing.Green);
		c.strokeLine(start.position.x,start.position.y,end.position.x,end.position.y);
		update();
	}
	@UtilFunctions
	public void clearDashBoardScreen(){
		packet=new TelemetryPacket();
		FtcDashboard.getInstance().clearTelemetry();
		update();
	}
}

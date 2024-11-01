package org.firstinspires.ftc.teamcode.utils.clients;

import android.util.Pair;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Params;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

public class TelemetryClient {
	public Telemetry telemetry;
	protected final Map < String , Pair< String , Integer >> data;
	protected int ID;
	public boolean showIndex;
	private static TelemetryClient instanceTelemetryClient;

	public TelemetryClient(final Telemetry telemetry){
		this.telemetry=telemetry;
		this.data = new HashMap<>();
//		update();
		TelemetryClient.instanceTelemetryClient =this;
	}

	public static TelemetryClient getInstance(){
		return TelemetryClient.instanceTelemetryClient;
	}

	public void clear(){
		this.data.clear();
		this.update();
	}

	/**
	 * 注意：这是新的Data
	 */
	public void addData(final String key, final String val){
		++ this.ID;
		this.data.put(key,new Pair<>(val, this.ID));
		this.update();
	}
	/**
	 * 注意：这是新的Data
	 */
	public void addData(final String key, final Object val){
		this.addData(key,String.valueOf(val));
	}
	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	public void deleteData(final String key){
		this.data.remove(key);
		this.update();
	}

	/**
	 * 自动创建新的行如果key所指向的值不存在
	 */
	public void changeData(final String key, final String val){
		if(this.data.containsKey(key)){
			this.data.replace(key,new Pair<>(val, (Objects.requireNonNull(this.data.get(key))).second));
		}else{
			this.addData(key, val);
		}
		this.update();
	}
	/**
	 * 自动创建新的行如果key所指向的值不存在
	 */
	public void changeData(final String key, final Object val){
		this.changeData(key,String.valueOf(val));
	}

	public void addLine(final String key){
		++ this.ID;
		this.data.put(key,new Pair<>("", this.ID));
		this.update();
	}
	public void addLine(final Object key){
		this.addLine(String.valueOf(key));
	}
	/**
	 * @throws RuntimeException 如果未能找到key所指向的值，将会抛出异常
	 */
	public void deleteLine(final String key){
		this.data.remove(key);
		this.update();
	}

	/**
	 * 将key行替代为val，自动创建新的行如果key所指向的值不存在
	 * @param oldData 目标行
	 * @param newData 替换行
	 */
	public void changeLine(final String oldData, final String newData){
		final int cache =Objects.requireNonNull(this.data.get(oldData)).second;
		this.data.remove(oldData);
		this.data.put(newData,new Pair<>("",cache));
		this.update();
	}

	public void update(){
		if(Params.Configs.sortDataInTelemetryClientUpdate) {
			final Vector <Pair <Integer, String>> outputData = new Vector <>();
			for (final Map.Entry <String, Pair <String, Integer>> i : this.data.entrySet()) {
				final String key = i.getKey();
				final String val = i.getValue().first;
				final Integer IDCache = i.getValue().second;
				if (! Objects.equals(i.getValue().first, "")) {//line
					outputData.add(new Pair <>(IDCache, key + ":" + val));
					if (Params.Configs.dashboardAutoSyncWithTelemetry) {
						DashboardClient.getInstance().put(key, val);
					}
				} else {//line
					outputData.add(new Pair <>(IDCache, key));
				}
			}
			outputData.sort(Comparator.comparingInt(x -> x.first));
			for (final Pair <Integer, String> outputLine : outputData) {
				if (this.showIndex) {
					this.telemetry.addLine("[" + outputLine.first + "]" + outputLine.second);
				} else {
					this.telemetry.addLine(outputLine.second);
				}
			}
			this.telemetry.update();
		}else{
			String cache;
			for (final Map.Entry<String, Pair<String, Integer>> entry : this.data.entrySet()) {
				final String                key = entry.getKey();
				final Pair<String, Integer> val = entry.getValue();
				cache= Objects.equals(val.first, "") ? val.first : key + ":" + val.first;
				if (this.showIndex) {
					this.telemetry.addLine("[" + val.second + "]" + cache);
				} else {
					this.telemetry.addLine(key + ":" + cache);
				}
				if(Params.Configs.dashboardAutoSyncWithTelemetry){
					DashboardClient.getInstance().put(key, val);
				}
			}
		}
	}
}

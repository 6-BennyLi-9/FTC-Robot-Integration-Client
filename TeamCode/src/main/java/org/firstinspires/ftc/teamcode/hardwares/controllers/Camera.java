package org.firstinspires.ftc.teamcode.hardwares.controllers;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.codes.samples.CameraDetection;
import org.firstinspires.ftc.teamcode.utils.enums.AutonomousLocation;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

/**
 * 我们这里使用的是OpenCV的颜色块识别，如果你有更多方案，可以进行修改，或者新建拉取请求
 * @see CameraDetection
 */
public class Camera extends OpenCvPipeline {
	private final Telemetry telemetry;
	private final Mat       mat     = new Mat();
	private final Mat       matRed  = new Mat();
	private final Mat       matBlue = new Mat();

	public Mat leftRed,leftBlue,middleRed,middleBlue,rightRed,rightBlue;
	public AutonomousLocation location = AutonomousLocation.failed;
	public double left_red_Value,middle_red_Value,right_red_Value,left_blue_Value,middle_blue_Value,right_blue_Value;

	//TODO: 区块识别的区域，需要更改
	static final Rect LEFT_ROI = new Rect(
			new Point(0, 0),
			new Point(0, 0));
	static final Rect  MIDDLE_ROI = new Rect(
			new Point(0,0),
			new Point(0,0));
	static final Rect RIGHT_ROI = new Rect(
			new Point(0,0),
			new Point(0,0));
	static double PERCENT_COLOR_THRESHOLD = 0.20;

	public Camera(final Telemetry telemetry) { this.telemetry = telemetry; }
	@Override
	public Mat processFrame(final Mat input) {
		Imgproc.cvtColor(input, this.mat, Imgproc.COLOR_RGB2HSV);

		//TODO:HSV颜色值 需要根据物体更改
		final Scalar redLowHSV  = new Scalar(0, 0, 0);
		final Scalar redHighHSV = new Scalar(0, 0, 0);

		final Scalar blueLowHSV  = new Scalar(0, 0, 0);
		final Scalar blueHighHSV = new Scalar(0, 0, 0);

		Core.inRange(this.mat, redLowHSV, redHighHSV, this.matRed);
		Core.inRange(this.mat, blueLowHSV, blueHighHSV, this.matBlue);

		this.leftRed = this.matRed.submat(Camera.LEFT_ROI);
		this.middleRed = this.matRed.submat(Camera.MIDDLE_ROI);
		this.rightRed = this.matRed.submat(Camera.RIGHT_ROI);

		this.leftBlue = this.matBlue.submat(Camera.LEFT_ROI);
		this.middleBlue = this.matBlue.submat(Camera.MIDDLE_ROI);
		this.rightBlue = this.matBlue.submat(Camera.RIGHT_ROI);

		this.left_red_Value = Core.sumElems(this.leftRed).val[0] / Camera.LEFT_ROI.area() / 255;
		this.middle_red_Value = Core.sumElems(this.middleRed).val[0] / Camera.MIDDLE_ROI.area() / 255;
		this.right_red_Value = Core.sumElems(this.rightRed).val[0] / Camera.RIGHT_ROI.area() / 255;

		this.left_blue_Value = Core.sumElems(this.leftBlue).val[0] / Camera.LEFT_ROI.area() / 255;
		this.middle_blue_Value = Core.sumElems(this.middleBlue).val[0] / Camera.MIDDLE_ROI.area() / 255;
		this.right_blue_Value = Core.sumElems(this.rightBlue).val[0] / Camera.RIGHT_ROI.area() / 255;


		this.leftRed.release();
		this.middleRed.release();
		this.rightRed.release();

		this.leftBlue.release();
		this.middleBlue.release();
		this.rightBlue.release();

		//输出roi范围内的概率
		final boolean LeftRed   = this.left_red_Value > Camera.PERCENT_COLOR_THRESHOLD;
		final boolean MiddleRed = this.middle_red_Value > Camera.PERCENT_COLOR_THRESHOLD;
		final boolean RightRed  = this.right_red_Value > Camera.PERCENT_COLOR_THRESHOLD;

		final boolean LeftBlue   = this.left_blue_Value > Camera.PERCENT_COLOR_THRESHOLD;
		final boolean MiddleBlue = this.middle_blue_Value > Camera.PERCENT_COLOR_THRESHOLD;
		final boolean RightBlue  = this.right_blue_Value > Camera.PERCENT_COLOR_THRESHOLD;


		if (LeftRed) {
			this.location = AutonomousLocation.left;
		} else if (MiddleRed) {
			this.location = AutonomousLocation.centre;
		} else if (RightRed) {
			this.location = AutonomousLocation.right;
		} else if (LeftBlue) {
			this.location = AutonomousLocation.left;
		} else if (MiddleBlue) {
			this.location = AutonomousLocation.centre;
		} else if (RightBlue) {
			this.location = AutonomousLocation.right;
		}

		//Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);

		final Scalar color = new Scalar(255, 0, 0);
		Imgproc.rectangle(this.mat, Camera.LEFT_ROI, color);
		Imgproc.rectangle(this.mat, Camera.MIDDLE_ROI, color);
		Imgproc.rectangle(this.mat, Camera.RIGHT_ROI, color);
		return this.mat;
	}

	public AutonomousLocation getLocation() {
		return this.location;
	}

	/**
	 * 用于测试OpenCV的运行情况
	 */
	public void showRoiVP(){
		this.telemetry.addData("leftRed roi raw value", (int) Core.sumElems(this.leftRed).val[0]);
		this.telemetry.addData("middleRed roi raw value", (int) Core.sumElems(this.middleRed).val[0]);
		this.telemetry.addData("rightRed roi raw value", (int) Core.sumElems(this.rightRed).val[0]);
		this.telemetry.addData("leftRed roi percentage", Math.round(this.left_red_Value * 100) + "%");
		this.telemetry.addData("middleRed roi percentage", Math.round(this.middle_red_Value * 100) + "%");
		this.telemetry.addData("rightRed roi percentage", Math.round(this.right_red_Value * 100) + "%");

		this.telemetry.addData("leftBlue roi raw value", (int) Core.sumElems(this.leftBlue).val[0]);
		this.telemetry.addData("middleBlue roi raw value", (int) Core.sumElems(this.middleBlue).val[0]);
		this.telemetry.addData("rightBlue roi raw value", (int) Core.sumElems(this.rightBlue).val[0]);
		this.telemetry.addData("leftBlue roi percentage", Math.round(this.left_blue_Value * 100) + "%");
		this.telemetry.addData("middleBlue roi percentage", Math.round(this.middle_blue_Value * 100) + "%");
		this.telemetry.addData("rightBlue roi percentage", Math.round(this.right_blue_Value * 100) + "%");
	}
}
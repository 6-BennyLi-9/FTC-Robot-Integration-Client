package org.firstinspires.ftc.teamcode.Hardwares.basic;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.enums.AutonomousLocation;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

/**
 * @see org.firstinspires.ftc.teamcode.RIC_samples.CameraDetection
 */
public class Camera extends OpenCvPipeline {
	private final Telemetry telemetry;
	private final Mat       mat     = new Mat();
	private final Mat       matRed  = new Mat();
	private final Mat       matBlue = new Mat();

	public Mat leftRed,leftBlue,middleRed,middleBlue,rightRed,rightBlue;
	public AutonomousLocation location = AutonomousLocation.failed;
	public double left_red_Value,middle_red_Value,right_red_Value,left_blue_Value,middle_blue_Value,right_blue_Value;

	static final Rect LEFT_ROI = new Rect(
			new Point(0, 0),//原x=110,y=305
			new Point(0, 0));//原x=300，y=560
	static final Rect  MIDDLE_ROI = new Rect(
			new Point(0,0),//原x=610，y=280
			new Point(0,0));//原x=690，y=490
	static final Rect RIGHT_ROI = new Rect(
			new Point(0,0),//原x=1050，y=295
			new Point(0,0));//原x=1240，y=570
	static double PERCENT_COLOR_THRESHOLD = 0.20;

	public Camera(Telemetry telemetry) { this.telemetry = telemetry; }
	@Override
	public Mat processFrame(Mat input) {
		Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

		//TODO:HSV颜色值 需要根据物体更改
		Scalar redLowHSV = new Scalar(0, 0, 0);
		Scalar redHighHSV = new Scalar(0, 0, 0);

		Scalar blueLowHSV = new Scalar(0, 0, 0);
		Scalar blueHighHSV = new Scalar(0, 0, 0);

		Core.inRange(mat, redLowHSV, redHighHSV, matRed);
		Core.inRange(mat, blueLowHSV, blueHighHSV, matBlue);

		leftRed = matRed.submat(LEFT_ROI);
		middleRed = matRed.submat(MIDDLE_ROI);
		rightRed = matRed.submat(RIGHT_ROI);

		leftBlue = matBlue.submat(LEFT_ROI);
		middleBlue = matBlue.submat(MIDDLE_ROI);
		rightBlue = matBlue.submat(RIGHT_ROI);

		left_red_Value = Core.sumElems(leftRed).val[0] / LEFT_ROI.area() / 255;
		middle_red_Value = Core.sumElems(middleRed).val[0] / MIDDLE_ROI.area() / 255;
		right_red_Value = Core.sumElems(rightRed).val[0] / RIGHT_ROI .area() / 255;

		left_blue_Value = Core.sumElems(leftBlue).val[0] / LEFT_ROI.area() / 255;
		middle_blue_Value = Core.sumElems(middleBlue).val[0] / MIDDLE_ROI.area() / 255;
		right_blue_Value = Core.sumElems(rightBlue).val[0] / RIGHT_ROI .area() / 255;


		leftRed.release();
		middleRed.release();
		rightRed.release();

		leftBlue.release();
		middleBlue.release();
		rightBlue.release();

		//输出roi范围内的概率
		boolean LeftRed = left_red_Value > PERCENT_COLOR_THRESHOLD;
		boolean MiddleRed = middle_red_Value > PERCENT_COLOR_THRESHOLD;
		boolean RightRed = right_red_Value > PERCENT_COLOR_THRESHOLD;

		boolean LeftBlue = left_blue_Value > PERCENT_COLOR_THRESHOLD;
		boolean MiddleBlue= middle_blue_Value > PERCENT_COLOR_THRESHOLD;
		boolean RightBlue = right_blue_Value > PERCENT_COLOR_THRESHOLD;


		if (LeftRed) {
			location = AutonomousLocation.left;
		} else if (MiddleRed) {
			location = AutonomousLocation.centre;
		} else if (RightRed) {
			location = AutonomousLocation.right;
		} else if (LeftBlue) {
			location = AutonomousLocation.left;
		} else if (MiddleBlue) {
			location = AutonomousLocation.centre;
		} else if (RightBlue) {
			location = AutonomousLocation.right;
		}

		//Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);

		Scalar color = new Scalar(255, 0, 0);
		Imgproc.rectangle(mat, LEFT_ROI, color);
		Imgproc.rectangle(mat, MIDDLE_ROI, color);
		Imgproc.rectangle(mat, RIGHT_ROI, color);
		return mat;
	}

	public AutonomousLocation getLocation() {
		return location;
	}

	/**
	 * 用于测试OpenCV的运行情况
	 */
	public void showRoiVP(){
		telemetry.addData("leftRed roi raw value", (int) Core.sumElems(leftRed).val[0]);
		telemetry.addData("middleRed roi raw value", (int) Core.sumElems(middleRed).val[0]);
		telemetry.addData("rightRed roi raw value", (int) Core.sumElems(rightRed).val[0]);
		telemetry.addData("leftRed roi percentage", Math.round(left_red_Value * 100) + "%");
		telemetry.addData("middleRed roi percentage", Math.round(middle_red_Value * 100) + "%");
		telemetry.addData("rightRed roi percentage", Math.round(right_red_Value * 100) + "%");

		telemetry.addData("leftBlue roi raw value", (int) Core.sumElems(leftBlue).val[0]);
		telemetry.addData("middleBlue roi raw value", (int) Core.sumElems(middleBlue).val[0]);
		telemetry.addData("rightBlue roi raw value", (int) Core.sumElems(rightBlue).val[0]);
		telemetry.addData("leftBlue roi percentage", Math.round(left_blue_Value * 100) + "%");
		telemetry.addData("middleBlue roi percentage", Math.round(middle_blue_Value * 100) + "%");
		telemetry.addData("rightBlue roi percentage", Math.round(right_blue_Value * 100) + "%");
	}
}
package org.firstinspires.ftc.teamcode.hardwares;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.codes.samples.CameraDetection;
import org.firstinspires.ftc.teamcode.hardwares.controllers.Camera;
import org.firstinspires.ftc.teamcode.utils.enums.AutonomousLocation;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

/**
 * 集成化的摄像头程序
 * <p>
 * 今年（2024-2025）赛季中摄像头的应用变少了，因此用途不多了
 *
 * @see CameraDetection
 */
public class Webcam {
	public Camera detector;
	public OpenCvCamera camera;

	public static boolean useWebcam;//Webcam is useless in 2024-2025 season

	public Webcam(@NonNull final HardwareMap hardwareMap){
		if(! useWebcam)return;

		//TODO：根据需要更改名称
		final int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		this.camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
		this.camera.setPipeline(this.detector);
		this.Init();
	}

	private void Init(){
		this.camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
		{
			@Override
			public void onOpened()
			{
				Webcam.this.camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
			}

			@Override
			public void onError(final int errorCode) {
				throw new RuntimeException(String.valueOf(errorCode));
			}
		});
	}

	public AutonomousLocation getLocation(){
		return this.detector.getLocation();
	}
	public void showRoiVP(){
		this.detector.showRoiVP();
	}
}

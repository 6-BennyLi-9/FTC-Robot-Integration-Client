package org.firstinspires.ftc.teamcode.hardwares;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.codes.samples.CameraDetection;
import org.firstinspires.ftc.teamcode.hardwares.basic.Camera;
import org.firstinspires.ftc.teamcode.utils.enums.AutonomousLocation;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

/**
 * @see CameraDetection
 */
public class Webcam {
	public Camera detector;
	public OpenCvCamera camera;

	public static boolean useWebcam=false;//Webcam is useless in 2024-2025 season

	public Webcam(@NonNull HardwareMap hardwareMap){
		if(!useWebcam)return;

		//TODO：根据需要更改名称
		int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
		camera.setPipeline(detector);
		Init();
	}

	private void Init(){
		camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
		{
			@Override
			public void onOpened()
			{
				camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
			}

			@Override
			public void onError(int errorCode) {
				throw new RuntimeException(String.valueOf(errorCode));
			}
		});
	}

	public AutonomousLocation getLocation(){
		return detector.getLocation();
	}
	public void showRoiVP(){
		detector.showRoiVP();
	}
}

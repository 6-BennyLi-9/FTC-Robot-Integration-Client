package org.firstinspires.ftc.teamcode.hardwares;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.hardwares.basic.Camera;
import org.firstinspires.ftc.teamcode.utils.enums.AutonomousLocation;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

/**
 * @see org.firstinspires.ftc.teamcode.samples.CameraDetection
 */
public class Webcam {
	Camera detector;
	OpenCvCamera camera;

	public Webcam(@NonNull HardwareMap hardwareMap){
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

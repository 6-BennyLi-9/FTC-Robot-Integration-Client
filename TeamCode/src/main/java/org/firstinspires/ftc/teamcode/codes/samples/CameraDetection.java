package org.firstinspires.ftc.teamcode.codes.samples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.hardwares.basic.Camera;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Disabled
@Autonomous(name = "CameraDetection",group = Params.Configs.SampleOpModesGroup)
public class CameraDetection extends AutonomousProgramTemplate {
	OpenCvCamera webcam;
	Camera detector=new Camera(telemetry);


	@Override
	public void runOpMode() {
		//TODO：根据需要更改名称
		int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
		webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
		webcam.setPipeline(detector);
		webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
		{
			@Override
			public void onOpened()
			{
				webcam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
			}

			@Override
			public void onError(int errorCode) {
				throw new RuntimeException(String.valueOf(errorCode));
			}
		});

		telemetry.addLine("Waiting for start");
		telemetry.update();

		while (!isStopRequested())
		{
			switch (detector.getLocation()) {
				case left:
					telemetry.addData("Location", "LEFT");
					telemetry.update();
					break;
				case centre:
					telemetry.addData("Location", "CENTRE");
					telemetry.update();
					break;
				case right:
					telemetry.addData("Location", "RIGHT");
					telemetry.update();
					break;
				case failed:
					telemetry.addData("Location", "failed");
					telemetry.update();
					break;
			}
			sleep(50);
		}
	}
}

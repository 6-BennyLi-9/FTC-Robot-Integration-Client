package org.firstinspires.ftc.teamcode.codes.samples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Params;
import org.firstinspires.ftc.teamcode.codes.templates.AutonomousProgramTemplate;
import org.firstinspires.ftc.teamcode.hardwares.controllers.Camera;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Disabled
@Autonomous(name = "CameraDetection",group = Params.Configs.SampleOpModesGroup)
public class CameraDetection extends AutonomousProgramTemplate {
	OpenCvCamera webcam;
	Camera detector=new Camera(this.telemetry);


	@Override
	public void runOpMode() {
		//TODO：根据需要更改名称
		final int cameraMonitorViewId = this.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", this.hardwareMap.appContext.getPackageName());
		this.webcam = OpenCvCameraFactory.getInstance().createWebcam(this.hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
		this.webcam.setPipeline(this.detector);
		this.webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
		{
			@Override
			public void onOpened()
			{
				CameraDetection.this.webcam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
			}

			@Override
			public void onError(final int errorCode) {
				throw new RuntimeException(String.valueOf(errorCode));
			}
		});

		this.telemetry.addLine("Waiting for start");
		this.telemetry.update();

		while (! this.isStopRequested())
		{
			switch (this.detector.getLocation()) {
				case left:
					this.telemetry.addData("Location", "LEFT");
					this.telemetry.update();
					break;
				case centre:
					this.telemetry.addData("Location", "CENTRE");
					this.telemetry.update();
					break;
				case right:
					this.telemetry.addData("Location", "RIGHT");
					this.telemetry.update();
					break;
				case failed:
					this.telemetry.addData("Location", "failed");
					this.telemetry.update();
					break;
			}
			this.sleep(50);
		}
	}
}

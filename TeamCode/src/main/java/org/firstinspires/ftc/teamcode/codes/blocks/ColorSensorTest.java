package org.firstinspires.ftc.teamcode.codes.blocks;

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Light;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "ColorSensorTest (Blocks to Java)", group = "Sensor")
public class ColorSensorTest extends LinearOpMode {

	public ColorSensor colorSensor;
	public DistanceSensor colorSensor_DistanceSensor;

	/**
	 * This OpMode shows how to use a color sensor in a generic way, regardless
	 * of which particular make or model of color sensor is used. The OpMode
	 * assumes that the color sensor is configured with a name of "sensor_color".
	 * <p>
	 * There will be some variation in the values measured depending on the specific sensor you are using.
	 * <p>
	 * You can increase the gain (a multiplier to make the sensor report
	 * higher values) by holding down the A button on the gamepad,
	 * and decrease the gain by holding down the B button on the gamepad.
	 * <p>
	 * If the color sensor has a light which is controllable from software,
	 * you can use the X button on the gamepad to toggle the light on and off.
	 * The REV sensors don't support this, but instead have a physical switch on
	 * them to turn the light on and off, beginning with REV Color Sensor V2.
	 * <p>
	 * If the color sensor also supports short-range distance measurements (usually via
	 * an infrared proximity sensor), the reported distance will be written to telemetry.
	 * As of September 2020, the only color sensors that support this are the ones from
	 * REV Robotics. These infrared proximity sensor measurements are only useful at very
	 * small distances, and are sensitive to ambient light and surface reflectivity.
	 * You should use a different sensor if you need precise distance measurements.
	 */
	@Override
	public void runOpMode() {
		int gain;
		boolean xButtonCurrentlyPressed;
		boolean xButtonPreviouslyPressed;
		NormalizedRGBA myNormalizedColors;
		int myColor;
		float hue;
		float saturation;
		float value;

		colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
		colorSensor_DistanceSensor = hardwareMap.get(DistanceSensor.class, "colorSensor");

		// Put initialization blocks here.
		// You can give the sensor a gain value, will be multiplied by the sensor's raw value
		// before the normalized color values are calculated. Color sensors (especially the REV
		// Color Sensor V3) can give very low values (depending on the lighting conditions),
		// which only use a small part of the 0-1 range that is available for the red,
		// green, and blue values. In brighter conditions, you should use a smaller
		// gain than in dark conditions. If your gain is too high, all of the
		// colors will report at or near 1, and you won't be able to determine what
		// color you are actually looking at. For this reason, it's better to err
		// on the side of a lower gain (but always greater than or equal to 1).
		gain = 2;
		// xButtonPreviouslyPressed and xButtonCurrentlyPressed keep track
		// of the previous and current state of the X button on the gamepad.
		xButtonPreviouslyPressed = false;
		// If supported by the sensor, turn the light on in the beginning (it
		// might already be on anyway, we just make sure it is if we can).
		colorSensor.enableLed(true);
		waitForStart();
		if (opModeIsActive()) {
			// Put run blocks here.
			// Once per loop we read the color sensor data, calculate the HSV colors
			// (Hue, Saturation and Value), and report all these values via telemetry.
			while (opModeIsActive()) {
				// Put loop blocks here.
				telemetry.addLine("Hold the A button on gamepad 1 to increase gain, or B to decrease it.");
				telemetry.addLine(" ");
				telemetry.addLine("Higher gain values mean that the sensor will report larger numbers for Red, Green, and Blue, and Value.");
				telemetry.addLine(" ");
				telemetry.addLine("Press the X button to turn the color sensor's LED on or off (if supported).");
				telemetry.addLine(" ");
				// Update the gain value if either of the A or B gamepad buttons is being held
				// A gain of less than 1 will make the values smaller, which is not helpful.
				if (gamepad1.a) {
					// Only increase the gain by a small amount, since this loop will occur multiple times per second.
					gain = (int) (gain + 0.005);
				} else if (gamepad1.b && gain > 1) {
					gain = (int) (gain - 0.005);
				}
				telemetry.addData("Gain", gain);
				// Tell the sensor our desired gain value (normally you would do this during initialization, not during the loop)
				((NormalizedColorSensor) colorSensor).setGain(gain);
				xButtonCurrentlyPressed = gamepad1.x;
				// If the button state is different than what it was, then act
				// to turn the color sensor's light on or off (if supported).
				if (xButtonCurrentlyPressed != xButtonPreviouslyPressed) {
					if (xButtonCurrentlyPressed) {
						// If the button is (now) down, then toggle the light
						colorSensor.enableLed(!((Light) colorSensor).isLightOn());
					}
				}
				xButtonPreviouslyPressed = xButtonCurrentlyPressed;
				// Save the color sensor data as a normalized color value. It's recommended
				// to use Normalized Colors over color sensor colors is because Normalized
				// Colors consistently gives values between 0 and 1, while the direct
				// Color Sensor colors are dependent on the specific sensor you're using.
				myNormalizedColors = ((NormalizedColorSensor) colorSensor).getNormalizedColors();
				// Convert the normalized color values to an Android color value.
				myColor = myNormalizedColors.toColor();
				// Use the Android color value to calculate the Hue, Saturation and Value color variables.
				// See http://web.archive.org/web/20190311170843/https://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html for an explanation of HSV color.
				hue = JavaUtil.rgbToHue(Color.red(myColor), Color.green(myColor), Color.blue(myColor));
				saturation = JavaUtil.rgbToSaturation(Color.red(myColor), Color.green(myColor), Color.blue(myColor));
				value = JavaUtil.rgbToValue(Color.red(myColor), Color.green(myColor), Color.blue(myColor));
				// Use telemetry to display feedback on the driver station. We show the red,
				// green, and blue normalized values from the sensor (in the range of 0 to
				// 1), as well as the equivalent HSV (hue, saturation and value) values.
				telemetry.addLine("Red " + JavaUtil.formatNumber(myNormalizedColors.red, 3) + " | Green " + JavaUtil.formatNumber(myNormalizedColors.green, 3) + " | Blue " + JavaUtil.formatNumber(myNormalizedColors.blue, 3));
				telemetry.addLine("Hue " + JavaUtil.formatNumber(hue, 3) + " | Saturation " + JavaUtil.formatNumber(saturation, 3) + " | Value " + JavaUtil.formatNumber(value, 3));
				telemetry.addData("Alpha", Double.parseDouble(JavaUtil.formatNumber(myNormalizedColors.alpha, 3)));
				// If this color sensor also has a distance sensor, display the measured distance.
				// Note that the reported distance is only useful at very close
				// range, and is impacted by ambient light and surface reflectivity.
				telemetry.addData("Distance (cm)", Double.parseDouble(JavaUtil.formatNumber(colorSensor_DistanceSensor.getDistance(DistanceUnit.CM), 3)));
				telemetry.update();
			}
		}
	}
}

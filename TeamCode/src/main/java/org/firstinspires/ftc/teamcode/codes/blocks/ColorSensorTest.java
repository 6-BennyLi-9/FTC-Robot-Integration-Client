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
	 * 此 OpMode 展示了如何使用颜色传感器以及使用哪个特定品牌或型号的颜色传感器。
	 * 此 OpMode 假设颜色传感器配置了 “sensor_color” 名称。
	 * <p>
	 * 根据您使用的特定传感器，测量值会有一些变化。
	 * <p>
	 * 您可以增加增益（使传感器报告的乘数更高的值）通过按住游戏手柄上的 A 按钮，
	 * 并通过按住游戏手柄上的 B 按钮来降低增益。
	 * <p>
	 * 如果颜色传感器具有可通过软件控制的灯，您可以使用游戏手柄上的 X 按钮来打开和关闭灯。
	 * REV 传感器不支持此功能，而是打开了一个物理开关它们用于打开和关闭灯（从 REV 颜色传感器 V2 开始）
	 * <p>
	 * 如果颜色传感器还支持短距离距离测量（通常通过红外接近传感器），报告的距离将被写入遥测数据。
	 * 截至 2020 年 9 月，唯一支持此功能的色彩传感器是REV 机器人。这些红外接近传感器测量仅在非常距离小，对环境光和表面反射率敏感。
	 * 如果您需要精确的距离测量，您应该使用不同的传感器。
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

		// 您可以为传感器提供一个增益值，该值将乘以传感器的原始值
		// 在计算标准化颜色值之前。颜色传感器（尤其是 REV
		// 颜色传感器 V3） 可以给出非常低的值（取决于照明条件），
		// 它们只使用可用于红色的 0-1 范围的一小部分，
		// green 和 blue 值。在较亮的条件下，您应该使用较小的
		// 增益比在黑暗条件下。如果您的增益过高，则所有
		// 颜色将报告在 1 或附近，您将无法确定是什么
		// 您实际正在查看的颜色。因此，最好犯错
		// 在较低增益的一侧（但始终大于或等于 1）。
		gain = 2;
		// xButtonPreviouslyPressed 和 xButtonCurrentlyPressed 跟踪游戏手柄上 X 按钮的先前和当前状态。
		xButtonPreviouslyPressed = false;
		//如果传感器支持，请先打开灯（它可能已经打开了，我们只是确保如果可以的话）。
		colorSensor.enableLed(true);
		waitForStart();
		if (opModeIsActive()) {
			// 每个循环一次，我们读取颜色传感器数据，计算 HSV 颜色（色相、饱和度和值），并通过遥测报告所有这些值。
			while (opModeIsActive()) {
				// Put loop blocks here.
				telemetry.addLine("Hold the A button on gamepad 1 to increase gain, or B to decrease it.");
				telemetry.addLine(" ");
				telemetry.addLine("Higher gain values mean that the sensor will report larger numbers for Red, Green, and Blue, and Value.");
				telemetry.addLine(" ");
				telemetry.addLine("Press the X button to turn the color sensor's LED on or off (if supported).");
				telemetry.addLine(" ");
				// 如果按住 A 或 B 游戏手柄按钮，则更新增益值小于 1 的增益将使值变小，这没有帮助。
				if (gamepad1.a) {
					// 仅少量增加增益，因为此循环每秒将发生多次。
					gain = (int) (gain + 0.005);
				} else if (gamepad1.b && gain > 1) {
					gain = (int) (gain - 0.005);
				}
				telemetry.addData("Gain", gain);
				// 告诉传感器我们所需的增益值（通常您会在初始化期间执行此操作，而不是在循环期间执行此操作）
				((NormalizedColorSensor) colorSensor).setGain(gain);
				xButtonCurrentlyPressed = gamepad1.x;
				// 如果按钮状态与原来的状态不同，则执行操作以打开或关闭颜色传感器的指示灯（如果支持）。
				if (xButtonCurrentlyPressed != xButtonPreviouslyPressed) {
					if (xButtonCurrentlyPressed) {
						// 如果按钮（现在）按下，则切换灯
						colorSensor.enableLed(!((Light) colorSensor).isLightOn());
					}
				}
				xButtonPreviouslyPressed = xButtonCurrentlyPressed;
				// 将颜色传感器数据保存为标准化颜色值。推荐将 Normalized Colors （标准化颜色） 用于颜色传感器颜色是因为 Normalized （标准化）
				// Colors 始终提供介于 0 和 1 之间的值，而直接的 Color Sensor 颜色取决于您使用的特定传感器。
				myNormalizedColors = ((NormalizedColorSensor) colorSensor).getNormalizedColors();
				// 将标准化颜色值转换为 Android 颜色值。
				myColor = myNormalizedColors.toColor();
				// 使用 Android 颜色值计算 Hue、Saturation 和 Value 颜色变量。
				// 有关 HSV 颜色的说明，请参阅 http://web.archive.org/web/20190311170843/https://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html。
				hue = JavaUtil.rgbToHue(Color.red(myColor), Color.green(myColor), Color.blue(myColor));
				saturation = JavaUtil.rgbToSaturation(Color.red(myColor), Color.green(myColor), Color.blue(myColor));
				value = JavaUtil.rgbToValue(Color.red(myColor), Color.green(myColor), Color.blue(myColor));
				// 使用遥测技术在驾驶员工作站上显示反馈。我们展示红色，绿色和蓝色标准化传感器的值（在 0 到 1） 以及等效的 HSV（色相、饱和度和值）值。
				telemetry.addLine("Red " + JavaUtil.formatNumber(myNormalizedColors.red, 3) + " | Green " + JavaUtil.formatNumber(myNormalizedColors.green, 3) + " | Blue " + JavaUtil.formatNumber(myNormalizedColors.blue, 3));
				telemetry.addLine("Hue " + JavaUtil.formatNumber(hue, 3) + " | Saturation " + JavaUtil.formatNumber(saturation, 3) + " | Value " + JavaUtil.formatNumber(value, 3));
				telemetry.addData("Alpha", Double.parseDouble(JavaUtil.formatNumber(myNormalizedColors.alpha, 3)));
				// 如果此颜色传感器还具有距离传感器，则显示测量的距离。
				// 请注意，报告的距离仅在非常近时有用范围，并受环境光和表面反射率的影响。
				telemetry.addData("Distance (cm)", Double.parseDouble(JavaUtil.formatNumber(colorSensor_DistanceSensor.getDistance(DistanceUnit.CM), 3)));
				telemetry.update();
			}
		}
	}
}

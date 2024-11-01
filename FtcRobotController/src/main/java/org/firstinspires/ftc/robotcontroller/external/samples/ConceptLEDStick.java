package org.firstinspires.ftc.robotcontroller.external.samples;
/*
        Copyright (c) 2021-24 Alan Smith

        All rights reserved.

        Redistribution and use in source and binary forms, with or without modification,
        are permitted (subject to the limitations in the disclaimer below) provided that
        the following conditions are met:

        Redistributions of source code must retain the above copyright notice, this list
        of conditions and the following disclaimer.

        Redistributions in binary form must reproduce the above copyright notice, this
        list of conditions and the following disclaimer in the documentation and/or
        other materials provided with the distribution.

        Neither the name of Alan Smith nor the names of its contributors may be used to
        endorse or promote products derived from this software without specific prior
        written permission.

        NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
        LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
        "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
        THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
        ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
        FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
        DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
        SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
        CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
        TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
        THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

import android.graphics.Color;

import com.qualcomm.hardware.sparkfun.SparkFunLEDStick;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

/*
 * This OpMode illustrates how to use the SparkFun QWIIC LED Strip
 *
 * This is a simple way to add a strip of 10 LEDs to your robot where you can set the color of each
 * LED or the whole strip.   This allows for driver feedback or even just fun ways to show your team
 * colors.
 *
 * Why?
 * Because more LEDs == more fun!!
 *
 * This OpMode assumes that the QWIIC LED Stick is attached to an I2C interface named "back_leds" in the robot configuration.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 *
 * You can buy this product here:  https://www.sparkfun.com/products/18354
 * Don't forget to also buy this to make it easy to connect to your Control or Expansion Hub:
 * https://www.sparkfun.com/products/25596
 */
@TeleOp(name = "Concept: LED Stick", group = "Concept")
@Disabled
public class ConceptLEDStick extends OpMode {
    private boolean wasUp;
    private boolean wasDown;
    private int brightness = 5;  // this needs to be between 0 and 31
    private final static double END_GAME_TIME = 120 - 30;

    private SparkFunLEDStick ledStick;

    @Override
    public void init() {
	    this.ledStick = this.hardwareMap.get(SparkFunLEDStick.class, "back_leds");
	    this.ledStick.setBrightness(this.brightness);
	    this.ledStick.setColor(Color.GREEN);
    }

    @Override
    public void start() {
	    this.resetRuntime();
    }

    @Override
    public void loop() {
	    this.telemetry.addLine("Hold the A button to turn blue");
	    this.telemetry.addLine("Hold the B button to turn red");
	    this.telemetry.addLine("Hold the left bumper to turn off");
	    this.telemetry.addLine("Use DPAD Up/Down to change brightness");
        
        if (END_GAME_TIME < getRuntime()) {
            final int[] ledColors = {Color.RED, Color.YELLOW, Color.RED, Color.YELLOW, Color.RED,
                                     Color.YELLOW, Color.RED, Color.YELLOW, Color.RED, Color.YELLOW};
	        this.ledStick.setColors(ledColors);
        } else if (this.gamepad1.a) {
	        this.ledStick.setColor(Color.BLUE);
        } else if (this.gamepad1.b) {
	        this.ledStick.setColor(Color.RED);
        } else if (this.gamepad1.left_bumper) {
	        this.ledStick.turnAllOff();
        } else {
	        this.ledStick.setColor(Color.GREEN);
        }

        /*
         * Use DPAD up and down to change brightness
         */
        int newBrightness = this.brightness;
        if (this.gamepad1.dpad_up && ! this.wasUp) {
            newBrightness = this.brightness + 1;
        } else if (this.gamepad1.dpad_down && ! this.wasDown) {
            newBrightness = this.brightness - 1;
        }
        if (newBrightness != this.brightness) {
	        this.brightness = Range.clip(newBrightness, 0, 31);
	        this.ledStick.setBrightness(this.brightness);
        }
	    this.telemetry.addData("Brightness", this.brightness);

	    this.wasDown = this.gamepad1.dpad_down;
	    this.wasUp = this.gamepad1.dpad_up;
    }
}

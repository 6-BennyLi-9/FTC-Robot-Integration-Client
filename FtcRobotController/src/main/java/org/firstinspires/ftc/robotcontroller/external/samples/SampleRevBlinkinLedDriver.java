/*
 * Copyright (c) 2018 Craig MacFarlane
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * (subject to the limitations in the disclaimer below) provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * Neither the name of Craig MacFarlane nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS
 * SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

/*
 * This OpMode demonstrates use of the REV Robotics Blinkin LED Driver.
 * AUTO mode cycles through all of the patterns.
 * MANUAL mode allows the user to manually change patterns using the
 * left and right bumpers of a gamepad.
 *
 * Configure the driver on a servo port, and name it "blinkin".
 *
 * Displays the first pattern upon init.
 */
@TeleOp(name="BlinkinExample")
@Disabled
public class SampleRevBlinkinLedDriver extends OpMode {

    /*
     * Change the pattern every 10 seconds in AUTO mode.
     */
    private final static int LED_PERIOD = 10;

    /*
     * Rate limit gamepad button presses to every 500ms.
     */
    private final static int GAMEPAD_LOCKOUT = 500;

    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;

    Telemetry.Item patternName;
    Telemetry.Item display;
    DisplayKind displayKind;
    Deadline ledCycleDeadline;
    Deadline gamepadRateLimit;

    protected enum DisplayKind {
        MANUAL,
        AUTO
    }

    @Override
    public void init()
    {
	    this.displayKind = DisplayKind.AUTO;

	    this.blinkinLedDriver = this.hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
	    this.pattern = RevBlinkinLedDriver.BlinkinPattern.RAINBOW_RAINBOW_PALETTE;
	    this.blinkinLedDriver.setPattern(this.pattern);

	    this.display = this.telemetry.addData("Display Kind: ", this.displayKind.toString());
	    this.patternName = this.telemetry.addData("Pattern: ", this.pattern.toString());

	    this.ledCycleDeadline = new Deadline(LED_PERIOD, TimeUnit.SECONDS);
	    this.gamepadRateLimit = new Deadline(GAMEPAD_LOCKOUT, TimeUnit.MILLISECONDS);
    }

    @Override
    public void loop()
    {
	    this.handleGamepad();

        if (DisplayKind.AUTO == displayKind) {
	        this.doAutoDisplay();
        } else {
            /*
             * MANUAL mode: Nothing to do, setting the pattern as a result of a gamepad event.
             */
        }
    }

    /*
     * handleGamepad
     *
     * Responds to a gamepad button press.  Demonstrates rate limiting for
     * button presses.  If loop() is called every 10ms and and you don't rate
     * limit, then any given button press may register as multiple button presses,
     * which in this application is problematic.
     *
     * A: Manual mode, Right bumper displays the next pattern, left bumper displays the previous pattern.
     * B: Auto mode, pattern cycles, changing every LED_PERIOD seconds.
     */
    protected void handleGamepad()
    {
        if (! this.gamepadRateLimit.hasExpired()) {
            return;
        }

        if (this.gamepad1.a) {
	        this.setDisplayKind(DisplayKind.MANUAL);
	        this.gamepadRateLimit.reset();
        } else if (this.gamepad1.b) {
	        this.setDisplayKind(DisplayKind.AUTO);
	        this.gamepadRateLimit.reset();
        } else if ((DisplayKind.MANUAL == displayKind) && (this.gamepad1.left_bumper)) {
	        this.pattern = this.pattern.previous();
	        this.displayPattern();
	        this.gamepadRateLimit.reset();
        } else if ((DisplayKind.MANUAL == displayKind) && (this.gamepad1.right_bumper)) {
	        this.pattern = this.pattern.next();
	        this.displayPattern();
	        this.gamepadRateLimit.reset();
        }
    }

    protected void setDisplayKind(final DisplayKind displayKind)
    {
        this.displayKind = displayKind;
	    this.display.setValue(displayKind.toString());
    }

    protected void doAutoDisplay()
    {
        if (this.ledCycleDeadline.hasExpired()) {
	        this.pattern = this.pattern.next();
	        this.displayPattern();
	        this.ledCycleDeadline.reset();
        }
    }

    protected void displayPattern()
    {
	    this.blinkinLedDriver.setPattern(this.pattern);
	    this.patternName.setValue(this.pattern.toString());
    }
}

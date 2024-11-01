/*
 * Copyright (c) 2024 DigitalChickenLabs
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;
import com.qualcomm.hardware.digitalchickenlabs.OctoQuadBase;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.Stack;

/*
 * This OpMode illustrates how to use the DigitalChickenLabs OctoQuad Quadrature Encoder & Pulse Width Interface Module.
 *
 *   The OctoQuad has 8 input channels that can used to read either Relative Quadrature Encoders or Pulse-Width Absolute Encoder inputs.
 *   Relative Quadrature encoders are found on most FTC motors, and some stand-alone position sensors like the REV Thru-Bore encoder.
 *   Pulse-Width encoders are less common. The REV Thru-Bore encoder can provide its absolute position via a variable pulse width,
 *   as can several sonar rangefinders such as the MaxBotix MB1000 series.
 *
 * This OpMode assumes that the OctoQuad is attached to an I2C interface named "octoquad" in the robot configuration.
 *
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 *
 * Select, Init and run the "OctoQuad Configuration Tool" OpMode
 * Read the blue User-Interface tips at the top of the telemetry screen.
 * Use the UI buttons to navigate the menu and make any desired changes to the OctoQuad configuration.
 * Use the Program Settings To FLASH option to make any changes permanent.
 *
 * See the sensor's product page: https://www.tindie.com/products/digitalchickenlabs/octoquad-8ch-quadrature-pulse-width-decoder/
 */
@TeleOp(name = "OctoQuad Configuration Tool", group="OctoQuad")
@Disabled
public class UtilityOctoQuadConfigMenu extends LinearOpMode
{
    TelemetryMenu.MenuElement rootMenu = new TelemetryMenu.MenuElement("OctoQuad Config Menu", true);
    TelemetryMenu.MenuElement menuHwInfo = new TelemetryMenu.MenuElement("Hardware Information", false);
    TelemetryMenu.EnumOption optionI2cResetMode;
    TelemetryMenu.EnumOption optionChannelBankConfig;

    TelemetryMenu.MenuElement menuEncoderDirections = new TelemetryMenu.MenuElement("Set Encoder Directions", false);
    TelemetryMenu.BooleanOption[] optionsEncoderDirections = new TelemetryMenu.BooleanOption[OctoQuadBase.NUM_ENCODERS];

    TelemetryMenu.MenuElement menuVelocityIntervals = new TelemetryMenu.MenuElement("Velocity Measurement Intervals", false);
    TelemetryMenu.IntegerOption[] optionsVelocityIntervals = new TelemetryMenu.IntegerOption[OctoQuadBase.NUM_ENCODERS];

    TelemetryMenu.MenuElement menuAbsParams = new TelemetryMenu.MenuElement("Abs. Encoder Pulse Width Params", false);
    TelemetryMenu.IntegerOption[] optionsAbsParamsMax = new TelemetryMenu.IntegerOption[OctoQuadBase.NUM_ENCODERS];
    TelemetryMenu.IntegerOption[] optionsAbsParamsMin = new TelemetryMenu.IntegerOption[OctoQuadBase.NUM_ENCODERS];

    TelemetryMenu.OptionElement optionProgramToFlash;
    TelemetryMenu.OptionElement optionSendToRAM;

    TelemetryMenu.StaticClickableOption optionExit;

    OctoQuad octoquad;

    boolean error;

    @Override
    public void runOpMode()
    {
	    this.octoquad = this.hardwareMap.getAll(OctoQuad.class).get(0);

        if(OctoQuadBase.OCTOQUAD_CHIP_ID != octoquad.getChipId())
        {
	        this.telemetry.addLine("Error: cannot communicate with OctoQuad. Check your wiring and configuration and try again");
	        this.telemetry.update();

	        this.error = true;
        }
        else
        {
            if(OctoQuadBase.SUPPORTED_FW_VERSION_MAJ != octoquad.getFirmwareVersion().maj)
            {
	            this.telemetry.addLine("Error: The OctoQuad is running a different major firmware version than this driver was built for. Cannot run configuration tool");
	            this.telemetry.update();

	            this.error = true;
            }
        }

        if(this.error)
        {
	        this.waitForStart();
            return;
        }

	    this.telemetry.addLine("Retrieving current configuration from OctoQuad");
	    this.telemetry.update();

	    this.optionExit = new TelemetryMenu.StaticClickableOption("Exit configuration menu")
        {
            @Override
            void onClick() // called on OpMode thread
            {
	            UtilityOctoQuadConfigMenu.this.requestOpModeStop();
            }
        };

	    this.optionI2cResetMode = new TelemetryMenu.EnumOption("I2C Reset Mode", OctoQuadBase.I2cRecoveryMode.values(), this.octoquad.getI2cRecoveryMode());
	    this.optionChannelBankConfig = new TelemetryMenu.EnumOption("Channel Bank Modes", OctoQuadBase.ChannelBankConfig.values(), this.octoquad.getChannelBankConfig());

	    this.menuHwInfo.addChild(new TelemetryMenu.StaticItem("Board Firmware: v" + this.octoquad.getFirmwareVersion()));
        //menuHwInfo.addChild(new TelemetryMenu.StaticItem("Board unique ID: FIXME"));

        for(int i = 0 ; OctoQuadBase.NUM_ENCODERS > i ; i++)
        {
	        this.optionsEncoderDirections[i] = new TelemetryMenu.BooleanOption(
                    String.format("Encoder %d direction", i), OctoQuadBase.EncoderDirection.REVERSE == octoquad.getSingleEncoderDirection(i),
                    "-",
                    "+");
        }
	    this.menuEncoderDirections.addChildren(this.optionsEncoderDirections);

        for(int i = 0 ; OctoQuadBase.NUM_ENCODERS > i ; i++)
        {
	        this.optionsVelocityIntervals[i] = new TelemetryMenu.IntegerOption(
                    String.format("Chan %d velocity intvl", i), OctoQuadBase.MIN_VELOCITY_MEASUREMENT_INTERVAL_MS, OctoQuadBase.MAX_VELOCITY_MEASUREMENT_INTERVAL_MS, this.octoquad.getSingleVelocitySampleInterval(i));
        }
	    this.menuVelocityIntervals.addChildren(this.optionsVelocityIntervals);

        for(int i = 0 ; OctoQuadBase.NUM_ENCODERS > i ; i++)
        {
            final OctoQuad.ChannelPulseWidthParams params = this.octoquad.getSingleChannelPulseWidthParams(i);

	        this.optionsAbsParamsMax[i] = new TelemetryMenu.IntegerOption(
                    String.format("Chan %d max pulse length", i), OctoQuadBase.MIN_PULSE_WIDTH_US, OctoQuadBase.MAX_PULSE_WIDTH_US,
                    params.max_length_us);

	        this.optionsAbsParamsMin[i] = new TelemetryMenu.IntegerOption(
                    String.format("Chan %d min pulse length", i), OctoQuadBase.MIN_PULSE_WIDTH_US, OctoQuadBase.MAX_PULSE_WIDTH_US,
                    params.min_length_us);
        }
	    this.menuAbsParams.addChildren(this.optionsAbsParamsMin);
	    this.menuAbsParams.addChildren(this.optionsAbsParamsMax);

	    this.optionProgramToFlash = new TelemetryMenu.OptionElement()
        {
	        final String name = "Program Settings to FLASH";
            long lastClickTime;

            @Override
            protected String getDisplayText()
            {
                if(0 == lastClickTime)
                {
                    return this.name;
                }
                else
                {
                    if(1000 > System.currentTimeMillis() - lastClickTime)
                    {
                        return this.name + " **OK**";
                    }
                    else
                    {
	                    this.lastClickTime = 0;
                        return this.name;
                    }
                }
            }

            @Override
            void onClick()
            {
	            UtilityOctoQuadConfigMenu.this.sendSettingsToRam();
	            UtilityOctoQuadConfigMenu.this.octoquad.saveParametersToFlash();
	            this.lastClickTime = System.currentTimeMillis();
            }
        };

	    this.optionSendToRAM = new TelemetryMenu.OptionElement()
        {
	        final String name = "Send Settings to RAM";
            long lastClickTime;

            @Override
            protected String getDisplayText()
            {
                if(0 == lastClickTime)
                {
                    return this.name;
                }
                else
                {
                    if(1000 > System.currentTimeMillis() - lastClickTime)
                    {
                        return this.name + " **OK**";
                    }
                    else
                    {
	                    this.lastClickTime = 0;
                        return this.name;
                    }
                }
            }

            @Override
            void onClick()
            {
	            UtilityOctoQuadConfigMenu.this.sendSettingsToRam();
	            this.lastClickTime = System.currentTimeMillis();
            }
        };

	    this.rootMenu.addChild(this.menuHwInfo);
	    this.rootMenu.addChild(this.optionI2cResetMode);
	    this.rootMenu.addChild(this.optionChannelBankConfig);
	    this.rootMenu.addChild(this.menuEncoderDirections);
	    this.rootMenu.addChild(this.menuVelocityIntervals);
	    this.rootMenu.addChild(this.menuAbsParams);
	    this.rootMenu.addChild(this.optionProgramToFlash);
	    this.rootMenu.addChild(this.optionSendToRAM);
	    this.rootMenu.addChild(this.optionExit);

        final TelemetryMenu menu = new TelemetryMenu(this.telemetry, this.rootMenu);

        while (! this.isStopRequested())
        {
            menu.loop(this.gamepad1);
	        this.telemetry.update();
	        this.sleep(20);
        }
    }

    void sendSettingsToRam()
    {
        for(int i = 0 ; OctoQuadBase.NUM_ENCODERS > i ; i++)
        {
	        this.octoquad.setSingleEncoderDirection(i, this.optionsEncoderDirections[i].getValue() ? OctoQuadBase.EncoderDirection.REVERSE : OctoQuadBase.EncoderDirection.FORWARD);
	        this.octoquad.setSingleVelocitySampleInterval(i, this.optionsVelocityIntervals[i].getValue());

            final OctoQuad.ChannelPulseWidthParams params = new OctoQuad.ChannelPulseWidthParams();
            params.max_length_us = this.optionsAbsParamsMax[i].getValue();
            params.min_length_us = this.optionsAbsParamsMin[i].getValue();

	        this.octoquad.setSingleChannelPulseWidthParams(i, params);
        }

	    this.octoquad.setI2cRecoveryMode((OctoQuadBase.I2cRecoveryMode) this.optionI2cResetMode.getValue());
	    this.octoquad.setChannelBankConfig((OctoQuadBase.ChannelBankConfig) this.optionChannelBankConfig.getValue());
    }

    /*
     * Copyright (c) 2023 OpenFTC Team
     *
     * Permission is hereby granted, free of charge, to any person obtaining a copy
     * of this software and associated documentation files (the "Software"), to deal
     * in the Software without restriction, including without limitation the rights
     * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
     * copies of the Software, and to permit persons to whom the Software is
     * furnished to do so, subject to the following conditions:
     *
     * The above copyright notice and this permission notice shall be included in all
     * copies or substantial portions of the Software.
     * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
     * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
     * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
     * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
     * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
     * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
     * SOFTWARE.
     */

    public static class TelemetryMenu
    {
        private final MenuElement root;
        private MenuElement currentLevel;

        private boolean dpadUpPrev;
        private boolean dpadDnPrev;
        private boolean dpadRightPrev;
        private boolean dpadLeftPrev;
        private boolean xPrev;
        private boolean lbPrev;

        private       int            selectedIdx;
        private final Stack<Integer> selectedIdxStack = new Stack<>();

        private final Telemetry telemetry;

        /**
         * TelemetryMenu constructor
         * @param telemetry pass in 'telemetry' from your OpMode
         * @param root the root menu element
         */
        public TelemetryMenu(final Telemetry telemetry, final MenuElement root)
        {
            this.root = root;
            currentLevel = root;
            this.telemetry = telemetry;

            telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML);
            telemetry.setMsTransmissionInterval(50);
        }

        /**
         * Call this from inside your loop to put the current menu state into
         * telemetry, and process gamepad inputs for navigating the menu
         * @param gamepad the gamepad you want to use to navigate the menu
         */
        public void loop(final Gamepad gamepad)
        {
            // Capture current state of the gamepad buttons we care about;
            // We can only look once or we risk a race condition
            final boolean dpadUp = gamepad.dpad_up;
            final boolean dpadDn = gamepad.dpad_down;
            final boolean dpadRight = gamepad.dpad_right;
            final boolean dpadLeft  = gamepad.dpad_left;
            final boolean x         = gamepad.x;
            final boolean lb       = gamepad.left_bumper;

            // Figure out who our children our at this level
            // and figure out which item is currently highlighted
            // with the selection pointer
            final ArrayList<Element> children         = this.currentLevel.children();
            final Element            currentSelection = children.get(this.selectedIdx);

            // Left and right are inputs to the selected item (if it's an Option)
            if (currentSelection instanceof OptionElement)
            {
                if (dpadRight && ! this.dpadRightPrev) // rising edge
                {
                    ((OptionElement) currentSelection).onRightInput();
                }
                else if (dpadLeft && ! this.dpadLeftPrev) // rising edge
                {
                    ((OptionElement) currentSelection).onLeftInput();
                }
            }

            // Up and down navigate the current selection pointer
            if (dpadUp && ! this.dpadUpPrev) // rising edge
            {
	            this.selectedIdx--; // Move selection pointer up
            }
            else if (dpadDn && ! this.dpadDnPrev) // rising edge
            {
	            this.selectedIdx++; // Move selection pointer down
            }

            // Make selected index sane (don't let it go out of bounds) :eyes:
            if (this.selectedIdx >= children.size())
            {
	            this.selectedIdx = children.size()-1;
            }
            else if (0 > selectedIdx)
            {
	            this.selectedIdx = 0;
            }

            // Select: either enter submenu or input to option
            else if (x && ! this.xPrev) // rising edge
            {
                // Select up element
                if (currentSelection instanceof SpecialUpElement)
                {
                    // We can only go up if we're not at the root level
                    if (this.currentLevel != this.root)
                    {
                        // Restore selection pointer to where it was before
	                    this.selectedIdx = this.selectedIdxStack.pop();

                        // Change to the parent level
	                    this.currentLevel = this.currentLevel.parent();
                    }
                }
                // Input to option
                else if (currentSelection instanceof OptionElement)
                {
                    ((OptionElement) currentSelection).onClick();
                }
                // Enter submenu
                else if (currentSelection instanceof MenuElement)
                {
                    // Save our current selection pointer so we can restore it
                    // later if the user navigates back up a level
	                this.selectedIdxStack.push(this.selectedIdx);

                    // We have no idea what's in the submenu :monkey: so best to
                    // just set the selection pointer to the first element
	                this.selectedIdx = 0;

                    // Now the current level becomes the submenu that the selection
                    // pointer was on
	                this.currentLevel = (MenuElement) currentSelection;
                }
            }

            // Go up a level
            else if (lb && ! this.lbPrev)
            {
                // We can only go up if we're not at the root level
                if (this.currentLevel != this.root)
                {
                    // Restore selection pointer to where it was before
	                this.selectedIdx = this.selectedIdxStack.pop();

                    // Change to the parent level
	                this.currentLevel = this.currentLevel.parent();
                }
            }

            // Save the current button states so that we can look for
            // the rising edge the next time around the loop :)
	        this.dpadUpPrev = dpadUp;
	        this.dpadDnPrev = dpadDn;
	        this.dpadRightPrev = dpadRight;
	        this.dpadLeftPrev = dpadLeft;
	        this.xPrev = x;
	        this.lbPrev = lb;

            // Start building the text display.
            // First, we add the static directions for gamepad operation
            final StringBuilder builder = new StringBuilder();
            builder.append("<font color='#119af5' face=monospace>");
            builder.append("Navigate items.....dpad up/down\n")
                    .append("Select.............X or Square\n")
                    .append("Edit option........dpad left/right\n")
                    .append("Up one level.......left bumper\n");
            builder.append("</font>");
            builder.append("\n");

            // Now actually add the menu options. We start by adding the name of the current menu level.
            builder.append("<font face=monospace>");
            builder.append("Current Menu: ").append(this.currentLevel.name).append("\n");

            // Now we loop through all the child elements of this level and add them
            for (int i = 0; i < children.size(); i++)
            {
                // If the selection pointer is at this index, put a green dot in the box :)
                if (this.selectedIdx == i)
                {
                    builder.append("[<font color=green face=monospace>•</font>] ");
                }
                // Otherwise, just put an empty box
                else
                {
                    builder.append("[ ] ");
                }

                // Figure out who the selection pointer is pointing at :eyes:
                final Element e = children.get(i);

                // If it's pointing at a submenu, indicate that it's a submenu to the user
                // by prefixing "> " to the name.
                if (e instanceof MenuElement)
                {
                    builder.append("> ");
                }

                // Finally, add the element's name
                builder.append(e.getDisplayText());

                // We musn't forget the newline
                builder.append("\n");
            }

            // Don't forget to close the font tag either
            builder.append("</font>");

            // Build the string!!!! :nerd:
            final String menu = builder.toString();

            // Add it to telemetry
	        this.telemetry.addLine(menu);
        }

        public static class MenuElement extends Element
        {
            private final String             name;
            private final ArrayList<Element> children = new ArrayList<>();

            /**
             * Create a new MenuElement; may either be the root menu, or a submenu (set isRoot accordingly)
             * @param name the name for this menu
             * @param isRoot whether this is a root menu, or a submenu
             */
            public MenuElement(final String name, final boolean isRoot)
            {
                this.name = name;

                // If it's not the root menu, we add the up one level option as the first element
                if (!isRoot)
                {
	                this.children.add(new SpecialUpElement());
                }
            }

            /**
             * Add a child element to this menu (may either be an Option or another menu)
             * @param child the child element to add
             */
            public void addChild(final Element child)
            {
                child.setParent(this);
	            this.children.add(child);
            }

            /**
             * Add multiple child elements to this menu (may either be option, or another menu)
             * @param children the children to add
             */
            public void addChildren(final Element[] children)
            {
                for (final Element e : children)
                {
                    e.setParent(this);
                    this.children.add(e);
                }
            }

            @Override
            protected String getDisplayText()
            {
                return this.name;
            }

            private ArrayList<Element> children()
            {
                return this.children;
            }
        }

        public static abstract class OptionElement extends Element
        {
            /**
             * Override this to get notified when the element is clicked
             */
            void onClick() {}

            /**
             * Override this to get notified when the element gets a "left edit" input
             */
            protected void onLeftInput() {}

            /**
             * Override this to get notified when the element gets a "right edit" input
             */
            protected void onRightInput() {}
        }

        public static class EnumOption extends OptionElement
        {
            protected int idx;
            protected Enum[] e;
            protected String name;

            public EnumOption(final String name, final Enum[] e)
            {
                this.e = e;
                this.name = name;
            }

            public EnumOption(final String name, final Enum[] e, final Enum def)
            {
                this(name, e);
	            this.idx = def.ordinal();
            }

            @Override
            public void onLeftInput()
            {
	            this.idx++;

                if(this.idx > this.e.length - 1)
                {
	                this.idx = 0;
                }
            }

            @Override
            public void onRightInput()
            {
	            this.idx--;

                if(0 > idx)
                {
	                this.idx = this.e.length - 1;
                }
            }

            @Override
            public void onClick()
            {
                //onRightInput();
            }

            @Override
            protected String getDisplayText()
            {
                return String.format("%s: <font color='#e37c07' face=monospace>%s</font>", this.name, this.e[this.idx].name());
            }

            public Enum getValue()
            {
                return this.e[this.idx];
            }
        }

        public static class IntegerOption extends OptionElement
        {
            protected int i;
            protected int min;
            protected int max;
            protected String name;

            public IntegerOption(final String name, final int min, final int max, final int def)
            {
                this.name = name;
                this.min = min;
                this.max = max;
                i = def;
            }

            @Override
            public void onLeftInput()
            {
	            this.i--;

                if(this.i < this.min)
                {
	                this.i = this.max;
                }
            }

            @Override
            public void onRightInput()
            {
	            this.i++;

                if(this.i > this.max)
                {
	                this.i = this.min;
                }
            }

            @Override
            public void onClick()
            {
                //onRightInput();
            }

            @Override
            protected String getDisplayText()
            {
                return String.format("%s: <font color='#e37c07' face=monospace>%d</font>", this.name, this.i);
            }

            public int getValue()
            {
                return this.i;
            }
        }

        static class BooleanOption extends OptionElement
        {
            private final String  name;
            private       boolean val = true;

            private String customTrue;
            private String customFalse;

            BooleanOption(final String name, final boolean def)
            {
                this.name = name;
                val = def;
            }

            BooleanOption(final String name, final boolean def, final String customTrue, final String customFalse)
            {
                this(name, def);
                this.customTrue = customTrue;
                this.customFalse = customFalse;
            }

            @Override
            public void onLeftInput()
            {
	            this.val = ! this.val;
            }

            @Override
            public void onRightInput()
            {
	            this.val = ! this.val;
            }

            @Override
            public void onClick()
            {
	            this.val = ! this.val;
            }

            @Override
            protected String getDisplayText()
            {
                final String valStr;

                if(null != customTrue && null != customFalse)
                {
                    valStr = this.val ? this.customTrue : this.customFalse;
                }
                else
                {
                    valStr = this.val ? "true" : "false";
                }

                return String.format("%s: <font color='#e37c07' face=monospace>%s</font>", this.name, valStr);
            }

            public boolean getValue()
            {
                return this.val;
            }
        }

        /**
         *
         */
        public static class StaticItem extends OptionElement
        {
            private final String name;

            public StaticItem(final String name)
            {
                this.name = name;
            }

            @Override
            protected String getDisplayText()
            {
                return this.name;
            }
        }

        public static abstract class StaticClickableOption extends OptionElement
        {
            private final String name;

            public StaticClickableOption(final String name)
            {
                this.name = name;
            }

            abstract void onClick();

            @Override
            protected String getDisplayText()
            {
                return this.name;
            }
        }

        private static abstract class Element
        {
            private MenuElement parent;

            protected void setParent(final MenuElement parent)
            {
                this.parent = parent;
            }

            protected MenuElement parent()
            {
                return this.parent;
            }

            protected abstract String getDisplayText();
        }

        private static class SpecialUpElement extends Element
        {
            @Override
            protected String getDisplayText()
            {
                return "<font color='#119af5' face=monospace>.. ↰ Up One Level</font>";
            }
        }
    }
}

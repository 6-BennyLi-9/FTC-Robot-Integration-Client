package org.roadrunner.core;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Actions;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.Arclength;
import com.acmerobotics.roadrunner.DualNum;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.MotorFeedforward;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.PoseVelocity2dDual;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.ProfileParams;
import com.acmerobotics.roadrunner.RamseteController;
import com.acmerobotics.roadrunner.TankKinematics;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.TimeTrajectory;
import com.acmerobotics.roadrunner.TimeTurn;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TrajectoryBuilderParams;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Twist2dDual;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.Vector2dDual;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.DownsampledWriter;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.FlightRecorder;
import com.acmerobotics.roadrunner.ftc.LazyImu;
import com.acmerobotics.roadrunner.ftc.LynxFirmware;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.roadrunner.core.messages.DriveCommandMessage;
import org.roadrunner.core.messages.PoseMessage;
import org.roadrunner.core.messages.TankCommandMessage;
import org.roadrunner.core.messages.TankLocalizerInputsMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Config
public final class TankDrive {
    public static class Params {
        // IMU orientation
        // TODO: fill in these values based on
        //   see https://ftc-docs.firstinspires.org/en/latest/programming_resources/imu/imu.html?highlight=imu#physical-hub-mounting
        public RevHubOrientationOnRobot.LogoFacingDirection logoFacingDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.UP;
        public RevHubOrientationOnRobot.UsbFacingDirection usbFacingDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

        // drive model parameters
        public double inPerTick;
        public double trackWidthTicks;

        // feedforward parameters (in tick units)
        public double kS;
        public double kV;
        public double kA;

        // path profile parameters (in inches)
        public double maxWheelVel = 50;
        public double minProfileAccel = -30;
        public double maxProfileAccel = 50;

        // turn profile parameters (in radians)
        public double maxAngVel = Math.PI; // shared with path
        public double maxAngAccel = Math.PI;

        // path controller gains
        public double ramseteZeta = 0.7; // in the range (0, 1)
        public double ramseteBBar = 2.0; // positive

        // turn controller gains
        public double turnGain;
        public double turnVelGain;
    }

    public static Params PARAMS = new Params();

    public final TankKinematics kinematics = new TankKinematics(TankDrive.PARAMS.inPerTick * TankDrive.PARAMS.trackWidthTicks);

    public final TurnConstraints defaultTurnConstraints = new TurnConstraints(TankDrive.PARAMS.maxAngVel, - TankDrive.PARAMS.maxAngVel, TankDrive.PARAMS.maxAngAccel);
    public final VelConstraint defaultVelConstraint =
            new MinVelConstraint(Arrays.asList(this.kinematics.new WheelVelConstraint(TankDrive.PARAMS.maxWheelVel),
                    new AngularVelConstraint(TankDrive.PARAMS.maxAngVel)
            ));
    public final AccelConstraint defaultAccelConstraint =
            new ProfileAccelConstraint(TankDrive.PARAMS.minProfileAccel, TankDrive.PARAMS.maxProfileAccel);

    public final List<DcMotorEx> leftMotors, rightMotors;

    public final LazyImu lazyImu;

    public final VoltageSensor voltageSensor;

    public final Localizer localizer;
    public Pose2d pose;

    private final LinkedList<Pose2d> poseHistory = new LinkedList<>();

    private final DownsampledWriter estimatedPoseWriter = new DownsampledWriter("ESTIMATED_POSE", 50_000_000);
    private final DownsampledWriter targetPoseWriter = new DownsampledWriter("TARGET_POSE", 50_000_000);
    private final DownsampledWriter driveCommandWriter = new DownsampledWriter("DRIVE_COMMAND", 50_000_000);

    private final DownsampledWriter tankCommandWriter = new DownsampledWriter("TANK_COMMAND", 50_000_000);

    public class DriveLocalizer implements Localizer {
        public final List<Encoder> leftEncs, rightEncs;

        private double lastLeftPos, lastRightPos;
        private boolean initialized;

        public DriveLocalizer() {
            {
                final List<Encoder> leftEncs = new ArrayList<>();
                for (final DcMotorEx m : TankDrive.this.leftMotors) {
                    final Encoder e = new OverflowEncoder(new RawEncoder(m));
                    leftEncs.add(e);
                }
                this.leftEncs = Collections.unmodifiableList(leftEncs);
            }

            {
                final List<Encoder> rightEncs = new ArrayList<>();
                for (final DcMotorEx m : TankDrive.this.rightMotors) {
                    final Encoder e = new OverflowEncoder(new RawEncoder(m));
                    rightEncs.add(e);
                }
                this.rightEncs = Collections.unmodifiableList(rightEncs);
            }

            // TODO: reverse encoder directions if needed
            //   leftEncs.get(0).setDirection(DcMotorSimple.Direction.REVERSE);
        }

        @Override
        public Twist2dDual<Time> update() {
            final List<PositionVelocityPair> leftReadings  = new ArrayList<>();
	        final List<PositionVelocityPair> rightReadings = new ArrayList<>();
	        double                           meanLeftPos   = 0.0, meanLeftVel = 0.0;
            for (final Encoder e : this.leftEncs) {
                final PositionVelocityPair p = e.getPositionAndVelocity();
                meanLeftPos += p.position;
                meanLeftVel += p.velocity;
                leftReadings.add(p);
            }
            meanLeftPos /= this.leftEncs.size();
            meanLeftVel /= this.leftEncs.size();

            double meanRightPos = 0.0, meanRightVel = 0.0;
            for (final Encoder e : this.rightEncs) {
                final PositionVelocityPair p = e.getPositionAndVelocity();
                meanRightPos += p.position;
                meanRightVel += p.velocity;
                rightReadings.add(p);
            }
            meanRightPos /= this.rightEncs.size();
            meanRightVel /= this.rightEncs.size();

            FlightRecorder.write("TANK_LOCALIZER_INPUTS",
                     new TankLocalizerInputsMessage(leftReadings, rightReadings));

            if (! this.initialized) {
	            this.initialized = true;

	            this.lastLeftPos = meanLeftPos;
	            this.lastRightPos = meanRightPos;

                return new Twist2dDual<>(
                        Vector2dDual.constant(new Vector2d(0.0, 0.0), 2),
                        DualNum.constant(0.0, 2)
                );
            }

            final TankKinematics.WheelIncrements<Time> twist = new TankKinematics.WheelIncrements<>(
                    new DualNum<Time>(new double[] {
		                    meanLeftPos - this.lastLeftPos,
                            meanLeftVel
                    }).times(TankDrive.PARAMS.inPerTick),
                    new DualNum<Time>(new double[] {
		                    meanRightPos - this.lastRightPos,
                            meanRightVel,
                    }).times(TankDrive.PARAMS.inPerTick)
            );

	        this.lastLeftPos = meanLeftPos;
	        this.lastRightPos = meanRightPos;

            return TankDrive.this.kinematics.forward(twist);
        }
    }

    public TankDrive(final HardwareMap hardwareMap, final Pose2d pose) {
        this.pose = pose;

        LynxFirmware.throwIfModulesAreOutdated(hardwareMap);

        for (final LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        // TODO: make sure your config has motors with these names (or change them)
        //   add additional motors on each side if you have them
        //   see https://ftc-docs.firstinspires.org/en/latest/hardware_and_software_configuration/configuring/index.html
	    this.leftMotors = Collections.singletonList(hardwareMap.get(DcMotorEx.class, "left"));
	    this.rightMotors = Collections.singletonList(hardwareMap.get(DcMotorEx.class, "right"));

        for (final DcMotorEx m : this.leftMotors) {
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        for (final DcMotorEx m : this.rightMotors) {
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        // TODO: reverse motor directions if needed
        //   leftMotors.get(0).setDirection(DcMotorSimple.Direction.REVERSE);

        // TODO: make sure your config has an IMU with this name (can be BNO or BHI)
        //   see https://ftc-docs.firstinspires.org/en/latest/hardware_and_software_configuration/configuring/index.html
	    this.lazyImu = new LazyImu(hardwareMap, "imu", new RevHubOrientationOnRobot(TankDrive.PARAMS.logoFacingDirection, TankDrive.PARAMS.usbFacingDirection));

	    this.voltageSensor = hardwareMap.voltageSensor.iterator().next();

	    this.localizer = new TankDrive.DriveLocalizer();

        FlightRecorder.write("TANK_PARAMS", TankDrive.PARAMS);
    }

    public void setDrivePowers(final PoseVelocity2d powers) {
        final TankKinematics.WheelVelocities<Time> wheelVels = new TankKinematics(2).inverse(
                PoseVelocity2dDual.constant(powers, 1));

        double maxPowerMag = 1;
        for (final DualNum<Time> power : wheelVels.all()) {
            maxPowerMag = Math.max(maxPowerMag, power.value());
        }

        for (final DcMotorEx m : this.leftMotors) {
            m.setPower(wheelVels.left.get(0) / maxPowerMag);
        }
        for (final DcMotorEx m : this.rightMotors) {
            m.setPower(wheelVels.right.get(0) / maxPowerMag);
        }
    }

    public final class FollowTrajectoryAction implements Action {
        public final TimeTrajectory timeTrajectory;
        private double beginTs = -1;

        private final double[] xPoints, yPoints;

        public FollowTrajectoryAction(final TimeTrajectory t) {
	        this.timeTrajectory = t;

            final List<Double> disps = com.acmerobotics.roadrunner.Math.range(
                    0, t.path.length(),
                    Math.max(2, (int) Math.ceil(t.path.length() / 2)));
	        this.xPoints = new double[disps.size()];
	        this.yPoints = new double[disps.size()];
            for (int i = 0; i < disps.size(); i++) {
                final Pose2d p = t.path.get(disps.get(i), 1).value();
	            this.xPoints[i] = p.position.x;
	            this.yPoints[i] = p.position.y;
            }
        }

        @Override
        public boolean run(@NonNull final TelemetryPacket p) {
            final double t;
            if (0 > beginTs) {
	            this.beginTs = Actions.now();
                t = 0;
            } else {
                t = Actions.now() - this.beginTs;
            }

            if (t >= this.timeTrajectory.duration) {
                for (final DcMotorEx m : TankDrive.this.leftMotors) {
                    m.setPower(0);
                }
                for (final DcMotorEx m : TankDrive.this.rightMotors) {
                    m.setPower(0);
                }

                return false;
            }

            final DualNum<Time> x = this.timeTrajectory.profile.get(t);

            final Pose2dDual<Arclength> txWorldTarget = this.timeTrajectory.path.get(x.value(), 3);
	        TankDrive.this.targetPoseWriter.write(new PoseMessage(txWorldTarget.value()));

	        TankDrive.this.updatePoseEstimate();

            final PoseVelocity2dDual<Time> command = new RamseteController(TankDrive.this.kinematics.trackWidth, TankDrive.PARAMS.ramseteZeta, TankDrive.PARAMS.ramseteBBar)
                    .compute(x, txWorldTarget, TankDrive.this.pose);
	        TankDrive.this.driveCommandWriter.write(new DriveCommandMessage(command));

            final TankKinematics.WheelVelocities<Time> wheelVels = TankDrive.this.kinematics.inverse(command);
            final double                               voltage   = TankDrive.this.voltageSensor.getVoltage();
            MotorFeedforward                     feedforward = new MotorFeedforward(TankDrive.PARAMS.kS, TankDrive.PARAMS.kV / TankDrive.PARAMS.inPerTick, TankDrive.PARAMS.kA / TankDrive.PARAMS.inPerTick);
            final double leftPower  = feedforward.compute(wheelVels.left) / voltage;
            final double rightPower = feedforward.compute(wheelVels.right) / voltage;
	        TankDrive.this.tankCommandWriter.write(new TankCommandMessage(voltage, leftPower, rightPower));

            for (final DcMotorEx m : TankDrive.this.leftMotors) {
                m.setPower(leftPower);
            }
            for (final DcMotorEx m : TankDrive.this.rightMotors) {
                m.setPower(rightPower);
            }

            p.put("x", TankDrive.this.pose.position.x);
            p.put("y", TankDrive.this.pose.position.y);
            p.put("heading (deg)", Math.toDegrees(TankDrive.this.pose.heading.toDouble()));

            final Pose2d error = txWorldTarget.value().minusExp(TankDrive.this.pose);
            p.put("xError", error.position.x);
            p.put("yError", error.position.y);
            p.put("headingError (deg)", Math.toDegrees(error.heading.toDouble()));

            // only draw when active; only one drive action should be active at a time
            final Canvas c = p.fieldOverlay();
	        TankDrive.this.drawPoseHistory(c);

            c.setStroke("#4CAF50");
            Drawing.drawRobot(c, txWorldTarget.value());

            c.setStroke("#3F51B5");
            Drawing.drawRobot(c, TankDrive.this.pose);

            c.setStroke("#4CAF50FF");
            c.setStrokeWidth(1);
            c.strokePolyline(this.xPoints, this.yPoints);

            return true;
        }

        @Override
        public void preview(final Canvas c) {
            c.setStroke("#4CAF507A");
            c.setStrokeWidth(1);
            c.strokePolyline(this.xPoints, this.yPoints);
        }
    }

    public final class TurnAction implements Action {
        private final TimeTurn turn;

        private double beginTs = -1;

        public TurnAction(final TimeTurn turn) {
            this.turn = turn;
        }

        @Override
        public boolean run(@NonNull final TelemetryPacket p) {
            final double t;
            if (0 > beginTs) {
	            this.beginTs = Actions.now();
                t = 0;
            } else {
                t = Actions.now() - this.beginTs;
            }

            if (t >= this.turn.duration) {
                for (final DcMotorEx m : TankDrive.this.leftMotors) {
                    m.setPower(0);
                }
                for (final DcMotorEx m : TankDrive.this.rightMotors) {
                    m.setPower(0);
                }

                return false;
            }

            final Pose2dDual<Time> txWorldTarget = this.turn.get(t);
	        TankDrive.this.targetPoseWriter.write(new PoseMessage(txWorldTarget.value()));

            final PoseVelocity2d robotVelRobot = TankDrive.this.updatePoseEstimate();

            final PoseVelocity2dDual<Time> command = new PoseVelocity2dDual<>(
                    Vector2dDual.constant(new Vector2d(0, 0), 3),
                    txWorldTarget.heading.velocity().plus(TankDrive.PARAMS.turnGain * TankDrive.this.pose.heading.minus(txWorldTarget.heading.value()) + TankDrive.PARAMS.turnVelGain * (robotVelRobot.angVel - txWorldTarget.heading.velocity().value())
                    )
            );
	        TankDrive.this.driveCommandWriter.write(new DriveCommandMessage(command));

            final TankKinematics.WheelVelocities<Time> wheelVels = TankDrive.this.kinematics.inverse(command);
            final double                               voltage   = TankDrive.this.voltageSensor.getVoltage();
            MotorFeedforward                     feedforward = new MotorFeedforward(TankDrive.PARAMS.kS, TankDrive.PARAMS.kV / TankDrive.PARAMS.inPerTick, TankDrive.PARAMS.kA / TankDrive.PARAMS.inPerTick);
            final double leftPower  = feedforward.compute(wheelVels.left) / voltage;
            final double rightPower = feedforward.compute(wheelVels.right) / voltage;
	        TankDrive.this.tankCommandWriter.write(new TankCommandMessage(voltage, leftPower, rightPower));

            for (final DcMotorEx m : TankDrive.this.leftMotors) {
                m.setPower(leftPower);
            }
            for (final DcMotorEx m : TankDrive.this.rightMotors) {
                m.setPower(rightPower);
            }

            final Canvas c = p.fieldOverlay();
	        TankDrive.this.drawPoseHistory(c);

            c.setStroke("#4CAF50");
            Drawing.drawRobot(c, txWorldTarget.value());

            c.setStroke("#3F51B5");
            Drawing.drawRobot(c, TankDrive.this.pose);

            c.setStroke("#7C4DFFFF");
            c.fillCircle(this.turn.beginPose.position.x, this.turn.beginPose.position.y, 2);

            return true;
        }

        @Override
        public void preview(final Canvas c) {
            c.setStroke("#7C4DFF7A");
            c.fillCircle(this.turn.beginPose.position.x, this.turn.beginPose.position.y, 2);
        }
    }

    public PoseVelocity2d updatePoseEstimate() {
        final Twist2dDual<Time> twist = this.localizer.update();
	    this.pose = this.pose.plus(twist.value());

	    this.poseHistory.add(this.pose);
        while (100 < poseHistory.size()) {
	        this.poseHistory.removeFirst();
        }

	    this.estimatedPoseWriter.write(new PoseMessage(this.pose));

        return twist.velocity().value();
    }

    private void drawPoseHistory(final Canvas c) {
        final double[] xPoints = new double[this.poseHistory.size()];
        final double[] yPoints = new double[this.poseHistory.size()];

        int i = 0;
        for (final Pose2d t : this.poseHistory) {
            xPoints[i] = t.position.x;
            yPoints[i] = t.position.y;

            i++;
        }

        c.setStrokeWidth(1);
        c.setStroke("#3F51B5");
        c.strokePolyline(xPoints, yPoints);
    }

    public TrajectoryActionBuilder actionBuilder(final Pose2d beginPose) {
        return new TrajectoryActionBuilder(
                TurnAction::new,
                FollowTrajectoryAction::new,
                new TrajectoryBuilderParams(1.0e-6,
                        new ProfileParams(
                                0.25, 0.1, 1.0e-2
                        )
                ),
                beginPose, 0.0, this.defaultTurnConstraints, this.defaultVelConstraint, this.defaultAccelConstraint
        );
    }
}

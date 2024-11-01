package org.roadrunner.core;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.*;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.DualNum;
import com.acmerobotics.roadrunner.HolonomicController;
import com.acmerobotics.roadrunner.MecanumKinematics;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.MotorFeedforward;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Pose2dDual;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.TimeTrajectory;
import com.acmerobotics.roadrunner.TimeTurn;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Twist2dDual;
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
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.roadrunner.core.messages.DriveCommandMessage;
import org.roadrunner.core.messages.MecanumCommandMessage;
import org.roadrunner.core.messages.MecanumLocalizerInputsMessage;
import org.roadrunner.core.messages.PoseMessage;

import java.lang.Math;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Config
public final class MecanumDrive {
    public static class Params {
        // IMU orientation
        // TODO: fill in these values based on
        //   see https://ftc-docs.firstinspires.org/en/latest/programming_resources/imu/imu.html?highlight=imu#physical-hub-mounting
        public RevHubOrientationOnRobot.LogoFacingDirection logoFacingDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.UP;
        public RevHubOrientationOnRobot.UsbFacingDirection usbFacingDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

        // drive model parameters
        public double inPerTick = 1;
        public double lateralInPerTick = this.inPerTick;
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
        public double axialGain;
        public double lateralGain;
        public double headingGain; // shared with turn

        public double axialVelGain;
        public double lateralVelGain;
        public double headingVelGain; // shared with turn
    }

    public static Params PARAMS = new Params();

    public final MecanumKinematics kinematics = new MecanumKinematics(MecanumDrive.PARAMS.inPerTick * MecanumDrive.PARAMS.trackWidthTicks, MecanumDrive.PARAMS.inPerTick / MecanumDrive.PARAMS.lateralInPerTick);

    public final TurnConstraints defaultTurnConstraints = new TurnConstraints(MecanumDrive.PARAMS.maxAngVel, - MecanumDrive.PARAMS.maxAngAccel, MecanumDrive.PARAMS.maxAngAccel);
    public final VelConstraint defaultVelConstraint =
            new MinVelConstraint(Arrays.asList(this.kinematics.new WheelVelConstraint(MecanumDrive.PARAMS.maxWheelVel),
                    new AngularVelConstraint(MecanumDrive.PARAMS.maxAngVel)
            ));
    public final AccelConstraint defaultAccelConstraint =
            new ProfileAccelConstraint(MecanumDrive.PARAMS.minProfileAccel, MecanumDrive.PARAMS.maxProfileAccel);

    public final DcMotorEx leftFront, leftBack, rightBack, rightFront;

    public final VoltageSensor voltageSensor;

    public final LazyImu lazyImu;

    public final Localizer localizer;
    public Pose2d pose;

    private final LinkedList<Pose2d> poseHistory = new LinkedList<>();

    private final DownsampledWriter estimatedPoseWriter = new DownsampledWriter("ESTIMATED_POSE", 50_000_000);
    private final DownsampledWriter targetPoseWriter = new DownsampledWriter("TARGET_POSE", 50_000_000);
    private final DownsampledWriter driveCommandWriter = new DownsampledWriter("DRIVE_COMMAND", 50_000_000);
    private final DownsampledWriter mecanumCommandWriter = new DownsampledWriter("MECANUM_COMMAND", 50_000_000);

    public class DriveLocalizer implements Localizer {
        public final Encoder leftFront, leftBack, rightBack, rightFront;
        public final IMU imu;

        private int lastLeftFrontPos, lastLeftBackPos, lastRightBackPos, lastRightFrontPos;
        private Rotation2d lastHeading;
        private boolean initialized;

        public DriveLocalizer() {
	        this.leftFront = new OverflowEncoder(new RawEncoder(MecanumDrive.this.leftFront));
	        this.leftBack = new OverflowEncoder(new RawEncoder(MecanumDrive.this.leftBack));
	        this.rightBack = new OverflowEncoder(new RawEncoder(MecanumDrive.this.rightBack));
	        this.rightFront = new OverflowEncoder(new RawEncoder(MecanumDrive.this.rightFront));

	        this.imu = MecanumDrive.this.lazyImu.get();

            // TODO: reverse encoders if needed
            //   leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        @Override
        public Twist2dDual<Time> update() {
            final PositionVelocityPair leftFrontPosVel = this.leftFront.getPositionAndVelocity();
            final PositionVelocityPair leftBackPosVel  = this.leftBack.getPositionAndVelocity();
            final PositionVelocityPair rightBackPosVel = this.rightBack.getPositionAndVelocity();
            final PositionVelocityPair rightFrontPosVel = this.rightFront.getPositionAndVelocity();

            final YawPitchRollAngles angles = this.imu.getRobotYawPitchRollAngles();

            FlightRecorder.write("MECANUM_LOCALIZER_INPUTS", new MecanumLocalizerInputsMessage(
                    leftFrontPosVel, leftBackPosVel, rightBackPosVel, rightFrontPosVel, angles));

            final Rotation2d heading = Rotation2d.exp(angles.getYaw(AngleUnit.RADIANS));

            if (! this.initialized) {
	            this.initialized = true;

	            this.lastLeftFrontPos = leftFrontPosVel.position;
	            this.lastLeftBackPos = leftBackPosVel.position;
	            this.lastRightBackPos = rightBackPosVel.position;
	            this.lastRightFrontPos = rightFrontPosVel.position;

	            this.lastHeading = heading;

                return new Twist2dDual<>(
                        Vector2dDual.constant(new Vector2d(0.0, 0.0), 2),
                        DualNum.constant(0.0, 2)
                );
            }

            final double headingDelta = heading.minus(this.lastHeading);
            final Twist2dDual<Time> twist = MecanumDrive.this.kinematics.forward(new MecanumKinematics.WheelIncrements<>(
                    new DualNum<Time>(new double[]{
                            (leftFrontPosVel.position - this.lastLeftFrontPos),
                            leftFrontPosVel.velocity,
                    }).times(MecanumDrive.PARAMS.inPerTick),
                    new DualNum<Time>(new double[]{
                            (leftBackPosVel.position - this.lastLeftBackPos),
                            leftBackPosVel.velocity,
                    }).times(MecanumDrive.PARAMS.inPerTick),
                    new DualNum<Time>(new double[]{
                            (rightBackPosVel.position - this.lastRightBackPos),
                            rightBackPosVel.velocity,
                    }).times(MecanumDrive.PARAMS.inPerTick),
                    new DualNum<Time>(new double[]{
                            (rightFrontPosVel.position - this.lastRightFrontPos),
                            rightFrontPosVel.velocity,
                    }).times(MecanumDrive.PARAMS.inPerTick)
            ));

	        this.lastLeftFrontPos = leftFrontPosVel.position;
	        this.lastLeftBackPos = leftBackPosVel.position;
	        this.lastRightBackPos = rightBackPosVel.position;
	        this.lastRightFrontPos = rightFrontPosVel.position;

	        this.lastHeading = heading;

            return new Twist2dDual<>(
                    twist.line,
                    DualNum.cons(headingDelta, twist.angle.drop(1))
            );
        }
    }

    public MecanumDrive(final HardwareMap hardwareMap, final Pose2d pose) {
        this.pose = pose;

        LynxFirmware.throwIfModulesAreOutdated(hardwareMap);

        for (final LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        // TODO: make sure your config has motors with these names (or change them)
        //   see https://ftc-docs.firstinspires.org/en/latest/hardware_and_software_configuration/configuring/index.html
	    this.leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
	    this.leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
	    this.rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");
	    this.rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");

	    this.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
	    this.leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
	    this.rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
	    this.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // TODO: reverse motor directions if needed
        //   leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

        // TODO: make sure your config has an IMU with this name (can be BNO or BHI)
        //   see https://ftc-docs.firstinspires.org/en/latest/hardware_and_software_configuration/configuring/index.html
	    this.lazyImu = new LazyImu(hardwareMap, "imu", new RevHubOrientationOnRobot(MecanumDrive.PARAMS.logoFacingDirection, MecanumDrive.PARAMS.usbFacingDirection));

	    this.voltageSensor = hardwareMap.voltageSensor.iterator().next();

	    this.localizer = new DriveLocalizer();

        FlightRecorder.write("MECANUM_PARAMS", MecanumDrive.PARAMS);
    }

    public void setDrivePowers(final PoseVelocity2d powers) {
        final MecanumKinematics.WheelVelocities<Time> wheelVels = new MecanumKinematics(1).inverse(
                PoseVelocity2dDual.constant(powers, 1));

        double maxPowerMag = 1;
        for (final DualNum<Time> power : wheelVels.all()) {
            maxPowerMag = Math.max(maxPowerMag, power.value());
        }

	    this.leftFront.setPower(wheelVels.leftFront.get(0) / maxPowerMag);
	    this.leftBack.setPower(wheelVels.leftBack.get(0) / maxPowerMag);
	    this.rightBack.setPower(wheelVels.rightBack.get(0) / maxPowerMag);
	    this.rightFront.setPower(wheelVels.rightFront.get(0) / maxPowerMag);
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
	            MecanumDrive.this.leftFront.setPower(0);
	            MecanumDrive.this.leftBack.setPower(0);
	            MecanumDrive.this.rightBack.setPower(0);
	            MecanumDrive.this.rightFront.setPower(0);

                return false;
            }

            final Pose2dDual<Time> txWorldTarget = this.timeTrajectory.get(t);
	        MecanumDrive.this.targetPoseWriter.write(new PoseMessage(txWorldTarget.value()));

            final PoseVelocity2d robotVelRobot = MecanumDrive.this.updatePoseEstimate();

            final PoseVelocity2dDual<Time> command = new HolonomicController(MecanumDrive.PARAMS.axialGain, MecanumDrive.PARAMS.lateralGain, MecanumDrive.PARAMS.headingGain, MecanumDrive.PARAMS.axialVelGain, MecanumDrive.PARAMS.lateralVelGain, MecanumDrive.PARAMS.headingVelGain
            )
                    .compute(txWorldTarget, MecanumDrive.this.pose, robotVelRobot);
	        MecanumDrive.this.driveCommandWriter.write(new DriveCommandMessage(command));

            final MecanumKinematics.WheelVelocities<Time> wheelVels = MecanumDrive.this.kinematics.inverse(command);
            final double                                  voltage   = MecanumDrive.this.voltageSensor.getVoltage();

            MotorFeedforward feedforward = new MotorFeedforward(MecanumDrive.PARAMS.kS, MecanumDrive.PARAMS.kV / MecanumDrive.PARAMS.inPerTick, MecanumDrive.PARAMS.kA / MecanumDrive.PARAMS.inPerTick);
            final double leftFrontPower = feedforward.compute(wheelVels.leftFront) / voltage;
            final double leftBackPower  = feedforward.compute(wheelVels.leftBack) / voltage;
            final double rightBackPower = feedforward.compute(wheelVels.rightBack) / voltage;
            final double rightFrontPower = feedforward.compute(wheelVels.rightFront) / voltage;
	        MecanumDrive.this.mecanumCommandWriter.write(new MecanumCommandMessage(
                    voltage, leftFrontPower, leftBackPower, rightBackPower, rightFrontPower
            ));

	        MecanumDrive.this.leftFront.setPower(leftFrontPower);
	        MecanumDrive.this.leftBack.setPower(leftBackPower);
	        MecanumDrive.this.rightBack.setPower(rightBackPower);
	        MecanumDrive.this.rightFront.setPower(rightFrontPower);

            p.put("x", MecanumDrive.this.pose.position.x);
            p.put("y", MecanumDrive.this.pose.position.y);
            p.put("heading (deg)", Math.toDegrees(MecanumDrive.this.pose.heading.toDouble()));

            final Pose2d error = txWorldTarget.value().minusExp(MecanumDrive.this.pose);
            p.put("xError", error.position.x);
            p.put("yError", error.position.y);
            p.put("headingError (deg)", Math.toDegrees(error.heading.toDouble()));

            // only draw when active; only one drive action should be active at a time
            final Canvas c = p.fieldOverlay();
	        MecanumDrive.this.drawPoseHistory(c);

            c.setStroke("#4CAF50");
            Drawing.drawRobot(c, txWorldTarget.value());

            c.setStroke("#3F51B5");
            Drawing.drawRobot(c, MecanumDrive.this.pose);

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
	            MecanumDrive.this.leftFront.setPower(0);
	            MecanumDrive.this.leftBack.setPower(0);
	            MecanumDrive.this.rightBack.setPower(0);
	            MecanumDrive.this.rightFront.setPower(0);

                return false;
            }

            final Pose2dDual<Time> txWorldTarget = this.turn.get(t);
	        MecanumDrive.this.targetPoseWriter.write(new PoseMessage(txWorldTarget.value()));

            final PoseVelocity2d robotVelRobot = MecanumDrive.this.updatePoseEstimate();

            final PoseVelocity2dDual<Time> command = new HolonomicController(MecanumDrive.PARAMS.axialGain, MecanumDrive.PARAMS.lateralGain, MecanumDrive.PARAMS.headingGain, MecanumDrive.PARAMS.axialVelGain, MecanumDrive.PARAMS.lateralVelGain, MecanumDrive.PARAMS.headingVelGain
            )
                    .compute(txWorldTarget, MecanumDrive.this.pose, robotVelRobot);
	        MecanumDrive.this.driveCommandWriter.write(new DriveCommandMessage(command));

            final MecanumKinematics.WheelVelocities<Time> wheelVels = MecanumDrive.this.kinematics.inverse(command);
            final double                                  voltage   = MecanumDrive.this.voltageSensor.getVoltage();
            MotorFeedforward                        feedforward = new MotorFeedforward(MecanumDrive.PARAMS.kS, MecanumDrive.PARAMS.kV / MecanumDrive.PARAMS.inPerTick, MecanumDrive.PARAMS.kA / MecanumDrive.PARAMS.inPerTick);
            final double leftFrontPower = feedforward.compute(wheelVels.leftFront) / voltage;
            final double leftBackPower  = feedforward.compute(wheelVels.leftBack) / voltage;
            final double rightBackPower = feedforward.compute(wheelVels.rightBack) / voltage;
            final double rightFrontPower = feedforward.compute(wheelVels.rightFront) / voltage;
	        MecanumDrive.this.mecanumCommandWriter.write(new MecanumCommandMessage(
                    voltage, leftFrontPower, leftBackPower, rightBackPower, rightFrontPower
            ));

	        MecanumDrive.this.leftFront.setPower(feedforward.compute(wheelVels.leftFront) / voltage);
	        MecanumDrive.this.leftBack.setPower(feedforward.compute(wheelVels.leftBack) / voltage);
	        MecanumDrive.this.rightBack.setPower(feedforward.compute(wheelVels.rightBack) / voltage);
	        MecanumDrive.this.rightFront.setPower(feedforward.compute(wheelVels.rightFront) / voltage);

            final Canvas c = p.fieldOverlay();
	        MecanumDrive.this.drawPoseHistory(c);

            c.setStroke("#4CAF50");
            Drawing.drawRobot(c, txWorldTarget.value());

            c.setStroke("#3F51B5");
            Drawing.drawRobot(c, MecanumDrive.this.pose);

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

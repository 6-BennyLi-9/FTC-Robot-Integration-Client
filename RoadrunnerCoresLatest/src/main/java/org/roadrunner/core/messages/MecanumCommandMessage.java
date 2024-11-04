package org.roadrunner.core.messages;

public final class MecanumCommandMessage {
    public long timestamp;
    public double voltage;
    public double leftFrontPower;
    public double leftBackPower;
    public double rightBackPower;
    public double rightFrontPower;

    public MecanumCommandMessage(final double voltage, final double leftFrontPower, final double leftBackPower, final double rightBackPower, final double rightFrontPower) {
        timestamp = System.nanoTime();
        this.voltage = voltage;
        this.leftFrontPower = leftFrontPower;
        this.leftBackPower = leftBackPower;
        this.rightBackPower = rightBackPower;
        this.rightFrontPower = rightFrontPower;
    }
}

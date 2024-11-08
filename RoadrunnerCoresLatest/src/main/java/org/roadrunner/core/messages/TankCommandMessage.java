package org.roadrunner.core.messages;

public final class TankCommandMessage {
    public long timestamp;
    public double voltage;
    public double leftPower;
    public double rightPower;

    public TankCommandMessage(final double voltage, final double leftPower, final double rightPower) {
        timestamp = System.nanoTime();
        this.voltage = voltage;
        this.leftPower = leftPower;
        this.rightPower = rightPower;
    }
}

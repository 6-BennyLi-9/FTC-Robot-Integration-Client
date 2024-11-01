package org.roadrunner.core.messages;

import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;

public final class ThreeDeadWheelInputsMessage {
    public long timestamp;
    public PositionVelocityPair par0;
    public PositionVelocityPair par1;
    public PositionVelocityPair perp;

    public ThreeDeadWheelInputsMessage(final PositionVelocityPair par0, final PositionVelocityPair par1, final PositionVelocityPair perp) {
        timestamp = System.nanoTime();
        this.par0 = par0;
        this.par1 = par1;
        this.perp = perp;
    }
}
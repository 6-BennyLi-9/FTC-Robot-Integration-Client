package org.firstinspires.ftc.teamcode.Hardwares.Single;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class SimpleMotor {
    DcMotorEx motor;
    /**
     * 不可轻易修改
     */
    public int targetPosition,bufVal=20;
    public double power=0.6f;
    public boolean paused;
    public double getEncoder(){
        return motor.getCurrentPosition();
    }

    /**
     * @param power 不可以为0,如果想要中止电机，更改paused的值
     */
    public void setTarget(int position,double power){
        targetPosition=position;
        if(power==0)return;
        this.power=Math.abs(power);
        power=Math.min(1,power);
    }
    public void update(){
        motor.setTargetPositionTolerance(bufVal);

        motor.setTargetPosition(targetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motor.setPower(paused? 0:power);
    }
    public boolean done(){
        return Math.abs(motor.getCurrentPosition()-targetPosition)<bufVal;
    }
}

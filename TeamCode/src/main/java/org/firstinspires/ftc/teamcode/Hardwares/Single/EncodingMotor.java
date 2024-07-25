package org.firstinspires.ftc.teamcode.Hardwares.Single;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class EncodingMotor {
    DcMotorEx motor;
    /**
     * 不可轻易修改
     */
    protected int targetPosition,bufVal=20;
    protected double power=0.6f;
    boolean paused;
    public double getEncoder(){
        return motor.getCurrentPosition();
    }
    public void setTargetPosition(int encoder){
        targetPosition=encoder;
    }

    /**
     * @param power 不可以为0,如果想要中止电机，更改paused的值
     */
    public void setPower(double power){
        if(power==0)return;
        this.power=Math.abs(power);
        power=Math.min(1,power);
    }

    public void update(){
        if(paused){
            motor.setPower(0);
        }else{
            double encoder=getEncoder();
            if(encoder+bufVal<targetPosition){
                motor.setPower(power);
            } else if (encoder-bufVal>targetPosition) {
                motor.setPower(-power);
            }else{
                motor.setPower(0);
            }
        }
    }
}

package org.firstinspires.ftc.teamcode.Utils;

import org.firstinspires.ftc.teamcode.Utils.Annotations.UtilFunctions;

public class Timer {
    public double StartTime,EndTime;
    public Timer(){
        StartTime=Functions.getCurrentTimeMills();
    }
    /**重新定义<code>StartTime</code>*/
    @UtilFunctions
    public final void restart(){
        StartTime=Functions.getCurrentTimeMills();
    }
    /**定义<code>EndTime</code>*/
    @UtilFunctions
    public final void stop(){
        EndTime=Functions.getCurrentTimeMills();
    }
    /**获取<code>EndTime-StartTime</code>*/
    @UtilFunctions
    public final double getDeltaTime(){
        return EndTime-StartTime;
    }
    /**定义<code>EndTime</code>并获取<code>EndTime-StartTime</code>*/
    @UtilFunctions
    public final double stopAndGetDeltaTime(){
        stop();
        return getDeltaTime();
    }
    /**定义<code>EndTime</code>并获取<code>EndTime-StartTime</code>并重新定义<code>StartTime</code>*/
    @UtilFunctions
    public final double restartAndGetDeltaTime(){
        double res=stopAndGetDeltaTime();
        restart();
        return res;
    }
    /**重新定义<code>StartTime</code>并定义<code>EndTime</code>*/
    @UtilFunctions
    public final void stopAndRestart(){
        stop();
        restart();
    }
}

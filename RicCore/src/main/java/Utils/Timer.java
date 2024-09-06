package Utils;

public class Timer {
    public double StartTime,EndTime;
    public Timer(){
        StartTime=Functions.getCurrentTimeMills();
    }
    /**重新定义<code>StartTime</code>*/
    public final void restart(){
        StartTime=Functions.getCurrentTimeMills();
    }
    /**定义<code>EndTime</code>*/
    public final void stop(){
        EndTime=Functions.getCurrentTimeMills();
    }
    /**获取<code>EndTime-StartTime</code>*/
    public final double getDeltaTime(){
        return EndTime-StartTime;
    }
    /**定义<code>EndTime</code>并获取<code>EndTime-StartTime</code>*/
    public final double stopAndGetDeltaTime(){
        stop();
        return getDeltaTime();
    }
    /**定义<code>EndTime</code>并获取<code>EndTime-StartTime</code>并重新定义<code>StartTime</code>*/
    public final double restartAndGetDeltaTime(){
        double res=stopAndGetDeltaTime();
        restart();
        return res;
    }
}

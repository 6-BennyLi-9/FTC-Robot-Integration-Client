package org.firstinspires.ftc.teamcode.utils;

import org.firstinspires.ftc.teamcode.utils.Annotations.UtilFunctions;

public final class Functions {
    @UtilFunctions
    public static double getCurrentTimeMills(){
        return System.nanoTime()/1.0E06;
    }
}

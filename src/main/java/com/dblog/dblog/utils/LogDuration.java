package com.dblog.dblog.utils;

import java.time.Duration;

public class LogDuration {
    private static final Logger logger = new Logger();
    public static void logDuration(String serviceName, Duration duration, int size){
        //System.out.println("Service: "+serviceName + " took " +duration.toMillis()+ " ms to return " + size + " records");
        logger.log(Logger.LogLevel.INFO,("Service: "+serviceName + " took " +duration.toMillis()+ " ms to return " + size + " records"));
    }
}

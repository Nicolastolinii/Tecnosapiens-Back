package com.dblog.dblog.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    // private static final LoggerHelper logger = new LoggerHelper();

    private static final String YELLOW = "\u001B[33;40m";
    private static final String RED = "\u001B[31;40m";
    private static final String WHITE = "\u001B[37;40m";
    private static final String DEFAULT = "\u001B[0m";

    /**
     * Enum LogLevel define diferentes niveles de mensajes de log.
     */
    public enum LogLevel {
        INFO, ERROR, WARNING;
    }

    /**
     * Registra un mensaje con el nivel de log especificado.
     *
     * @param level   El nivel de log del mensaje (INFO, ERROR, WARNING).
     * @param message El mensaje que se va a registrar.
     */
    public void log(LogLevel level, String message) {
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String formattedLevel = String.format("[ %s - %s ]", formattedTime, level);
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[3];
        String sourceFile = caller.getFileName() + ":" + caller.getLineNumber();
        String formattedMessage = String.format("%s ==> %s", formattedLevel, message);

        switch (level) {
            case INFO:
                printColoredMessage(YELLOW, formattedMessage, sourceFile);
                break;
            case ERROR:
                printColoredMessage(RED, formattedMessage, sourceFile);
                break;
            case WARNING:
                printColoredMessage(WHITE, formattedMessage, sourceFile);
                break;
        }
    }

    /**
     * Imprime un mensaje de log con color.
     *
     * @param color    El color del mensaje.
     * @param level    El nivel de log del mensaje.
     * @param message  El mensaje que se va a imprimir.
     */
    private void printColoredMessage(String color, String level, String message) {
        System.out.println(color + level + " ==> " + message + DEFAULT);
    }
}

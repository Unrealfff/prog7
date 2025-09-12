package org.unrealfff.utility;

import java.io.Serializable;

/**
 * Класс для отслеживания результа выполнения команды
 */
public class Response implements Serializable {
    private int exitCode;
    private String message;

    public Response(int code, String message){
        this.exitCode = code;
        this.message = message;
    }

    public Response(String s){
        this(1,s);
    }

    public String getMessage() {
        return message;
    }
    public int getExitCode(){
        return exitCode;
    }
    public String toString(){
        return String.valueOf(exitCode)+';'+message;
    }
}

package org.unrealfff.commands;

import java.io.Serializable;

/**
 * Класс контейнер для передачи команд между клиентом и сервером
 */
public class Container implements Serializable {
    private static final long serialVersionUID = 15L;
    private Commands commands;
    private String args;
    private String[] login;

    public Container(Commands commands, String args, String[] login) {
        this.commands = commands;
        this.args = args;
        this.login = login;
    }

    public String[] getLogin() { return login;}

    /**
     * Метод для получения типа команды
     * @return тип команды
     */
    public Commands getCommandType() {
        return commands;
    }

    /**
     * Метод для получения аргументов команды
     * @return аргументы команды
     */
    public String getArgs() {
        return args;
    }
}
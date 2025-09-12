package org.managers;

import org.unrealfff.commands.Command;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Менеджер команд.
 */
public class CommandManager {
    private final Map<String, Command> commandMap = new LinkedHashMap<>();
    private final List<String> commandHistory = new ArrayList<String>();


    /**
     * Функция для добавления команды для менеджера.
     * @param CommandName Название команды.
     * @param command Объект команды.
     */
    public void register(String CommandName, Command command){
        commandMap.put(CommandName, command);
    }

    /**
     * Геттер для команд.
     * @return Словарь зарегистрированных команд.
     */
    public Map<String, Command> getCommands() {
        return commandMap;
    }

    /**
     * Геттер для истории команд.
     * @return список выполненных команд по порядку.
     */
    public List<String> getCommandHistory() {
        return commandHistory;
    }

    /**
     * Добавление последней команды в историю команд.
     * @param CommandName, String название последней команды
     */
    public void addToHistory(String CommandName){
        commandHistory.add(CommandName);
    }
}
package org.commands;

import org.unrealfff.commands.Command;
import org.unrealfff.utility.Console;
import org.unrealfff.utility.Response;
/**
 * Класс для команды для вывода списка команд из файла
 */
public class ExecuteScript extends Command {
    private final Console console;

    /**
     * Стандартный конструктор команды execute_script
     *
     * @param c консоль для ввода/вывода
     */
    public ExecuteScript(Console c) {
        super("execute_script", "execute_script scriptName - executes the script from file");
        console = c;
    }

    /**
     * Выполняет команду execute_script
     *
     * @return ResultResponse - результат выполнения команды.
     */
    @Override
    public Response execute(String[] arguments) {
        if (arguments[1].isEmpty()) return new Response(0, "wrong usage of the command '" + getName() + "'");

        return new Response("executing script '" + arguments[1] + "'...");
    }
}
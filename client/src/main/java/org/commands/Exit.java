package org.commands;


import org.unrealfff.commands.Command;
import org.unrealfff.utility.Console;
import org.unrealfff.utility.Response;

/**
 * Команда exit осуществляет выход из приложения
 */
public class Exit extends Command {
    private final Console console;

    /**
     * Стандартный конструктор команды exit.
     * @param c Консоль для ввода/вывода.
     */
    public Exit(Console c){
        super("exit", "exit - exits program");
        console = c;
    }

    /**
     * Выполняет команду exit.
     * @return ResultResponse - результат выполнения команды.
     */
    @Override
    public Response execute(String[] arguments) {
        if (!arguments[1].isEmpty()) return new Response(0, "wrong usage of the command '" + getName() + "'");
        return new Response("exit");
    }
}
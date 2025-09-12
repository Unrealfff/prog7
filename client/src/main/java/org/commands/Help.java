package org.commands;

import org.unrealfff.commands.Command;
import org.unrealfff.commands.Commands;
import org.unrealfff.utility.Console;
import org.unrealfff.utility.Response;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * класс команды для вывода справки по всем доступным командам
 */
public class Help extends Command {
    private final Console console;
    private Map<Commands, String[]> commands;

    public Help(Console console, Map<Commands, String[]> commands) {
        super("help", "help - shows available commands");
        this.console = console;
        this.commands = commands;
    }

    /**
     * Исполнение команды
     *
     * @param args Аргументы
     * @return результат выполнения команды
     */
    @Override
    public Response execute(String[] args, String[] login) {
        if (!args[1].isEmpty())
            return new Response(0, "wrong usage of the command '" + getName() + "'");
        return new Response(commands.keySet().stream().map(comand -> String.format("%s - %sn", commands.get(comand)[0], commands.get(comand)[1])).collect(Collectors.joining("\n")));
    }

    public Response execute(String[] args) {
        if (!args[1].isEmpty())
            return new Response(0, "wrong usage of the command '" + getName() + "'");
        return new Response(commands.keySet().stream().map(comand -> String.format("%s - %sn", commands.get(comand)[0], commands.get(comand)[1])).collect(Collectors.joining("\n")));
    }
}
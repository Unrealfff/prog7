package org.commands;

import org.unrealfff.commands.Command;
import org.unrealfff.commands.Commands;
import org.unrealfff.utility.Console;
import org.unrealfff.utility.Response;
import org.utility.Runner;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * класс команды для вывода истории команд
 */
public class History extends Command {
    private final Console console;
    private Runner runner;

    public History(Console console, Runner runner) {
        super("history", "history - displays 9 last commands");
        this.console = console;
        this.runner = runner;
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
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            if (i >= runner.getCommandHistory().size()) break;
            stringBuilder.append(runner.getCommandHistory().get(i +
                    runner.getCommandHistory().size() - (Math.min(runner.getCommandHistory().size(),
                    9))) + "\n");
        }
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        return new Response(stringBuilder.toString());
    }
    public Response execute(String[] args) {
        if (!args[1].isEmpty())
            return new Response(0, "wrong usage of the command '" + getName() + "'");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            if (i >= runner.getCommandHistory().size()) break;
            stringBuilder.append(runner.getCommandHistory().get(i +
                    runner.getCommandHistory().size() - (Math.min(runner.getCommandHistory().size(),
                    9))) + "\n");
        }
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        return new Response(stringBuilder.toString());
    }
}


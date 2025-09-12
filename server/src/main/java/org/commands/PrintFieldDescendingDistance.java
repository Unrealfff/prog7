package org.commands;

import org.managers.CollectionManager;
import org.managers.CommandManager;
import org.managers.FileManager;
import org.unrealfff.commands.Command;
import org.unrealfff.managers.Handler;
import org.unrealfff.models.Route;
import org.unrealfff.utility.Console;
import org.unrealfff.utility.Response;

import java.util.ArrayList;
import java.util.Comparator;
/**
 * Класс команды для вывода всех дистанций в порядке убывания
 */
public class PrintFieldDescendingDistance extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    private final FileManager fileManager;

    /**
     * Стандартный конструктор команды add
     *
     * @param console
     * @param cm
     */
    public PrintFieldDescendingDistance(Console console, CollectionManager cm, FileManager fileManager) {
        super("print_field_descending_distance", "print_field_descending_distance - prints all distances in descending order");
        this.console = console;
        this.collectionManager = cm;
        this.fileManager = fileManager;
    }

    /**
     * Применение команды Add.
     *
     * @param arguments Аргумент для выполнения (проверяем, что их не передали).
     * @return ResultResponse - результат выполнения команды.
     */
    @Override
    public Response execute(String[] arguments, String[] login) {
        if (!arguments[1].isEmpty()) return new Response(0, "wrong usage of the command '" + getName() + "'");
        Integer userId = getUserId(login);
        if (userId == -1) {
            return new Response(0, "could not login user");
        }
        var srt = new ArrayList<>(collectionManager.getCollection());
        srt.sort(new ReverseSortByDistance());
        StringBuilder stringBuilder = new StringBuilder();
        for(var i : srt) stringBuilder.append(i.getDistance() + "\n");
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        return new Response(stringBuilder.toString());
    }

    private Integer getUserId(String[] login) {
        Integer userId = fileManager.login(login);
        return  userId;
    }
}


class ReverseSortByDistance implements Comparator<Route> {
    /**
     * comparator for reversed sort
     * @param a
     * @param b
     * @return
     */
    public int compare(Route a, Route b) {
        if (a.getDistance() != null && b.getDistance() != null) {
            return Float.compare(b.getDistance(), a.getDistance());
        }
        else if(a.getDistance() != null) {
            return -1;
        }
        else if(b.getDistance() != null) {
            return 1;
        }
        else{
            return 0;
        }
    }
}
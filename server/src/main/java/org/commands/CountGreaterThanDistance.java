package org.commands;

import org.managers.CollectionManager;
import org.managers.FileManager;
import org.unrealfff.commands.Command;
import org.unrealfff.managers.Handler;
import org.unrealfff.models.Route;
import org.unrealfff.utility.Console;
import org.unrealfff.utility.Response;
import java.util.Iterator;
/**
 * Класс команды для подсчёта объектов с большей дистанцией
 */
public class CountGreaterThanDistance extends Command{
    private final Console console;
    private final CollectionManager collectionManager;
    private final FileManager fileManager;

    /**
     * Стандартный конструктор команды add
     *
     * @param console
     * @param cm
     */
    public CountGreaterThanDistance(Console console, CollectionManager cm, FileManager fileManager) {
        super("count_greater_than_distance", "count_greater_than_distance distance - number of elements with distance greater then given");
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
        if (arguments[1].isEmpty()) return new Response(0, "wrong usage of the command '" + getName() + "'");
        Integer userId = getUserId(login);
        if (userId == -1) {
            return new Response(0, "could not login user");
        }
        Float distance;
        try {
            distance = Float.valueOf(arguments[1]);
        }
        catch (NumberFormatException e) {
            return new Response("wrong usage of the command '" + getName() + "'");
        }
        Iterator<Route> iter = collectionManager.getCollection().iterator();
        Integer ans = 0;
        while (iter.hasNext()) {
            var curr = iter.next();
            if(curr.getDistance() != null && curr.getDistance().compareTo(distance) > 0) {
                ans++;
            }
        }
        return new Response(ans.toString());
    }

    private Integer getUserId(String[] login) {
        Integer userId = fileManager.login(login);
        return  userId;
    }
}

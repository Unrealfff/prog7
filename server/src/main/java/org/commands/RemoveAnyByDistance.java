package org.commands;

import org.managers.CollectionManager;
import org.managers.CommandManager;
import org.managers.FileManager;
import org.unrealfff.commands.Command;
import org.unrealfff.managers.Handler;
import org.unrealfff.models.Route;
import org.unrealfff.utility.Console;
import org.unrealfff.utility.Response;
/**
 * Класс команды для удаления 1 объекта с данной дистанцией
 */
public class RemoveAnyByDistance extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    private final FileManager fileManager;
    public RemoveAnyByDistance(Console console, CollectionManager collectionManager, FileManager fileManager) {
        super("remove_any_by_distance", "remove_any_by_distance distance - removes 1 element with given distance");
        this.console = console;
        this.collectionManager = collectionManager;
        this.fileManager = fileManager;
    }
    /**
     * Исполнение команды
     * @param args Аргументы
     * @return результат выполнения команды
     */
    @Override
    public Response execute(String[] args, String[] login) {
        if (args[1].isEmpty()) {
            return new Response(0, "wrong usage of the command " + getName() + "'");
        }
        Integer userId = getUserId(login);
        if (userId == -1) {
            return new Response(0, "could not login user");
        }

        Float distance = null;
        try {
            distance = Float.parseFloat(args[1].trim());
        } catch (NumberFormatException e) {
            return new Response(0, "wrong usage of the command " + getName() + "'");
        }
        var iter = collectionManager.getCollection().iterator();
        Integer id = -1;
        boolean flag = false;
        while (iter.hasNext()) {
            var i = iter.next();
            if(i.getDistance() != null && i.getDistance().equals(distance) && i.getUser().equals(userId)) {
                id = i.getId();
                flag = true;
                collectionManager.remove(i.getId(), userId);
                break;
            }
        }
        if (flag) return new Response(1, "Successfully removed element with id " + id);
        else return new Response(0, "no element with such distance");
    }

    private Integer getUserId(String[] login) {
        Integer userId = fileManager.login(login);
        return  userId;
    }
}

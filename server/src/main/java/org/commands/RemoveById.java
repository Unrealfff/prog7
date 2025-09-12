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
 * Класс команды для удаления элемента коллекции по id
 */
public class RemoveById extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    private final FileManager fileManager;
    public RemoveById(Console console, CollectionManager collectionManager, FileManager fileManager) {
        super("remove_id", "remove_by_id id - removes element with given id");
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

        Integer id = -1;
        try {
            id = Integer.parseInt(args[1].trim());
            //System.out.println("Attempting to remov " + id);
        } catch (NumberFormatException e) {
            //System.out.println("Error: Invalid ID format");
            return new Response(0, "wrong usage of the command " + getName() + "'");
        }

        if (collectionManager.getById(id) == null || !collectionManager.getCollection().contains(collectionManager.getById(id))) {
            //System.out.println("Error:  with ID " + id + " not found");
            return new Response(0, "no element with such id");
        }

        if(collectionManager.remove(id, userId)) return new Response(1, "Successfully removed element with id " + id);
        return new Response(0, "could not remove this element");
        //System.out.println("Successfully removed element with id: " + id);
    }

    private Integer getUserId(String[] login) {
        Integer userId = fileManager.login(login);
        return  userId;
    }
}
package org.commands;

import org.managers.CollectionManager;
import org.managers.CommandManager;
import org.managers.FileManager;
import org.unrealfff.commands.Command;
import org.unrealfff.managers.Handler;
import org.unrealfff.models.Route;
import org.unrealfff.utility.Console;
import org.unrealfff.utility.Response;

import java.time.LocalDate;
import java.util.ArrayList;
/**
 * Класс команды для для удаления всех объектов, меньших чем заданных
 */
public class RemoveLower extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    private final FileManager fileManager;
    public RemoveLower(Console console, CollectionManager collectionManager, FileManager fileManager) {
        super("remove_lower", "remove_lower {element} - removes all elements, lower than given");
        this.console = console;
        this.collectionManager = collectionManager;
        this.fileManager = fileManager;
    }
    /**
     * Исполнение команды
     * @param arguments Аргументы
     * @return результат выполнения команды
     */
    @Override
    public Response execute(String[] arguments, String[] login) {
        if (arguments[1].isEmpty()) {
            return new Response(0, "wrong usage of the command " + getName() + "'");
        }
        Integer userId = getUserId(login);
        if (userId == -1) {
            return new Response(0, "could not login user");
        }
        String[] routeData = arguments[1].split(",");
        routeData[routeData.length - 1] = userId.toString();
        if (routeData.length < 7) {
            System.out.println("Error: Insufficient data fields received: " + routeData.length + " (required: 7)");
            return new Response(0, "could not create object - data is missing");
        }

        // Временно установим ID в 1, он будет заменен в методе add коллекции
        routeData[0] = "1";

        System.out.println("Creating Route object from provided data...");

        // Create Route with temporary ID
        Route route = Route.fromArray(routeData);
        if (route == null) {
            System.out.println("Error: Failed to create Route object. Invalid data format");
            return new Response(0, "could not create object - data is incorrect");
        }

        if (!route.validate()) {
            System.out.println("Error: Route object validation failed");
            return new Response(0, "could not create object - data is incorrect");
        }
        var iter = collectionManager.getCollection().iterator();
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Integer> ids = new ArrayList<>();
        while(iter.hasNext()) {
            Route cur = iter.next();
            if (cur.getDistance() != null && cur.compareTo(route) < 0 && cur.getUser().equals(userId)) {
                stringBuilder.append("Successfully removed element with id " + cur.getId() + "\n");
                ids.add(cur.getId());
            }
        }
        for(Integer i : ids) collectionManager.remove(i, userId);
        if(!stringBuilder.isEmpty()) stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        else stringBuilder.append("no lower elements");
        return new Response(stringBuilder.toString());
    }

    private Integer getUserId(String[] login) {
        Integer userId = fileManager.login(login);
        return  userId;
    }
}

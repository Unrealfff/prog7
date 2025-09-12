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
/**
 * Класс команды для обновления элемента
 */
public class Update extends Command{
    private final Console console;
    private final CollectionManager collectionManager;
    private final FileManager fileManager;
    /**
     * Стандартный конструктор команды add
     * @param console console
     * @param cm commandmanager
     */
    public Update(Console console, CollectionManager cm, FileManager fileManager){
        super("update", "update id {element} - updates every field of route with given id in the collection");
        this.console = console;
        this.collectionManager = cm;
        this.fileManager = fileManager;
    }

    /**
     * Применение команды Add.
     * @param arguments Аргумент для выполнения (проверяем, что их не передали).
     * @return ResultResponse - результат выполнения команды.
     */
    @Override
    public Response execute(String[] arguments, String[] login) {
        if (arguments[1].isEmpty()) return new Response(0, "wrong usage of the command '"+ getName()+"'");
        Integer id;
        System.out.println("Processing new element addition...");
        Integer userId = getUserId(login);
        if (userId == -1) {
            return new Response(0, "could not login user");
        }
        // Split input data
        String[] routeData = arguments[1].split(",");
        routeData[routeData.length - 1] = userId.toString();
        if (routeData.length < 8) {
            System.out.println("Error: Insufficient data fields received: " + routeData.length + " (required: 8)");
            return new Response(0, "could not create object - data is missing");
        }

        // Временно установим ID в 1, он будет заменен в методе add коллекции
        //routeData[0] = "1";

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
        if (collectionManager.getById(route.getId()).getUser().equals(userId)) {
            if (collectionManager.update(route)) {
                return new Response("router updated");
            }
            else {
                return new Response("this route does not exist");
            }
        }
        return new Response(0, "could not update given element")  ;
    }

    private Integer getUserId(String[] login) {
        Integer userId = fileManager.login(login);
        return  userId;
    }
}

package org.commands;

import org.managers.CollectionManager;
import org.managers.FileManager;
import org.unrealfff.commands.Command;
import org.unrealfff.managers.Handler;
import org.unrealfff.models.Route;
import org.unrealfff.utility.Console;
import org.unrealfff.utility.Response;

import java.time.LocalDate;
/**
 * Класс команды для добавления элемента в коллекцию
 */
public class Add extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    private final FileManager fileManager;
    /**
     * Стандартный конструктор команды add
     * @param console
     * @param cm
     */
    public Add(Console console, CollectionManager cm, FileManager fileManager){
        super("add (element)", "add - adds 1 element to collection");
        this.console = console;
        this.collectionManager = cm;
        this.fileManager = fileManager;
    }



    private Integer getUserId(String[] login) {
        Integer userId = fileManager.login(login);
        return  userId;
    }
    /**
     * Применение команды Add.
     * @param arguments Аргумент для выполнения (проверяем, что их не передали).
     * @return ResultResponse - результат выполнения команды.
     */
    @Override
    public Response execute(String[] arguments, String[] login) {
        try {
            if(arguments.length < 2 || arguments[1].isEmpty()) {
                System.out.println("Error: wrong arguments");
                return new Response(0, "wrong usage of the command '" + getName() + "'");
            }
            Integer userId = getUserId(login);
            if (userId == -1) {
                return new Response(0, "could not login user");
            }

            System.out.println("Processing new element addition...");

            // Split input data
            String[] routeData = arguments[1].split(",");
            routeData[routeData.length - 1] = userId.toString();
            if (routeData.length < 8) {
                System.out.println("Error: Insufficient data fields received: " + routeData.length + " (required: 8)");
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

            /*if (!route.validate()) {
                System.out.println("Error: Route object validation failed");
                return new Response(0, "Объект не прошел валидацию. Проверьте обязательные поля.");
            }*/

            System.out.println("Attempting to add Route to collection...");

            // Добавляем дракона в коллекцию (ID будет установлен автоматически)
            Integer curId = fileManager.add(route);
            if (!curId.equals( -1)) {
                route.setId(curId);
                collectionManager.add(route);
                System.out.println("Route successfully added to collection");
                return new Response(1, "object was added to the collection");
            } else {
                System.out.println("Error: Failed to add Route to collection");
                return new Response(0, "could not add object to the collection");
            }
        } catch (Exception e) {
            System.out.println("Error occurred while adding element: " + e.getMessage());
            return new Response(0, "could not add object to the collection: " + e.getMessage());
        }
    }
}
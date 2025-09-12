package org.managers;

import org.unrealfff.commands.Commands;
import org.unrealfff.commands.Container;
import org.unrealfff.utility.Console;
import org.unrealfff.utility.Response;

/**
 * класс для исполнения команд
 */
public class Router {
    Console console;
    CommandManager commandManager;

    public Router(Console console, CommandManager commandManager) {
        this.console = console;
        this.commandManager = commandManager;
    }
    public Response run(Container command) {
        String[] userCommand = new String[2];
        if (command != null) {
            userCommand[0] = command.getCommandType().Type();
            userCommand[1] = command.getArgs();
            String[] user = command.getLogin();
            //System.out.println(userCommand[1]);

            System.out.println("got command " + userCommand[0]);

            var commandHandler = commandManager.getCommands().get(userCommand[0]);
            Response response;

            if (userCommand[0].equals("")) {
                response = new Response(0, "empty command");
            } else if (commandHandler == null) {
                response = new Response(0,
                        "command '" + userCommand[0] + "' not found");
            } else {
                // Если это команда exit, сохраняем коллекцию перед выполнением
                /*if (command.getCommandType() == Commands.Exit) {
                    System.out.println("got command exit");
                    try {
                        commandManager.getCommands().get("save").execute(new String[]{"save", ""});
                        System.out.println("collection has been saved");
                    } catch (Exception e) {
                        System.err.println("couldn't save the collection: " + e.getMessage());
                    }
                }*/

                response = commandHandler.execute(userCommand, user);
                System.out.println("command '" + userCommand[0] + "' executed");
            }

            //networkManager.sendData(NetworkManager.serializer(response)); //return
            return response;
        }
        return new Response(0, "null command");
        // Небольшая задержка для снижения нагрузки на процессор
    }
}

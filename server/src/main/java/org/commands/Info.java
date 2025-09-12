package org.commands;

import org.managers.CollectionManager;
import org.managers.CommandManager;
import org.managers.FileManager;
import org.unrealfff.commands.Command;
import org.unrealfff.managers.Handler;
import org.unrealfff.models.Route;
import org.unrealfff.utility.Console;
import org.unrealfff.utility.Response;
import java.time.LocalDateTime;

/**
 * Класс команды для получения информации о коллекции
 */
public class Info extends Command {
    private final Console console;
    public final CollectionManager collectionManager;
    private final FileManager fileManager;

    public Info(Console console, CollectionManager collectionManager, FileManager fileManager) {
        super("info", "info - displays information about collection");
        this.console = console;
        this.collectionManager = collectionManager;
        this.fileManager = fileManager;
    }

    /**
     * Исполнение команды
     *
     * @param args Аргументы
     * @return результат выполнения команды
     */
    @Override
    public Response execute(String[] args, String[] login) {
        if (!args[1].isEmpty()) {
            System.out.println("Error: Command doesn't accept any arguments");
            return new Response(0, "wrong usage of the command '" + getName() + "'");
        }
        Integer userId = getUserId(login);
        if (userId == -1) {
            return new Response(0, "could not login user");
        }

        //System.out.println("Retrieving collection information...");

        LocalDateTime lastInitTime = collectionManager.getLastInitTime();
        String lastInitTimeString = (lastInitTime == null) ? "not initialized yet" :
                lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString();

        LocalDateTime lastSaveTime = collectionManager.getLastSaveTime();
        String lastSaveTimeString = (lastSaveTime == null) ? "not saved yet" :
                lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();

        // Формируем ответ для клиента на русском
        String clientResponse = "collection info:\n" +
                " type - " + collectionManager.getCollection().getClass().toString() + "\n" +
                " size - " + collectionManager.getCollection().size() + "\n" +
                " last save - " + (lastSaveTime == null ? "collection has not been saved yet" :
                lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString()) + "\n" +
                " last init - " + (lastInitTime == null ? "collection has not been inited yet" :
                lastInitTime.toLocalDate().toString() + " " + lastInitTime.toLocalTime().toString());

        return new Response(clientResponse);
    }

    private Integer getUserId(String[] login) {
        Integer userId = fileManager.login(login);
        return  userId;
    }
}
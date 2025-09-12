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
 * Класс команды для вывода
 */
public class Show extends Command{
    private final Console console;
    private final CollectionManager collectionManager;
    private final FileManager fileManager;
    /**
     * Стандартный конструктор команды clear
     * @param console консоль для ввода/вывода
     * @param collectionManager менеджер коллекции для взаимодействия
     */
    public Show(Console console, CollectionManager collectionManager, FileManager fileManager) {
        super("show", "show - shows collection");
        this.console = console;
        this.collectionManager = collectionManager;
        this.fileManager = fileManager;
    }

    /**
     * Выполняет команду Show.
     * @return ResultResponse - результат выполнения команды.
     */
    @Override
    public Response execute(String[] arguments, String[] login) {
        if (!arguments[1].isEmpty()) return new Response(0, "wrong usage of the command '" + getName() + "'");
        Integer userId = getUserId(login);
        if (userId == -1) {
            return new Response(0, "could not login user");
        }
        return new Response(collectionManager.toString());
    }

    private Integer getUserId(String[] login) {
        Integer userId = fileManager.login(login);
        return  userId;
    }
}
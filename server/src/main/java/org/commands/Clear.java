package org.commands;

import org.managers.CollectionManager;
import org.managers.FileManager;
import org.unrealfff.commands.Command;
import org.unrealfff.managers.Handler;
import org.unrealfff.models.Route;
import org.unrealfff.utility.Console;
import org.unrealfff.utility.Response;

/**
 * Команда clear очищает колллекцию
 */
public class Clear extends Command{
    final private Console console;
    final private CollectionManager collectionManager;
    private final FileManager fileManager;
    /**
     * Стандартный конструктор команды clear
     * @param c консоль для ввода/вывода
     * @param cm менеджер коллекции для взаимодействия
     */
    public Clear(Console c,CollectionManager cm, FileManager fileManager){
        super("clear", "clear - clears collection");
        console = c;
        this.collectionManager = cm;
        this.fileManager = fileManager;


    }
    /**
     * Выполняет команду clear
     * @return ResultResponse - результат выполнения команды.
     */
    @Override
    public Response execute(String[] arguments, String[] login) {
        if (!arguments[1].isEmpty()) return new Response(0, "wrong usage of the command '" + getName() + "'");
        Integer userId = getUserId(login);
        if (userId == -1) {
            return new Response(0, "could not login user");
        }
        if(collectionManager.clear(userId)) return new Response("collection has been cleared");
        return new Response(0, "could not delete all elements beloning to user " + login[0]);
    }

    private Integer getUserId(String[] login) {
        Integer userId = fileManager.login(login);
        return  userId;
    }
}

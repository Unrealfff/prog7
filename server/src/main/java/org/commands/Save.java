package org.commands;

import org.managers.CollectionManager;
import org.managers.CommandManager;
import org.unrealfff.commands.Command;
import org.unrealfff.managers.Handler;
import org.unrealfff.models.Route;
import org.unrealfff.utility.Console;
import org.unrealfff.utility.Response;
/**
 * Класс команды для сохранения
 */
public class Save extends Command{
    private final Console console;
    private final CollectionManager collectionManager;

    /**
     * Стандартный конструктор команды save.
     * @param c консоль для ввода/вывода.
     * @param cm менеджер коллекции для взаимодействия.
     */
    public Save(Console c, CollectionManager cm){
        super("save", "save - saves collection in a file");
        console = c;
        collectionManager = cm;
    }

    /**
     * Выполняет команду save
     * @param arguments Аргумент для выполнения
     * @return ResultResponse - результат выполнения команды.
     */
    @Override
    public Response execute(String[] arguments) {
        if (!arguments[1].isEmpty()) return new Response(0, "wrong usage of the command '" + getName() + "'");
        collectionManager.save();
        return new Response("collection has been saved");
    }
}

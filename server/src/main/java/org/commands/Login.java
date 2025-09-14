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
public class Login extends Command {
    private final Console console;
    private final FileManager fileManager;
    /**
     * Стандартный конструктор команды add
     * @param console
     */
    public Login(Console console, FileManager fileManager){
        super("login", "login - logs in a user");
        this.console = console;
        this.fileManager = fileManager;
    }


    /**
     * Применение команды Add.
     * @param arguments Аргумент для выполнения (проверяем, что их не передали).
     * @return ResultResponse - результат выполнения команды.
     */
    @Override
    public Response execute(String[] arguments, String[] login) {
        try {
            if(!arguments[1].isEmpty()) {
                System.out.println("Error: wrong arguments");
                return new Response(0, "wrong usage of the command '" + getName() + "'");
            }

            if (!fileManager.login(login).equals(-1)) {
                return new Response(1, "user logged in");
            }
            return new Response(0, "could not log in user");

        } catch (Exception e) {
            System.out.println("Error occurred while logging in");
            return new Response(0, "could not log in user");
        }
    }
}
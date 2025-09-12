package org;

import org.managers.NetworkManager;
import org.unrealfff.commands.Commands;
import org.unrealfff.utility.StandartConsole;
import org.utility.Runner;

import java.util.HashMap;
import java.util.Map;


public class Client {
    public Client() {

    }
    public static void main(String[] args) {
        var console = new StandartConsole();
        NetworkManager networkManager = new NetworkManager();
        console.println("connecting to the server...");
        while (!networkManager.init()){}
        console.println("connection has been established");
        Map<Commands,String[]> commands = new HashMap<>();
        commands.put(Commands.Add, new String[]{"add", "adds 1 element to collection"});
        commands.put(Commands.Clear, new String[]{"clear", "clears the collection"});
        commands.put(Commands.Exit, new String[]{"exit", "stops the program"});
        commands.put(Commands.Help, new String[]{"help", "help - show available commands"});
        commands.put(Commands.Info, new String[]{"info", "show collecction info"});
        commands.put(Commands.RemoveById, new String[]{"remove_by_id id", "removes an element by id"});
        commands.put(Commands.RemoveLower, new String[]{"remove_lower {object}", "removes all elements, lower than given"});
        commands.put(Commands.Show, new String[]{"show", "shows collection"});
        commands.put(Commands.ExecuteScript, new String[]{"execute_script", "execute_script filename - executes script from file"});
        commands.put(Commands.Update, new String[]{"update {ID}", "updates all fields in the element with the same id"});
        commands.put(Commands.AddIfMin, new String[] {"add_if_min", "add_if_min {element} - adds 1 element to collection if it is the lowest one"});
        commands.put(Commands.CountGreaterThanDistance, new String[]{"count_greater_than_distance", "count_greater_than_distance distance - number of elements with distance greater then given"});
        commands.put(Commands.PrintFieldDescendingDistance, new String[]{"print_field_descending_distance", "print_field_descending_distance - prints all distances in descending order"});
        commands.put(Commands.Register, new String[]{"register", "register - registers a new user"});
        commands.put(Commands.RemoveAnyByDistance, new String[]{"remove_any_by_distance", "remove_any_by_distance distance - removes 1 element with given distance"});
        commands.put(Commands.History, new String[]{"history", "history - displays last 9 commands"});


        new Runner(networkManager, console, commands).interactiveMode();
    }
}

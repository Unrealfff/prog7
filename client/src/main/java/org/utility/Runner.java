package org.utility;

import org.commands.ExecuteScript;
import org.commands.Exit;
import org.commands.Help;
import org.commands.History;
import org.managers.NetworkManager;
import org.unrealfff.commands.Command;
import org.unrealfff.commands.Commands;
import org.unrealfff.commands.Container;
import org.unrealfff.managers.Handler;
import org.unrealfff.models.Route;
import org.unrealfff.utility.Console;
import org.unrealfff.utility.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;

public class Runner {
    private Console console;
    private Map<Commands,String[]> commands;
    private final List<String> scriptStack = new ArrayList<>();
    private int lengthRecursion = -1;
    private NetworkManager networkManager;
    private Map<Commands, Command> clientCommands;
    private ArrayList<String> history;


    public Runner(NetworkManager networkManager, Console console, Map<Commands,String[]> commands) {
        this.console = console;
        this.networkManager = networkManager;
        this.commands=commands;
        clientCommands = new HashMap<>();
        clientCommands.put(Commands.Exit, new Exit(console));
        clientCommands.put(Commands.ExecuteScript, new ExecuteScript(console));
        clientCommands.put(Commands.Help, new Help(console, commands));
        clientCommands.put(Commands.History, new History(console, this));
        this.history = new ArrayList<>();
    }

    public String[] login(){
        while (true) {
            console.println("enter login");
            console.prompt();
            String login = console.readln().trim();
            console.println("enter password");
            console.print("# ");
            String password = console.readPassword().trim();
            String[] data = {login, cypher(password)};
            return data;
        }
    }

    public String cypher(String password) {
        try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Интерактивный режим
     */
    public void interactiveMode() {
        try {
            Response commandStatus;
            String[] userCommand = {"", ""};

            while (true) {
                try {
                    console.prompt();
                    userCommand = (console.readln().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                    //console.println(userCommand[0]);
                    commandStatus = launchCommand(userCommand);
                    if (commandStatus.getMessage().equals("exit")) break;
                    console.println(commandStatus.getMessage());
                } catch (NoSuchElementException e) {
                    console.printError("could not read the command.");
                    console.selectConsole();
                } catch (IllegalStateException e) {
                    console.printError("unexpected error in command runtime");
                    console.selectConsole();
                }
            }
        } catch (Exception e) {
            console.printError("critical error: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Функция проверки скрипта на наличие рекурсии
     *
     * @param argument      название запускаемого скрипта
     * @param scriptScanner сканер скрипта
     * @return true, если скрипт можно запускать
     */
    private boolean checkRecursion(String argument, Scanner scriptScanner) {
        var recStart = -1;
        var i = 0;
        for (String script : scriptStack) {
            i++;
            if (argument.equals(script)) {
                if (recStart < 0) recStart = i;
                if (lengthRecursion < 0) {
                    console.selectConsole();
                    console.println("recursion has been spotted. enter recursion depth (0..500)");
                    while (lengthRecursion < 0 || lengthRecursion > 500) {
                        try {
                            console.prompt();
                            lengthRecursion = Integer.parseInt(console.readln().trim());
                            if (lengthRecursion < 0 || lengthRecursion > 500) {
                                console.println("lenth not recognized");
                            }
                        } catch (NumberFormatException e) {
                            console.println("length not recognized");
                        }
                    }
                    console.selectFile(scriptScanner);
                }
                if (i > recStart + lengthRecursion || i > 500)
                    return false;
            }
        }
        return true;
    }

    /**
     * Режим работы скрипта
     * @param argument название файла со скриптом
     * @return возвращает ответ о выполнении скрипта
     */
    private Response scriptMode(String argument) {
        String[] userCommand = {"", ""};
        StringBuilder executionOutput = new StringBuilder();

        if (!new File(argument).exists()) return new Response(0, "file does not exists");
        if (!Files.isReadable(Paths.get(argument))) return new Response(0, "can't read the file");

        scriptStack.add(argument);
        try (Scanner scriptScanner = new Scanner(new File(argument))) {

            Response commandStatus;

            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            console.selectFile(scriptScanner);
            do {
                userCommand = (console.readln().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                while (console.canReadln() && userCommand[0].isEmpty()) {
                    userCommand = (console.readln().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                }
                executionOutput.append(console.getPrompt() + String.join(" ", userCommand) + "\n");
                var needLaunch = true;
                if (userCommand[0].equals("execute_script")) {
                    needLaunch = checkRecursion(userCommand[1], scriptScanner);
                }
                commandStatus = needLaunch ? launchCommand(userCommand) : new Response("recursion depth exceeded");
                if (userCommand[0].equals("execute_script")) console.selectFile(scriptScanner);
                executionOutput.append(commandStatus.getMessage() + "\n");
            } while (commandStatus.getExitCode() == 1 && !commandStatus.getMessage().equals("exit") && console.canReadln());

            console.selectConsole();
            if (commandStatus.getExitCode() == 0 && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
                executionOutput.append("script contains corrupted data\n");
            }

            return new Response(commandStatus.getExitCode(), executionOutput.toString());
        } catch (FileNotFoundException exception) {
            return new Response(0, "file not found");
        } catch (NoSuchElementException exception) {
            return new Response(0, "file is empty");
        } catch (IllegalStateException exception) {
            console.printError("unexpected");
            System.exit(0);
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
        return new Response("");
    }

    /**
     * Функиция загрузки команды
     * @param userCommand загружаемая команда
     * @return возвращает ответ о выполнении программы
     */
    private Response launchCommand(String[] userCommand) {
        if (!networkManager.isConnected()) {
            console.println("reconnecting");
            if(!networkManager.init()) {
                console.println("reconnection failed");
                return new Response(0, "server is not responding");
            }
            console.println("connection has been reestablished");
        }
        history.add(userCommand[0]);
        if (userCommand[0].equals("")) return new Response("");
        var command = Commands.get(userCommand[0]);
        if(!commands.containsKey(command)) {
            command=null;
        }

        if (command == null)
            return new Response(0, "command '" + userCommand[0] + "' not found");

        try {
            switch (userCommand[0]) {
                case "execute_script" -> {
                    Response tmp = new ExecuteScript(console).execute(userCommand);
                    if (tmp.getExitCode() != 1) return tmp;
                    Response tmp2 = scriptMode(userCommand[1]);
                    return new Response(tmp2.getExitCode(), tmp.getMessage() + "\n" + tmp2.getMessage().trim());
                }
                default -> {
                    byte[] bytes;
                    String[] registration = login();
                    if(command.needsRoute()) {
                        try {
                            Route route = null;
                            try {
                                route = Handler.getRoute(console, Integer.parseInt(userCommand[1]), LocalDate.now());
                            }
                            catch (NumberFormatException e) {
                                route = Handler.getRoute(console, -1, LocalDate.now());
                            }
                            if (route == null) {
                                return new Response(0, "could not create the object");
                            }
                            bytes = NetworkManager.serializeData(new Container(command, route.toString(), registration));
                            //System.out.println(route);
                        } catch (NoSuchElementException e) {
                            console.selectConsole();
                            return new Response(0, "COuld not read the data");
                        } catch (IllegalStateException e) {
                            console.selectConsole();
                            return new Response(0, "Could not read the data");
                        }
                    } else if (!command.serverBased()) {
                        /*if(command == Commands.Exit) {
                            bytes = NetworkManager.serializeData(new Container(Commands.Exit, "", registration));
                            networkManager.sendData(bytes);
                            //return clientCommands.get(Commands.Exit).execute(userCommand);
                        }*/
                            return clientCommands.get(command).execute(userCommand);
                    } else {
                        bytes = NetworkManager.serializeData(new Container(command, userCommand[1], registration));
                    }

                    networkManager.sendData(bytes);
                    var data = networkManager.receiveData();
                    return networkManager.deserializeData(data);
                }
            }
        } catch (Exception e) {
            console.printError("error during command runtime " + e.getMessage());
            return new Response(0, "error during command runtime");
        }
    }

    public ArrayList<String> getCommandHistory() {
        return this.history;
    }
}

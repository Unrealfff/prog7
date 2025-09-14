package org;

import org.managers.*;
import org.unrealfff.commands.Container;
import org.unrealfff.utility.Console;
import org.unrealfff.utility.StandartConsole;
import org.commands.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
    public static void main(String[] args) {
        var console = new StandartConsole();
        if (args.length != 0) {
            console.println("filename in env FILEPATH");
            System.exit(0);
        }
        var dumpManager = new FileManager(console);
        var collectionManager = new CollectionManager(dumpManager);
        if (!collectionManager.init()) {
            System.err.println("couldn't load the collection");
            System.exit(1);
        }
        var commandManager = new CommandManager() {{
            //register("help", new Help(console, this));
            register("add", new Add(console, collectionManager, dumpManager));
            register("add_if_min", new AddIfMin(console, collectionManager, dumpManager));
            register("clear", new Clear(console, collectionManager, dumpManager));
            register("register", new Register(console, dumpManager));
            register("exit", new Exit(console));
            register("info", new Info(console, collectionManager, dumpManager));
            register("remove_by_id", new RemoveById(console, collectionManager, dumpManager));
            register("save", new Save(console, collectionManager));
            register("show", new Show(console, collectionManager, dumpManager));
            register("update", new Update(console, collectionManager, dumpManager));
            register("login", new Login(console, dumpManager));
            register("print_field_descending_distance", new PrintFieldDescendingDistance(console, collectionManager, dumpManager));
            register("count_greater_than_distance", new CountGreaterThanDistance(console, collectionManager, dumpManager));
            register("remove_any_by_distance", new RemoveAnyByDistance(console, collectionManager, dumpManager));
            register("remove_lower", new RemoveLower(console, collectionManager, dumpManager));
        }};
        var networkManager = new NetworkManager();
        Router router = new Router(console, commandManager);
        if (!networkManager.init(router, console)) {
            System.err.println("couldn't initiate networking");
            System.exit(1);
        }
        collectionManager.sort();


        System.out.println("server is ready");
        networkManager.run();
    }
}
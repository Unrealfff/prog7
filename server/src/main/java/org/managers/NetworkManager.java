package org.managers;

import org.unrealfff.commands.Commands;
import org.unrealfff.commands.Container;
import org.unrealfff.utility.Console;
import org.unrealfff.utility.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashSet;
import java.util.concurrent.*;


/**
 * Класс для обмена данными с клиентом
 */
public class NetworkManager {
    Selector selector;
    ServerSocketChannel serverSocket;
    HashSet<SocketChannel> clients;
    Router router;
    boolean running;
    Console console;
    ExecutorService cachedPool;
    //ExecutorService processPool;
    ExecutorService writePool;


    public boolean init(Router router, Console console) {
        try {
            selector = Selector.open();
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(5454));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            clients = new HashSet<>();
            this.router = router;
            running = true;
            this.console = console;
            this.cachedPool = Executors.newCachedThreadPool();
            //this.processPool = Executors.newCachedThreadPool();
            this.writePool = Executors.newFixedThreadPool(4);
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
    public Selector getSelector() {
        return this.selector;
    }

    public void handleConnection(SocketChannel client, ExecutorService pool) {
        try {
            ByteBuffer inBuffer = ByteBuffer.allocate(1024 * 8);
            //ByteBuffer outBuffer = ByteBuffer.allocate(1024);
            int bytesRead = -1;
            try {
                bytesRead = client.read(inBuffer);
            } catch (ClosedChannelException | SocketException e) {
                clients.remove(client);
                selector.selectedKeys().clear();
                System.out.println("client disconnected");
                client.close();
                //continue;

            }
            if (bytesRead == -1) {
                client.close();
                clients.remove(client);
            }
            inBuffer.flip();
            //var data = new String(byteBuffer.array(), byteBuffer.position(), bytesRead);
            var data = deserialize(inBuffer.array());
            inBuffer.clear();
            Future<Response> response = cachedPool.submit(() -> processData(data));
            sendData(response, client);
            //return data;
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public Response processData(Container containerFuture) {
        /*while(!containerFuture.isDone()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            catch (InterruptedException e) {
                containerFuture.cancel(true);
            }
        }*/
        try {
            return router.run(containerFuture);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendData(Future<Response> responseFuture, SocketChannel client) {
        while(!responseFuture.isDone()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            catch (InterruptedException e) {
                responseFuture.cancel(true);
            }
        }
        try {
            ByteBuffer outBuffer = ByteBuffer.wrap(serializer(responseFuture.get()));
            //outBuffer.flip();
            try {
                while (outBuffer.hasRemaining()) {
                    client.write(outBuffer);
                }
            } catch (ClosedChannelException e) {
                clients.remove(client);
                client.close();
                selector.selectedKeys().clear();
                //System.out.println("client disconnected");
                //continue;
            }
            outBuffer.clear();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
        while (running) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            checkInput();
            if(!running) break;
                if(selector.select() == 0) continue;
                //System.out.println(selector.selectedKeys().size());
                for (var key : selector.selectedKeys()) {
                    if (key.isAcceptable()) {
                        if(key.channel() instanceof ServerSocketChannel channel) {
                            var client = channel.accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);
                            clients.add(client);
                        }
                        else {
                            throw new RuntimeException("unknown channel");
                        }
                    }
                    else if(key.isReadable()) {
                        if(key.channel() instanceof SocketChannel client) {
                            cachedPool.execute(() -> handleConnection(client, cachedPool));
                            //Future<Response> response = cachedPool.submit(() -> processData(data));
                            //writePool.execute(() -> sendData(response, client));
                        }
                        else {
                            throw new RuntimeException("unknown channel");
                        }
                    }
                }
                selector.selectedKeys().clear();
            }
            System.exit(0);
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            for (var client : clients) {
                try{
                    client.close();
                }
                catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }

    /**
     * Метод для сериализации данных
     * @param obj Объект
     * @return сериализованные данные
     */
    public static byte[] serializer(Object obj) {
        if (obj == null) return null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            System.err.println("Serialization failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Метод для сериализации полученных данных
     * @param bytes данные
     * @return десериализованный объект
     */
    public static Container deserialize(byte[] bytes) {
        if (bytes == null) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (Container) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            //System.err.println("Deserialization failed: " + e.getMessage());
            return null;
        }
    }
    private void checkInput() {
        if (console.isEampty() > 0) {
            String line = console.readln().trim().toLowerCase();
            switch (line) {
                case "exit":
                    //System.out.println("hey");
                    running = false;
                    writePool.shutdown();;
                    cachedPool.shutdown();
                    //readPool.shutdown();
                    System.exit(0);
                    break;
                //case "save":
                 //   router.run(new Container(Commands.Save, ""));
                   // break;
            }
        }
    }
}
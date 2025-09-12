package org.managers;

import org.unrealfff.utility.Response;

import java.io.*;
import java.net.*;
import java.util.Arrays;

/**
 * Класс для обмена данными с сервером
 */
public class NetworkManager {
    private InetAddress host;
    private int port;
    private SocketAddress serverAddress;
    private Socket sock;
    InputStream is;
    OutputStream os;
    private boolean connected = false;

    public NetworkManager() {

    }

    public boolean isConnected() {
        return connected;
    }

    /**
     * Метод для настройки подключения
     * @return результат инициализации
     */
    public boolean init() {
        try {
            host = InetAddress.getByName("localhost");
            port = 5454;
            serverAddress = new InetSocketAddress(host, port);
            sock = new Socket(host, port);
            os = sock.getOutputStream();
            is = sock.getInputStream();
            connected = true;
            return true;
        }
        catch (IOException e) {
            //System.err.println("Initialization failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Отправить данные на сервер
     * @param arr массив данных
     * @return результат отправки
     */
    public boolean sendData(byte[] arr) {
        if (arr == null || serverAddress == null) return false;

        try {
            //sock = new Socket(InetAddress.getLocalHost(), port);
            os.write(arr);
            return true;
        } catch (IOException e) {
            System.err.println("Failed to send data: " + e.getMessage());
            return false;
        }
    }

    /**
     * мемтод для сериализации данных
     * @param obj Объект
     * @return сериализованные данные
     */
    public static byte[] serializeData(Object obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            System.err.println("Serialization failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * метод для сериализации данных
     * @param bytes сериализованные данные
     * @return результат десериализации
     */
    public Response deserializeData(byte[] bytes) {
        if (bytes == null || Arrays.equals(bytes, new byte[bytes.length])) {
            this.connected = false;
            return new Response(0, "server not responding");
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (Response) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            System.err.println("Deserialization failed: " + e.getMessage());
            return new Response(0, "deserialization failed: " + e.getMessage());
        }
    }

    /**
     * Метод для получения данных от сервера
     * @return массив полученных данных
     */
    public byte[] receiveData() {
        try {
            byte[] data = new byte[1024*8];
            is.read(data);
            return data;

        }
        catch (IOException e) {
            System.err.println("Receive failed: " + e.getMessage());
            return null;
        }
    }
}
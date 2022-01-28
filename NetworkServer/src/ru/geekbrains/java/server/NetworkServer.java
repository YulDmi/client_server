package ru.geekbrains.java.server;

import ru.geekbrains.java.Auth.Authentication;
import ru.geekbrains.java.Auth.NetworkAuth;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetworkServer {
    private final int port;
    private final List<ClientHandler> clients;
    private Authentication auth;

    public NetworkServer(int port) {
        this.port = port;
        clients = new ArrayList<>();
        this.auth = new NetworkAuth();
    }

    public void start() {
        Socket clientSocket ;

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Сервер запущен. Ожидаем подключения клиента....");
            auth.start();
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Клиент подключен. Аутентификация...");
                clients.add(new ClientHandler(this, clientSocket));
            }
        } catch (IOException e) {
            System.out.println("Ошибка при работе с сервером");
        }
        finally {
                auth.stop();
        }
    }

    public Authentication getAuth() {
        return auth;
    }

    public synchronized void broadcastMessage(String name, String massage) {
        for (ClientHandler ch : clients){
            try {
                if (ch.getNikName().equals(name)) continue;
                ch.sendMessage(String.format("От %s : %s%n", name, massage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void broadcastMessageByName (String name, String massage)    {
        for (ClientHandler ch : clients){
            try {
                if (ch.getNikName().equals(name))
                ch.sendMessage(massage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public synchronized void uncheckClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }
}

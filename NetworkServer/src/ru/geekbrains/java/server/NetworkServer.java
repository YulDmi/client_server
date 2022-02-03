package ru.geekbrains.java.server;

import ru.geekbrains.java.Auth.Authentication;
import ru.geekbrains.java.Auth.JdbcApp;
import ru.geekbrains.java.Auth.NetworkAuth;
import ru.geekbrains.java.Command;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.geekbrains.java.Command.commandUpdateUserList;

public class NetworkServer {
    private final int port;
    private final List<ClientHandler> clients;
    private Authentication auth;


    public NetworkServer(int port) {
        this.port = port;
        clients = new ArrayList<>();
        this.auth = new NetworkAuth();
    }

    public Authentication getAuth() {
        return auth;
    }

    public void start() {
        Socket clientSocket;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен. Ожидаем подключения клиента....");
            auth.start();
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Клиент подключен.");
                new ClientHandler(this, clientSocket);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при работе с сервером");
        } finally {
            auth.stop();
        }
    }

    public synchronized void broadcastMessage(String name, Command command) {
        for (ClientHandler ch : clients) {
            try {
                if (ch.getNikName().equals(name)) continue;
                ch.sendMessage(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void broadcastMessageByName(String receiver, Command command) {
        for (ClientHandler ch : clients) {
            try {
                if (ch.getNikName().equals(receiver))
                    ch.sendMessage(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void uncheckClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastMessage(null, commandUpdateUserList(getAllNames()));
    }

    public synchronized boolean addClients(ClientHandler clientHandler) {
        if (!clients.isEmpty()) {
            for (ClientHandler ch : clients) {
                if (ch.getNikName().equals(clientHandler.getNikName())) return false;
            }
        }
        clients.add(clientHandler);
        return true;
    }

    public List<String> getAllNames() {
        return clients.stream()
                .map(ClientHandler::getNikName)
                .collect(Collectors.toList());
    }
}

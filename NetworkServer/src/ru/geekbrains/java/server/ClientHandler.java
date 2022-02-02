package ru.geekbrains.java.server;

import ru.geekbrains.java.Auth.Authentication;
import ru.geekbrains.java.Auth.NetworkAuth;
import ru.geekbrains.java.Command;
import ru.geekbrains.java.CommandType;
import ru.geekbrains.java.command.CommandAuth;
import ru.geekbrains.java.command.CommandPrivateMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static ru.geekbrains.java.Command.*;

public class ClientHandler {
    private final NetworkServer networkServer;
    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String nikName;


    public ClientHandler(NetworkServer server, Socket socket) {
        this.networkServer = server;
        this.clientSocket = socket;
        init();
    }

    public String getNikName() {
        return nikName;
    }

    private void init() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            new Thread(() -> {
                try {
                    System.out.println("Попытка аутентификации");
                    authentication();
                    readMessage();
                } catch (IOException e) {
                    System.err.println("соединение закрыто со стороны клиента");
                } finally {
                    closeConnection();
                    System.out.println("соединение с клиентом закрыто на сервере");
                }
            }).start();
        } catch (IOException e) {
            System.err.println("Ошибка при обработке соединения");
        }
    }

    private void authentication() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            if (command.getType() == CommandType.AUTH) {
                if (processAuthCommand(command)) return;
            } else {
                System.err.println("Unknown command " + command.getType());
            }
        }
    }


    private boolean processAuthCommand(Command command) throws IOException {
        CommandAuth commandAuth = (CommandAuth) command.getData();
        String login = commandAuth.getLogin();
        String password = commandAuth.getPassword();
        Authentication networkAuth = networkServer.getAuth();
        String username = networkAuth.UsernameByLoginAndPassword(login, password);
        if (username == null) {
            sendMessage(commandAuthError("Ошибка аутентификации. Нет такого пользователя"));
            return false;
        }
        nikName = username;
        boolean isAddClient = networkServer.addClients(this);
        if (isAddClient) {
            commandAuth.setName(nikName);
            sendMessage(command);
            networkServer.broadcastMessage(null, commandUpdateUserList(networkServer.getAllNames()));
            networkServer.broadcastMessage(nikName, commandMessage(nikName + " присоединился к чату!"));
            System.out.println("Успешная аутентификация");
            return true;
        } else {
            sendMessage(commandAuthError(String.format("Пользователь %s с таким логином и паролем уже подключен к чату.", username)));
            return false;
        }
    }


    private void readMessage() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            switch (command.getType()) {
                case MESSAGE:
                    networkServer.broadcastMessage(nikName, command);
                    break;
                case PRIVATE_MESSAGE:
                    CommandPrivateMessage privateMessage = (CommandPrivateMessage) command.getData();
                    String receiver = privateMessage.getName();
                    networkServer.broadcastMessageByName(receiver, command);
                    break;
                case END:
                    System.err.println("Соединение разорвано");
                    return;
            }
        }
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) in.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "not exist command";
            e.printStackTrace();
            sendMessage(commandError(errorMessage));
            System.err.println(errorMessage);
            return null;
        }
    }

    public void sendMessage(Command command) throws IOException {
        out.writeObject(command);
    }

    private void closeConnection() {
        try {
            networkServer.uncheckClient(this);
            networkServer.broadcastMessage(nikName, commandMessage(nikName + " покинул чат."));
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

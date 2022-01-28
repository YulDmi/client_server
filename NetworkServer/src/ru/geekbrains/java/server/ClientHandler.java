package ru.geekbrains.java.server;

import ru.geekbrains.java.Auth.Authentication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private final NetworkServer networkServer;
    private final Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;
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
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());

            new Thread(() -> {
                try {
                    System.out.println("Попытка аутентификации");
                    authentication();
                    readMessage();
                } catch (IOException e) {
                    System.out.println(nikName + " - покинул чат.");
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            System.out.println("Ошибка при обработке соединения");
        }
    }

    private void readMessage() throws IOException {
        while (true) {
            String message = in.readUTF();
            if ("/end".equals(message)) {
                System.out.println("Соединение разорвано");
                return;
            }
            String[] text = message.split("\\s+", 2);
            String name = text[0];
            String mess = text[1];
            if (name.equals("All")) {
                networkServer.broadcastMessage(nikName, message);
            } else
                networkServer.broadcastMessageByName(name, String.format("Приватно от %s : %s%n", nikName, mess));
        }
    }

    private void authentication() throws IOException {

        while (true) {
            Authentication auth = networkServer.getAuth();
            String message = in.readUTF();
            if (message.startsWith("/auth")) {
                String[] partMessage = message.split("\\s+", 3);
                String username = auth.UsernameByLoginAndPassword(partMessage[1], partMessage[2]);
                if (username == null) {
                    sendMessage("/auth Err");
                    System.out.println("Ошибка аутентификации");
                } else {
                    sendMessage("/auth " + username);
                    nikName = username;
                    networkServer.broadcastMessage(nikName,  "присоединился к чату!");
                    System.out.println("Успешная аутентификация");
                    break;
                }
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }


    private void closeConnection() {
        try {
            networkServer.uncheckClient(this);
            networkServer.broadcastMessage(nikName, " покинул чат." );
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

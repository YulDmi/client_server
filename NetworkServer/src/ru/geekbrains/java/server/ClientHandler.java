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

    public ClientHandler(NetworkServer server, Socket socket ) {
        this.networkServer = server;
        this.clientSocket = socket;
        init();
    }

    private void init() {
        try {
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            new Thread(() -> {
                try {
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
            System.out.printf("От %s : %s%n", nikName, message);

            if ("/end".equals(message)) {
                System.out.println("Соединение разорвано");
                return;
            }
            networkServer.broadcastMessage(String.format("От %s : %s%n", nikName, message));

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
                    sendMessage("нет такого пользователя");
                } else {
                    nikName = username;
                    networkServer.broadcastMessage(nikName + "присоединился к чату!");
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
            networkServer.broadcastMessage(String.format("%s : покинул чат. %n", nikName));
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

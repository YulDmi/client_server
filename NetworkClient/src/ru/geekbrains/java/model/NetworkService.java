package ru.geekbrains.java.model;

import ru.geekbrains.java.Command;
import ru.geekbrains.java.command.*;
import ru.geekbrains.java.controller.ClientController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import static ru.geekbrains.java.Command.*;

public class NetworkService {
    private String HOST;
    private int PORT;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ClientController controller;
    private volatile String nikName;
    private final String USER_ALL = "All";
    private Socket client;

    public NetworkService(ClientController controller) {
        this.controller = controller;
        this.PORT = controller.getPORT();
        this.HOST = controller.getHOST();
    }

    public void connect() {
        try {
            client = new Socket(HOST, PORT);
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
            new Thread(() -> {
                try {
                    reedMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }


    public void reedMessage() throws IOException {
        while (true) {
            Command command;
            try {
                command = (Command) in.readObject();
                if (command == null) continue;
                switch (command.getType()) {
                    case AUTH:
                        CommandAuth commandAuth = (CommandAuth) command.getData();
                        nikName = commandAuth.getName();
                        controller.runChat(nikName);
                        break;
                    case AUTH_ERROR:
                        CommandAuthError commandAuthError = (CommandAuthError) command.getData();
                        controller.viewErrorAuth(commandAuthError.getMessageError());
                        break;
                    case UPDATE_USER_LIST:
                        CommandUpdateUserList cul = (CommandUpdateUserList) command.getData();
                        List<String> names = cul.getList();
                        names.remove(nikName);
                        names.add(0, USER_ALL);
                        controller.updateList(names);
                        break;
                    case ERROR:
                        CommandError commandError = (CommandError) command.getData();
                        String messageError = commandError.getMessageError();
                        System.out.println(messageError);
                        controller.viewErrorAuth(messageError);
                        break;
                    case MESSAGE:
                        CommandMessage commandMessage = (CommandMessage) command.getData();
                        controller.viewMessage(USER_ALL + " <- ", commandMessage.getMessage());
                        break;
                    case PRIVATE_MESSAGE:
                        CommandPrivateMessage commandPrivateMessage = (CommandPrivateMessage) command.getData();
                        controller.viewMessage("Мне <- ", commandPrivateMessage.getMessage());
                        break;
                    default:
                        System.err.println("Не известная комманда");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Не известная комманда");
            }
        }
    }


    public void sendMessage(String user, String message) throws IOException {
        String mess = String.format("От %s : %s", nikName, message);

        if (user.equals(USER_ALL)) {
            sendCommand(commandMessage(mess));
        } else {
            sendCommand(commandPrivateMessage(user, mess));
        }
    }

    public void sendCommand(Command command) throws IOException {
        out.writeObject(command);
    }

    private void close() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}

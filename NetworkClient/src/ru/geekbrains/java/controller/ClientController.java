package ru.geekbrains.java.controller;

import ru.geekbrains.java.model.NetworkService;
import ru.geekbrains.java.view.AuthDilog;
import ru.geekbrains.java.view.ClientForm;

import javax.swing.*;
import java.io.IOException;

public class ClientController {


    private final String HOST;
    private final int PORT;
    private String nikName;
    private MessageHandler messageHandler;
    private NetworkService networkService;
    private AuthDilog authDilog;
    private ClientForm clientForm;

    public ClientController(String host, int port)   {
        this.HOST = host;
        this.PORT = port;
//        networkService = new NetworkService(this);
//        //  messageHandler = new MessageHandler();
//        authDilog = new AuthDilog(this);
//
//        clientForm = new ClientForm(this);
    }

    public String getHOST() {
        return HOST;
    }

    public int getPORT() {
        return PORT;
    }

    public void startController() {
        networkService.connect();
         runAuthProcess();

    }

    private void runAuthProcess() {
        authDilog.setVisible(true);

    }

    public void sendAuth(String message) throws IOException {
        networkService.sendMessage(message);

        System.out.println(message);
        if (networkService.isAuthented()) {
            authDilog.setVisible(false);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    clientForm.init();
                }
            });
        } else authDilog.viewError("нет такого пользователя");
    }
}

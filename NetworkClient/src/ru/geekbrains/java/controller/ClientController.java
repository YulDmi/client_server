package ru.geekbrains.java.controller;

import ru.geekbrains.java.model.NetworkService;
import ru.geekbrains.java.view.AuthDilog;
import ru.geekbrains.java.view.ClientForm;

import java.io.IOException;

public class ClientController {


    private final String HOST;
    private final int PORT;
    private NetworkService networkService;
    private AuthDilog authDilog;
    private ClientForm clientForm;

    public ClientController(String host, int port) {
        this.HOST = host;
        this.PORT = port;
        networkService = new NetworkService(this);
        authDilog = new AuthDilog(this);
        clientForm = new ClientForm(this);
    }

    public String getHOST() {
        return HOST;
    }

    public int getPORT() {
        return PORT;
    }

    public void startController() {
        networkService.connect();
        authDilog.setVisible(true);
    }

    public void sendAuth(String message) throws IOException {
        networkService.sendMessage(message);
    }

    public void runChat(String name) {
        authDilog.dispose();
        clientForm.init();
        clientForm.setTitle(name);
        clientForm.setVisible(true);
    }

    public void sendMessage(String user, String message) throws IOException {
        networkService.sendMessage(user + " " + message);

    }

    public void viewMessage(String name, String message) {
        clientForm.appendMessage(name, message);
    }

    public void viewErrorAuth(String massage) {
        authDilog.viewError(massage);
    }
}

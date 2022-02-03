package ru.geekbrains.java.controller;

import ru.geekbrains.java.Command;
import ru.geekbrains.java.model.NetworkService;
import ru.geekbrains.java.view.AuthDilog;
import ru.geekbrains.java.view.ClientForm;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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

    public void sendAuth(String log, String pass) throws IOException {
        networkService.sendCommand(Command.commandAuth(log, pass));
    }

    public void runChat(String name) {
        authDilog.dispose();
        clientForm.init();
        clientForm.setTitle(name);
        clientForm.setVisible(true);
    }

    public void sendMessage(String user, String message) throws IOException {
         networkService.sendMessage(user, message);
    }

    public void viewMessage(String name, String message) {
        clientForm.appendMessage(name, message);
    }

    public void viewErrorAuth(String massage) {
        authDilog.viewError(massage);
    }

    public void updateList(List<String> list) {
        clientForm.updateUserList(list);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientController that = (ClientController) o;
        return PORT == that.PORT &&
                Objects.equals(HOST, that.HOST) &&
                Objects.equals(networkService, that.networkService) &&
                Objects.equals(authDilog, that.authDilog) &&
                Objects.equals(clientForm, that.clientForm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(HOST, PORT, networkService, authDilog, clientForm);
    }


}

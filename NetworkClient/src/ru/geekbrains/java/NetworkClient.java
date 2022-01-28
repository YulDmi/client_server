package ru.geekbrains.java;

import ru.geekbrains.java.controller.ClientController;

public class NetworkClient {
    public static void main(String[] args) {


        ClientController clientController = new ClientController("localhost", 8189);
        clientController.startController();


    }
}

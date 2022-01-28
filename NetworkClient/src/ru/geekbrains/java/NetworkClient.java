package ru.geekbrains.java;

import ru.geekbrains.java.controller.ClientController;

import java.io.IOException;

public class NetworkClient {
    private static final int DEFAULT_PORT = 8189;
    private static final String HOST = "localhost";

    public static void main(String[] args) {


          ClientController  clientController = new ClientController(HOST, DEFAULT_PORT);
            clientController.startController();


    }
}

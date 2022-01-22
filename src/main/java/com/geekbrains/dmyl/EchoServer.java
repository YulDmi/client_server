package com.geekbrains.dmyl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {
    private final static String END_COMMAND = "/end";
    private final static int PORT = 8189;
    private static ServerSocket serverSocket  ;
    private static Socket clientSocket  ;

    public static void main(String[] args) {

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Сервер запущен. Ждем подключения клиента....");
            clientSocket = serverSocket.accept();
            System.out.println("Клиент подключен");
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String str = null;
                        try {
                            str = in.readUTF();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (str.equals(END_COMMAND)) {
                            break;
                        }
                        System.out.println("Сообщение от клиента : " + str);
                    }
                }
            }).start();


            sendMessage(out);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null && clientSocket != null) {
                try {
                    serverSocket.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void sendMessage(DataOutputStream out) {
        Scanner scanner = new Scanner(System.in);

        while (!serverSocket.isClosed()) {
            String message = scanner.nextLine();
            try {
                out.writeUTF("Server : " + message);
                out.flush();
            } catch (IOException e) {
                System.out.println("Сообщение не было отправлено");
            }
        }
    }
}

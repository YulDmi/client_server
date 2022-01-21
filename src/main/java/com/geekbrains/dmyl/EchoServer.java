package com.geekbrains.dmyl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
private final static String END_COMMAND =  "/end";


    public static void main(String[] args) {

        Socket clientSocket = null;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(8189);
            System.out.println("Сервер запущен. Ждем подключения клиента....");
            clientSocket = serverSocket.accept();
            System.out.println("Клиент подключен");
            DataInputStream in = new DataInputStream(clientSocket.getInputStream() );
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            while (true){
                String str = in.readUTF();
                if (str.equals(END_COMMAND)){
                    break;
                }
                System.out.println("Сообщение от клиента : " + str);
                out.writeUTF(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
}

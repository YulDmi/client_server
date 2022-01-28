package ru.geekbrains.java.model;

import ru.geekbrains.java.controller.ClientController;
import ru.geekbrains.java.controller.MessageHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkService {
    private   String HOST;
    private   int PORT;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean authtorized = false;
    private MessageHandler messageHandler;
    private ClientController controller;
    private String nikName;
    //   private Authentication auth;

public NetworkService(){
    HOST = "localhost";
    PORT = 8189;
}

    public NetworkService(ClientController controller) {
        this.controller = controller;
        this.PORT = controller.getPORT();
        this.HOST = controller.getHOST();
    }

    public void connect() {
        Socket socket = null;
        try {
            socket = new Socket(HOST, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            System.out.println("connected");

        //    runReedMessage();
            new Thread(() -> {

                // try {

                reedMessage();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
//        } finally {
//            if (socket != null) {
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
 //           }
        }
    }


    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
        System.out.println("send");
    }


    public void reedMessage() {

        System.out.println("Test reed Message");
        while (true) {
            try {
                String text = in.readUTF();
                if (text.startsWith("/auth")) {
                    String name = text.split("\\s+", 2)[1];
                    if (!name.equals("Err")) {
                        authtorized = true;
                        nikName = name;
                        break;
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }


        }
    }


    public boolean isAuthented() {
        return authtorized;
    }

    public static void main(String[] args) {
        NetworkService networkService = new NetworkService();
        networkService.connect();
    }

}

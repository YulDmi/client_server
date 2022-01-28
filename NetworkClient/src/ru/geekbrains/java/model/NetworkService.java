package ru.geekbrains.java.model;

import ru.geekbrains.java.controller.ClientController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkService {
    private String HOST;
    private int PORT;
    private DataOutputStream out;
    private DataInputStream in;
    private volatile boolean authtorized = false;
    private ClientController controller;
    private String nikName;

    public NetworkService(ClientController controller) {
        this.controller = controller;
        this.PORT = controller.getPORT();
        this.HOST = controller.getHOST();
    }

    public String getNikName() {
        return nikName;
    }
    public void connect() {
        Socket client = null;
        try {
            client = new Socket(HOST, PORT);
            out = new DataOutputStream(client.getOutputStream());
            in = new DataInputStream(client.getInputStream());

            Thread t = new Thread(this::reedMessage);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }


    public void reedMessage() {
        while (true) {
            try {
                String text = in.readUTF();
                if (text.startsWith("/auth")) {
                    String name = text.split("\\s+", 2)[1];
                    if (!name.equals("Err")) {
                        authtorized = true;
                        nikName = name;
                    }
                    continue;
                }
                if (text.startsWith("/end")) {
                    controller.viewMessage("Server", " Соединение разорвано");
                    return;
                }
                String[] partText = text.split("\\s+", 2);
                controller.viewMessage(partText[0], partText[1]);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }


    public boolean isAuthented() {
        while (!authtorized) ;
        return true;
    }


}

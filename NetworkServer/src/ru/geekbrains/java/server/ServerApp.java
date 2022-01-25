package ru.geekbrains.java.server;

public class ServerApp {
    public static final int DEFAULT_PORT = 8189;

    public static void main(String[] args) {
        int port = getPort(args);
        new NetworkServer(port).start();
    }

    private static int getPort(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length == 1){
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка идентификации номера порта");
            }
        }
        return port;
    }

}

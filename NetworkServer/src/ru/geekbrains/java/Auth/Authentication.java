package ru.geekbrains.java.Auth;

public interface Authentication {

    void start();

    void stop();

    String UsernameByLoginAndPassword(String login, String password);
}

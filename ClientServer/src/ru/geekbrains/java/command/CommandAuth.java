package ru.geekbrains.java.command;

import java.io.Serializable;

public class CommandAuth implements Serializable {
    private String login;
    private String password;
    private String name;

    public CommandAuth(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

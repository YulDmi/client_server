package ru.geekbrains.java.command;

import java.io.Serializable;

public class CommandPrivateMessage implements Serializable {
    private String name;
    private String  message;

    public CommandPrivateMessage(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String  getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }
}

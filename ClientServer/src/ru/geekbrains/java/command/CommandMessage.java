package ru.geekbrains.java.command;

import java.io.Serializable;

public class CommandMessage implements Serializable {

    private String  message;

    public CommandMessage(  String message) {

        this.message = message;
    }

    public String  getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

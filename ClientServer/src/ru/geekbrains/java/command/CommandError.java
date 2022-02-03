package ru.geekbrains.java.command;

import java.io.Serializable;

public class CommandError implements Serializable {
   private String messageError;

    public CommandError(String messageError) {
        this.messageError = messageError;
    }

    public String getMessageError() {
        return messageError;
    }
}

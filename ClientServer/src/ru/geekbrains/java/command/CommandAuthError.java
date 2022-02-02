package ru.geekbrains.java.command;

import java.io.Serializable;

public class CommandAuthError implements Serializable {
   private String messageError;

    public CommandAuthError(String messageError) {
        this.messageError = messageError;
    }

    public String getMessageError() {
        return messageError;
    }
}

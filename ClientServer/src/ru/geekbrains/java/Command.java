package ru.geekbrains.java;

import ru.geekbrains.java.command.*;

import java.io.Serializable;
import java.util.List;

public class Command implements Serializable {
    private CommandType type;
    private Object data;

    public Command(CommandType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public CommandType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public static Command commandError(String message) {
        return new Command(CommandType.ERROR, new CommandError(message));
    }

    public static Command commandAuthError(String message) {
        return new Command(CommandType.AUTH_ERROR, new CommandAuthError(message));
    }
    public static Command commandAuth(String login, String password) {
        return new Command(CommandType.AUTH, new CommandAuth(login, password));
    }

    public static Command commandMessage(String message) {
        return new Command(CommandType.MESSAGE, new CommandMessage(message));
    }

    public static Command commandPrivateMessage(String name, String message) {
        return new Command(CommandType.PRIVATE_MESSAGE, new CommandPrivateMessage(name, message));
    }

    public static Command commandUpdateUserList(List<String> list) {
        return new Command(CommandType.UPDATE_USER_LIST, new CommandUpdateUserList(list));
    }

    public static Command commandEnd(){
        return new Command(CommandType.END, null);
    }

}

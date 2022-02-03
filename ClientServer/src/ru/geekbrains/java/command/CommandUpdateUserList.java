package ru.geekbrains.java.command;

import java.io.Serializable;
import java.util.List;

public class CommandUpdateUserList implements Serializable {

    private final List<String> list;

    public CommandUpdateUserList(List<String> list) {

        this.list = list;
    }

    public List<String> getList() {
        return list;
    }
}

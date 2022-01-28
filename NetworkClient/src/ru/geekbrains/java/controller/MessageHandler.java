package ru.geekbrains.java.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class MessageHandler {
    private DataOutputStream out;
    private DataInputStream in;

    public MessageHandler(DataInputStream in, DataOutputStream out) {
        this.in = in;
        this.out = out;
    }
}

package ru.geekbrains.java.view;

import ru.geekbrains.java.controller.ClientController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ClientForm extends JFrame{
    private ClientController clientController;
    private JTextArea chatText;
    private JButton sendButton;
    private JTextField messageTextField;
    private JList userList;
    private JPanel mainPanel;

    public ClientForm(ClientController clientController) {
        this.clientController = clientController;
    }

    public void init() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         setSize(640, 480);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        addListeners();
    }

    private void addListeners() {
        sendButton.addActionListener(e -> sendMessage());
        messageTextField.addActionListener(e -> sendMessage());
    }

    private void sendMessage() {
        String message = messageTextField.getText();
        if (message.isBlank()) {
            return;
        }

        String selectUser = (String) userList.getSelectedValue();
        if (selectUser != null) {
            try {
                clientController.sendMessage(selectUser, message);
                appendOwnMessage(selectUser, message);
            } catch (IOException e) {
                viewError("Сообщение не отправлено");
            }
        }
        messageTextField.setText(null);
    }

    private void appendOwnMessage(String user, String message) {
        appendMessage("Я : " + user, String.format("%s%n", message));
    }

    public void appendMessage(String name, String message) {
        SwingUtilities.invokeLater(() -> {
            String formattedMessage = String.format("%s: %s%n", name, message);
            chatText.append(formattedMessage);
        });
    }
    public void viewError(String err) {
        JOptionPane.showMessageDialog(null, err );
    }
}

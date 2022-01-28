package ru.geekbrains.java.view;

import ru.geekbrains.java.controller.ClientController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientForm extends JFrame{
    private ClientController clientController;
    private JTextArea chatText;
    private JButton sendButton;
    private JTextField messageTextField;
    private JList userList;
    private JPanel mainPanel;
   // private JFrame frame;

    public ClientForm(ClientController clientController) {
        this.clientController = clientController;


    }

    public void init() {
        //frame = new JFrame();
         setTitle("Chat");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         setSize(640, 480);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
      //  setVisible(true);
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
        appendOwnMessage(message);
        String selectUser = (String) userList.getSelectedValue();
        if (selectUser != null) {
            appendMessage(selectUser, message);
        }
        messageTextField.setText(null);
    }

    private void appendOwnMessage(String message) {
        appendMessage("Я", message);
    }

    private void appendMessage(String name, String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String formattedMessage = String.format("%s: %s%n", name, message);
                chatText.append(formattedMessage);
            }
        });
    }

    public void clientFormStart() {
        setVisible(true);
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                 ;
//            }
//        });

    //   }


}

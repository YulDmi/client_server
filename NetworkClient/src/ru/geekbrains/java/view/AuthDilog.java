package ru.geekbrains.java.view;

import ru.geekbrains.java.controller.ClientController;
import ru.geekbrains.java.model.NetworkService;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class AuthDilog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPasswordField passwordText;
    private JTextField loginText;
    private ClientController clientController;

    public AuthDilog(ClientController clientController) {

        this.clientController = clientController;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        pack();

    }

    private void onOK() {

    String login = loginText.getText().trim();
    String password = new String(passwordText.getPassword());
        try {
            clientController.sendAuth(login, password);
        } catch (IOException e) {
            viewError("Ошибка отправки данных");
        }
    }

    public void viewError(String err) {
        JOptionPane.showMessageDialog(null, err );
    }

    private void onCancel() {
        dispose();
    }

}

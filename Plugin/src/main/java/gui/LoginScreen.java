package gui;


import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

public class LoginScreen implements ActionListener {

    private Controller controller;
    private JPanel sendRequestScreenContent;
    private JButton loginButton;

    public LoginScreen(Controller controller) {
        this.controller = controller;
        loginButton.addActionListener(this);
        if(controller.isInitialized) {
            loginButton.setText("Login");
        } else {
            loginButton.setText("Registrieren");
        }
    }

    public JPanel getContent() {
        return sendRequestScreenContent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.showWelcomeMenu();
        try {
            if(controller.onLoginButtonPressed()) {
                controller.updateToolWindow();
            }
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
        //controller.updateToolWindow();
      controller.mailBoxScreen.refreshTableLogin();
    }

}
